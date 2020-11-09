package com.example.huuquang.qrcode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huuquang.qrcode.model.Company;
import com.example.huuquang.qrcode.model.User;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText et_email, et_password, et_fullname;
    private View mProgressView;
    private View mFormView;

    private FirebaseAuth mAuth;
    private DatabaseReference mUsersReference;
    private DatabaseReference mCompanyReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mProgressView = findViewById(R.id.progress);
        mFormView = findViewById(R.id.signup_form);

        et_fullname = (EditText) findViewById(R.id.fullname);
        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        Button submitBtn = findViewById(R.id.signup_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        Button cancelBtn = findViewById(R.id.signup_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUsersReference = FirebaseDatabase.getInstance().getReference("users");
        mCompanyReference = FirebaseDatabase.getInstance().getReference("companies");
    }

    private void attemptSignUp(){
        if (!validateForm()) {
            return;
        }
        showProgress(true);
        if(isBanned()){
            return;
        }

        String email = et_email.getText().toString();
        String[] splits = email.split("@");
        final String domain = splits[1];

        //check domain
        UserUtil.signInAnonymous(mAuth)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mCompanyReference.orderByChild("domain").equalTo(domain)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChildren()){
                                            Log.d("TAG", dataSnapshot.toString());
                                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                                String companyId = childSnapshot.getKey();
                                                register(companyId);
                                            }

                                        }else{
                                            Toast.makeText(SignUpActivity.this, "Công ty chưa hỗ trợ", Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }
                                        mAuth.signOut();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(SignUpActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                        mAuth.signOut();
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void register(final String companyId) {
        mAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            onAuthSuccess(user, companyId);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        showProgress(false);
                    }
                });
    }

    private boolean isBanned() {
        return false;
    }

    private void onAuthSuccess(FirebaseUser user, String companyId) {
        String fullname = et_fullname.getText().toString();
        writeNewUser(user.getEmail(), fullname, user.getUid(), companyId);
    }

    private void writeNewUser(String email, String fullname, final String uid, String companyId) {
        User newUser = new User();
        newUser.setFullname(fullname);
        newUser.setEmail(email);
        newUser.setCompany_id(companyId);

        UserUtil.init(newUser);

        mUsersReference.child(uid).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                UserUtil.getCurrentUser().setUid(uid);
                Toast.makeText(SignUpActivity.this, "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        // Reset errors.
        et_email.setError(null);
        et_password.setError(null);

        String email = et_email.getText().toString();
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            et_email.setError(getString(R.string.error_field_required));
            valid = false;
        } else if (!isEmailValid(email)) {
            et_email.setError(getString(R.string.error_invalid_email));
            valid = false;
        }

        String password = et_password.getText().toString();
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            et_password.setError(getString(R.string.error_invalid_password));
            valid = false;
        }

        return valid;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
