package com.example.huuquang.qrcode.fragment.report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.adapter.ReportItemGroupAdapter;
import com.example.huuquang.qrcode.adapter.ReportWorkerAdapter;
import com.example.huuquang.qrcode.model.ItemLocation;
import com.example.huuquang.qrcode.model.User;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportDetailWorkerAllFragment extends Fragment {
    TextView mTitleTextView;
    EditText mSearchEditText;
    Button mSearchButton;
    RecyclerView mDetailRecycleView;

    List<User> userList;
    List<User> userSearchList;
    ReportWorkerAdapter adapter;

    DatabaseReference userPlantRef;
    DatabaseReference userRef;
    ChildEventListener userPlantListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_detail_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleTextView = view.findViewById(R.id.report_title);
        mTitleTextView.setText("Nhân Viên - Tất Cả");

        mDetailRecycleView = view.findViewById(R.id.report_detail_recycle_view);
        mDetailRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        userSearchList = new ArrayList<>();
        adapter = new ReportWorkerAdapter(userList);
        mDetailRecycleView.setAdapter(adapter);

        mSearchEditText = view.findViewById(R.id.report_search_edit_text);
        mSearchEditText.setHint("Tên Nhân Viên Cần Tìm");
        mSearchButton = view.findViewById(R.id.report_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywordText = mSearchEditText.getText().toString().trim();
                if(!keywordText.isEmpty()){
                    userSearchList.clear();
                    for(User item : userList){
                        if(item.getFullname().contains(keywordText)){
                            userSearchList.add(item);
                        }
                    }

                    if(userSearchList.isEmpty()){
                        Toast.makeText(getContext(), "Không Tồn Tại", Toast.LENGTH_SHORT).show();
                    }else{
                        adapter.setList(userSearchList);
                    }
                }else{
                    adapter.setList(userList);
                }
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference("users");
        userPlantRef = FirebaseDatabase.getInstance().getReference("companies").child(UserUtil.getCurrentUser().getCompany_id())
                .child("plants").child(UserUtil.getCurrentUser().getPlant_id())
                .child("users");
        userPlantListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String uid = (String)dataSnapshot.getValue();
                userRef.orderByKey().equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren() && dataSnapshot.getChildrenCount() == 1){
                            for(DataSnapshot childSnap : dataSnapshot.getChildren()){
                                User user = childSnap.getValue(User.class);
                                user.setUid(childSnap.getKey());
                                adapter.addItem(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
    public void onResume() {
        super.onResume();
        userPlantRef.addChildEventListener(userPlantListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.clear();
        userPlantRef.removeEventListener(userPlantListener);
    }
}
