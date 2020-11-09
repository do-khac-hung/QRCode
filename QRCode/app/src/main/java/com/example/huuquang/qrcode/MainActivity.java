package com.example.huuquang.qrcode;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.example.huuquang.qrcode.fragment.AddTodoFragment;
import com.example.huuquang.qrcode.fragment.ChatFragment;
import com.example.huuquang.qrcode.fragment.IncomeFragment;
import com.example.huuquang.qrcode.fragment.OutcomeFragment;
import com.example.huuquang.qrcode.fragment.ReportFragment;
import com.example.huuquang.qrcode.fragment.SettingsFragment;
import com.example.huuquang.qrcode.fragment.TodoFragment;
import com.example.huuquang.qrcode.fragment.UpperFragment;
import com.example.huuquang.qrcode.model.User;
import com.example.huuquang.qrcode.service.ConnectionReceiver;
import com.example.huuquang.qrcode.util.FragmentUtil;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements UpperFragment.UpperFragmentCallback,
        ConnectionReceiver.ConnectionReceiverListener{

    private static int cameraPermissionReqCode = 250;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean askedPermission = false;
    private List<Fragment> fragmentsNav;
    private ConnectionReceiver connectionReceiver;
    private IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    private UpperFragment upperFragment;
    private AHBottomNavigation bottomNavigationView;
    private AHBottomNavigationAdapter navAdapter;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForUpdateInfo();

        connectionReceiver = new ConnectionReceiver();

        upperFragment = new UpperFragment();
        Log.d("12312312312", upperFragment.isInLayout() + "");

        fragmentsNav = new ArrayList<>();
        fragmentsNav.add(FragmentUtil.INCOME_FRAGMENT, new IncomeFragment());
        fragmentsNav.add(FragmentUtil.OUTCOME_FRAGMENT, new OutcomeFragment());
        fragmentsNav.add(FragmentUtil.REPORT_FRAGMENT, new ReportFragment());
        fragmentsNav.add(FragmentUtil.TODO_FRAGMENT, new TodoFragment());
        fragmentsNav.add(FragmentUtil.SETTING_FRAGMENT, new SettingsFragment());
        fragmentsNav.add(FragmentUtil.CHAT_FRAGMENT, new ChatFragment());

        alertDialog = buildAlertDialog();

        bottomNavigationView = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        navAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_main);
        navAdapter.setupWithBottomNavigation(bottomNavigationView);

        bottomNavigationView.setAccentColor(Color.parseColor("#ffffff"));
        bottomNavigationView.setDefaultBackgroundColor(Color.parseColor("#000000"));
        bottomNavigationView.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigationView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Fragment selectedFragment = fragmentsNav.get(position);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        switchFragment(0);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        openCameraWithPermission();

        //hide keyboard on start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);

        //register network broadcast
        this.registerReceiver(connectionReceiver, filter);
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        this.unregisterReceiver(connectionReceiver);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

        for(Fragment mainFragment : fragmentsNav){
            Class mainFragClass = mainFragment.getClass();
            if(mainFragClass.isInstance(currentFragment)){
                MainActivity.super.onBackPressed();
                return;
            }
        }

        if(currentFragment.getClass() == AddTodoFragment.class){
            switchFragment(FragmentUtil.TODO_FRAGMENT);
        }else{
            switchFragment(FragmentUtil.REPORT_FRAGMENT);
        }
    }

    private void switchFragment(int position){
        switchFragment(fragmentsNav.get(position));
    }

    private void switchFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    public void hideProgress(){
        findViewById(R.id.main_progress).setVisibility(View.GONE);
    }

    @Override
    public void onChatButtonClick() {
        bottomNavigationView.setCurrentItem(AHBottomNavigation.CURRENT_ITEM_NONE, false);
        switchFragment(FragmentUtil.CHAT_FRAGMENT);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            Log.d(this.getClass().getSimpleName(), "onNetworkConnectionChanged: " + isConnected);
            if(alertDialog.isShowing()){
                alertDialog.hide();
            }
        }else{
            Log.d(this.getClass().getSimpleName(), "onNetworkConnectionChanged: " + isConnected);
            if(!checkPrefInternet()){
                alertDialog.show();
            }
        }
    }

    private boolean checkPrefInternet(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(getString(R.string.pref_key_internet), false);
    }

    private AlertDialog buildAlertDialog(){
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         return builder.setTitle("Connection Lost")
                .setMessage("Your Connection Lost")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
    }

    private void checkForUpdateInfo(){
        User user = UserUtil.getCurrentUser();
        if(user.getCompany_id() == null || user.getPlant_id() == null){
            Intent intent = new Intent(this, AdditionSignUpInfoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @TargetApi(23)
    private void openCameraWithPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

        } else if(!askedPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    cameraPermissionReqCode);
            askedPermission = true;
        } else {
            // Wait for permission result
        }
    }

    /**
     * Call from Activity#onRequestPermissionsResult
     * @param requestCode The request code passed in {@link android.support.v4.app.ActivityCompat#requestPermissions(Activity, String[], int)}.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == cameraPermissionReqCode) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was not granted
                //TODO show error dialog
            }
        }
    }
}
