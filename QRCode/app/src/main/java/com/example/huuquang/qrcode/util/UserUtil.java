package com.example.huuquang.qrcode.util;

import com.example.huuquang.qrcode.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserUtil {
    private static User currentUser;

    private UserUtil() { }

    public static void init(User user){
        if(currentUser == null) currentUser = new User();
        currentUser.setUid(user.getUid());
        currentUser.setEmail(user.getEmail());
        currentUser.setFullname(user.getFullname());
        currentUser.setCompany_id(user.getCompany_id());
        currentUser.setPlant_id(user.getPlant_id());
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Task<AuthResult> signInAnonymous(FirebaseAuth auth){
        return auth.signInWithEmailAndPassword("anonymous@mail.com", "123456");
    }
}
