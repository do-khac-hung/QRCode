package com.example.huuquang.qrcode.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Todo;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddTodoFragment extends Fragment {
    private EditText contentText;
    private EditText deadLineDateText;
    private EditText deadLineTimeText;
    private EditText petitionerText;
    private Button saveButton, cancelButton;

    private Calendar time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        time = Calendar.getInstance();
        time.set(Calendar.SECOND, 0);

        deadLineDateText = view.findViewById(R.id.todo_date);
        deadLineTimeText = view.findViewById(R.id.todo_time);
        contentText = view.findViewById(R.id.todo_content);
        petitionerText = view.findViewById(R.id.todo_petitioner);
        saveButton = view.findViewById(R.id.todo_save);
        cancelButton = view.findViewById(R.id.todo_cancel);

        deadLineDateText.setText(String.format("%02d", time.get(Calendar.DAY_OF_MONTH)) + "/"
                + String.format("%02d", (time.get(Calendar.MONTH)+1)) + "/"
                + time.get(Calendar.YEAR));
        deadLineTimeText.setText(String.format("%02d", time.get(Calendar.HOUR_OF_DAY)) + ":"
                + time.get(Calendar.MINUTE));

        deadLineDateText.setInputType(InputType.TYPE_NULL);
        deadLineDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        time.set(Calendar.YEAR, year);
                        time.set(Calendar.MONTH, month);
                        time.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        deadLineDateText.setText(String.format("%02d", dayOfMonth) + "/" +String.format("%02d", (month+1))  + "/" + year);
                    }
                }, time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        deadLineTimeText.setInputType(InputType.TYPE_NULL);
        deadLineTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        time.set(Calendar.MINUTE, minute);

                        deadLineTimeText.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                    }
                }, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeMili = time.getTimeInMillis();
                String content = contentText.getText().toString().trim();
                String petitioner = petitionerText.getText().toString().trim();

                Todo todo = new Todo();
                if(content.isEmpty() || petitioner.isEmpty()){
                    return;
                }else{
                    todo.setPetitioner(petitioner);
                    todo.setContent(content);
                    todo.setTime(timeMili);
                    todo.setDone(false);

                    FirebaseDatabase.getInstance().getReference("users").child(UserUtil.getCurrentUser().getUid())
                            .child("todos").push().setValue(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Thêm Công Việc Thành Công", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                    });
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
