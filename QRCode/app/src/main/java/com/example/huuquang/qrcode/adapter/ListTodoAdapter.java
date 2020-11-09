package com.example.huuquang.qrcode.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Todo;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ListTodoAdapter extends RecyclerView.Adapter<ListTodoAdapter.TodoViewHolder> {
    private List<Todo> todoList;
    private Context context;

    public ListTodoAdapter(Context context, List<Todo> todoList) {
        this.todoList = todoList;
        this.context = context;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_todo, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TodoViewHolder holder, int position) {
        final Todo todo = todoList.get(position);
        holder.mContentText.setText(todo.getContent());
        holder.mDeadLineDateText.setText(todo.toDateString());
        holder.mDeadLineTimeText.setText(todo.toTimeString());
        holder.mPetitioner.setText(todo.getPetitioner());

        final Calendar todoTime = new GregorianCalendar();
        todoTime.setTimeInMillis(todo.getTime());
        todoTime.set(Calendar.SECOND, 0);
        holder.mDeadLineDateText.setInputType(InputType.TYPE_NULL);
        holder.mDeadLineDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        todoTime.set(Calendar.YEAR, year);
                        todoTime.set(Calendar.MONTH, month);
                        todoTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        long miliTime = todoTime.getTimeInMillis();
                        FirebaseDatabase.getInstance().getReference("users").child(UserUtil.getCurrentUser().getUid())
                                .child("todos").child(todo.getId())
                                .child("time").setValue(miliTime);
//                        holder.mDeadLineDateText.setText(String.format("%02d", dayOfMonth) + "/" +String.format("%02d", (month+1))  + "/" + year);
                    }
                }, todoTime.get(Calendar.YEAR), todoTime.get(Calendar.MONTH), todoTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        holder.mDeadLineTimeText.setInputType(InputType.TYPE_NULL);
        holder.mDeadLineTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        todoTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        todoTime.set(Calendar.MINUTE, minute);
                        todoTime.set(Calendar.SECOND, 0);

                        long miliTime = todoTime.getTimeInMillis();
                        FirebaseDatabase.getInstance().getReference("users").child(UserUtil.getCurrentUser().getUid())
                                .child("todos").child(todo.getId())
                                .child("time").setValue(miliTime);
//                        holder.mDeadLineTimeText.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                    }
                }, todoTime.get(Calendar.HOUR_OF_DAY), todoTime.get(Calendar.MINUTE), true).show();
            }
        });
        holder.mDoneCheckbox.setChecked(todo.isDone());
        holder.mDoneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(todoList.contains(todo)) {
                    FirebaseDatabase.getInstance().getReference("users").child(UserUtil.getCurrentUser().getUid())
                            .child("todos").child(todo.getId())
                            .child("done").setValue(isChecked);
                }
            }
        });
        holder.mPetitioner.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_ACTION_NEXT) {
                    FirebaseDatabase.getInstance().getReference("users").child(UserUtil.getCurrentUser().getUid())
                            .child("todos").child(todo.getId())
                            .child("petitioner").setValue(holder.mPetitioner.getText().toString());
                    return true;
                }
                return false;
            }
        });
        holder.mContentText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    FirebaseDatabase.getInstance().getReference("users").child(UserUtil.getCurrentUser().getUid())
                            .child("todos").child(todo.getId())
                            .child("content").setValue(holder.mContentText.getText().toString());
                    return true;
                }
                return false;
            }
        });
        holder.mDeleteButton.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mDeadLineDateText.setOnClickListener(null);
                holder.mDeadLineTimeText.setOnClickListener(null);
                holder.mDoneCheckbox.setOnCheckedChangeListener(null);
                holder.mContentText.setOnEditorActionListener(null);
                holder.mPetitioner.setOnEditorActionListener(null);

                FirebaseDatabase.getInstance().getReference("users").child(UserUtil.getCurrentUser().getUid())
                        .child("todos").child(todo.getId()).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public List<Todo> getTodoList() {
        return todoList;
    }

    public void addItem(Todo todo){
        todoList.add(todo);
        notifyDataSetChanged();
    }

    public void clear(){
        todoList.clear();
        notifyDataSetChanged();
    }

    public void updateItem(Todo changedTodo){
        String id = changedTodo.getId();

        int index = -1;
        for(int i = 0; i < todoList.size(); i++){
            Todo todo = todoList.get(i);
            if(todo.getId().equals(id)){
                index = i;
                break;
            }
        }

        if(index!=-1) {
            todoList.set(index, changedTodo);
            notifyDataSetChanged();
        }
    }

    public int deleteItem(String id) {
        int index = -1;
        for(int i = 0; i < todoList.size(); i++){
            Todo todo = todoList.get(i);
            if(todo.getId().equals(id)){
                index = i;
                break;
            }
        }
        if(index!=-1) {
            todoList.remove(index);
            notifyDataSetChanged();
        }
        return index;
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder{
        public EditText mContentText;
        public EditText mDeadLineDateText;
        public EditText mDeadLineTimeText;
        public EditText mPetitioner;
        public CheckBox mDoneCheckbox;
        public ImageButton mDeleteButton;

        public TodoViewHolder(View itemView) {
            super(itemView);
            mContentText = itemView.findViewById(R.id.todo_item_content);
            mDeadLineDateText = itemView.findViewById(R.id.todo_item_deadline_date);
            mDeadLineTimeText = itemView.findViewById(R.id.todo_item_deadline_time);
            mPetitioner = itemView.findViewById(R.id.todo_item_petitioner);
            mDoneCheckbox = itemView.findViewById(R.id.todo_done_checkbox);
            mDeleteButton = itemView.findViewById(R.id.todo_delete_button);
        }
    }
}
