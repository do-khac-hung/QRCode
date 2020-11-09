package com.example.huuquang.qrcode.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.huuquang.qrcode.adapter.ListMessageAdapter;
import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Message;
import com.example.huuquang.qrcode.model.Todo;
import com.example.huuquang.qrcode.model.User;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChatFragment extends Fragment
        implements ListMessageAdapter.MessageAdapterOnTouchListener{
    private final int RC_CSV_PICKER = 1;

    private List<Message> messages;
    private ListMessageAdapter messageAdapter;

    private RecyclerView mMessageRecycleView;
    private EditText mMessageEditText;
    private ImageButton mSendButton;
    private ImageButton attachmentButton;

    private DatabaseReference messagesRef;
    private DatabaseReference todoRef;
    private ChildEventListener messageListener;
    private FirebaseAuth mAuth;
    private StorageReference fileRef;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messages = new ArrayList<>();
        messageAdapter = new ListMessageAdapter(messages, this, this.getContext());

        mMessageEditText = view.findViewById(R.id.message_edit_text);

        mSendButton = view.findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mMessageEditText.getText().toString();
                if(content.trim().isEmpty()) return;

                String uid = UserUtil.getCurrentUser().getUid();
                String username = UserUtil.getCurrentUser().getFullname();

                Message sendMessage = new Message();
                sendMessage.setContent(content);
                sendMessage.setUid(uid);
                sendMessage.setFullName(username);

                Map<String, Object> data = sendMessage.toMap();
                data.put("time", ServerValue.TIMESTAMP);

                messagesRef.push().setValue(data);

                mMessageEditText.getText().clear();
            }
        });

        attachmentButton = view.findViewById(R.id.attachment_button);
        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_CSV_PICKER);
            }
        });

        mMessageRecycleView = view.findViewById(R.id.chat_rc);
        mMessageRecycleView.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setStackFromEnd(true);
        mMessageRecycleView.setLayoutManager(layoutManager);

        todoRef = FirebaseDatabase.getInstance().getReference("users")
                .child(UserUtil.getCurrentUser().getUid()).child("todos");

        fileRef = FirebaseStorage.getInstance().getReference(UserUtil.getCurrentUser().getCompany_id());

        messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(UserUtil.getCurrentUser().getCompany_id());
        messageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Message message = dataSnapshot.getValue(Message.class);
                if(message.getUid().equals(UserUtil.getCurrentUser().getUid())){
                    message.setType(Message.TYPE_MINE);
                }else{
                    message.setType(Message.TYPE_YOUR);
                }
                messageAdapter.addItem(message);
                mMessageRecycleView.scrollToPosition(messageAdapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_CSV_PICKER && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            String lastPathSegment = uri.getLastPathSegment();
            String filename = lastPathSegment;
            if(filename.contains("/")){
                String[] tmp = filename.split("/");
                filename = tmp[tmp.length-1];
            }

            //upload file
            fileRef.child(filename).putFile(uri)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();

                            Message message = new Message();
                            message.setUid(UserUtil.getCurrentUser().getUid());
                            message.setFullName(UserUtil.getCurrentUser().getFullname());
                            message.setLink(downloadUri.toString());

                            Map<String, Object> data = message.toMap();
                            data.put("time", ServerValue.TIMESTAMP);

                            messagesRef.push().setValue(data);
                        }
                    });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        messageAdapter.clear();
        detachMessageListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        attachMessageListener();
    }

    private void attachMessageListener(){
        messagesRef.addChildEventListener(messageListener);
    }

    private void detachMessageListener(){
        if(messageListener != null){
            messagesRef.removeEventListener(messageListener);
        }
    }

    public void createTodo(Message message){
        Todo todo = new Todo();
        todo.setContent(message.getContent());
        todo.setTime(message.getTime());
        todo.setPetitioner(message.getFullName());
        todo.setDone(false);

        todoRef.push().setValue(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Thêm Công Việc Thành Công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLongTouch(Message message) {
        createTodo(message);
    }
}
