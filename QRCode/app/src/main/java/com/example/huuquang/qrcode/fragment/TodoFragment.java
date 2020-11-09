package com.example.huuquang.qrcode.fragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huuquang.qrcode.adapter.ListTodoAdapter;
import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.fragment.report.ReportDetailItemInfoFragment;
import com.example.huuquang.qrcode.model.Todo;
import com.example.huuquang.qrcode.service.AlarmReceiver;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class TodoFragment extends Fragment {

    private static HashMap<String, Integer> mapAlarm;

    private List<Todo> todoList;
    private RecyclerView mTodoRecycleView;
    private ListTodoAdapter mTodoAdapter;

    private DatabaseReference todoRef;
    private ChildEventListener todoListener;
    private FloatingActionButton addButton;

    public TodoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapAlarm = new HashMap<>();
        todoList = new ArrayList<>();
        mTodoAdapter = new ListTodoAdapter(getContext(), todoList);

        mTodoRecycleView = view.findViewById(R.id.todo_recycle_view);
        mTodoRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTodoRecycleView.setAdapter(mTodoAdapter);

        addButton = view.findViewById(R.id.todo_add);
        addButton.getDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddTodoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        todoRef = FirebaseDatabase.getInstance().getReference("users")
                .child(UserUtil.getCurrentUser().getUid()).child("todos");
        todoListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Todo todo = dataSnapshot.getValue(Todo.class);
                todo.setId(dataSnapshot.getKey());
                mTodoAdapter.addItem(todo);

                setAlarm(todo);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Todo changedTodo = dataSnapshot.getValue(Todo.class);
                changedTodo.setId(dataSnapshot.getKey());
                mTodoAdapter.updateItem(changedTodo);

                setAlarm(changedTodo);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = mTodoAdapter.deleteItem(key);

                unsetAlarm(index);
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
        todoRef.orderByChild("time").addChildEventListener(todoListener);
        mTodoAdapter.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        todoRef.removeEventListener(todoListener);
    }

    public void createTodo(String content, long time, String petitioner){
        Todo todo = new Todo();
        todo.setContent(content);
        todo.setTime(time);
        todo.setPetitioner(petitioner);
        todo.setDone(false);

        todoRef.push().setValue(todo);
    }

    private void setAlarm(Todo todo){
        int requestCode = mTodoAdapter.getTodoList().indexOf(todo);
        String content = todo.getContent();
        long time = todo.getTime();
        boolean tmp = PreferenceManager.getDefaultSharedPreferences(getContext()).contains(getString(R.string.pref_key_alarm_noti));
        if(tmp){
            String ahead = PreferenceManager.getDefaultSharedPreferences(getContext())
                    .getString(getString(R.string.pref_key_alarm_noti), "15");
            if(TextUtils.isDigitsOnly(ahead)){
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(time);
                calendar.add(Calendar.MINUTE, -Integer.parseInt(ahead));

                time = calendar.getTimeInMillis();
                content = content + " trong " + ahead + " phút sắp tới";
            }
        }

        if(time < System.currentTimeMillis()){
            return;
        }

        if(requestCode == -1){
            Log.d("TODOFRAGMENT", "setAlarm: " + requestCode);
            return;
        }

        AlarmManager a = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(this.getContext(), AlarmReceiver.class);
        intent.putExtra("content", content);

        PendingIntent p1=PendingIntent.getBroadcast(getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        a.set(AlarmManager.RTC_WAKEUP, time, p1);
    }

    private void unsetAlarm(int requestCode){
        AlarmManager a = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(this.getContext(), AlarmReceiver.class);

        if(requestCode == -1){
            Log.d("TODOFRAGMENT", "setAlarm: " + requestCode);
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent != null){
            a.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
