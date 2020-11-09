package com.example.huuquang.qrcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.huuquang.qrcode.model.User;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdditionSignUpInfoActivity extends AppCompatActivity {

    private AppCompatSpinner mSpinner;
    private Button mSaveButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_sign_up_info);

        final User user = UserUtil.getCurrentUser();

        mSpinner = findViewById(R.id.plant_spinner);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.list_item_spinner, R.id.spinner_item_text);
        FirebaseDatabase.getInstance().getReference("companies").child(user.getCompany_id()).child("plants")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            spinnerAdapter.add(childSnapshot.getKey());
                        }
                        mSpinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        mSaveButton = findViewById(R.id.btn_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                user.setPlant_id((String)mSpinner.getSelectedItem());
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(user.toMap())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference("companies").child(user.getCompany_id())
                                        .child("plants").child(user.getPlant_id())
                                        .child("users").push().setValue(user.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AdditionSignUpInfoActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AdditionSignUpInfoActivity.this, MainActivity.class);

                                        showProgress(false);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
            }
        });

        mProgressBar = findViewById(R.id.progress);
    }

    private void showProgress(boolean isShow){
        if(isShow) {
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
