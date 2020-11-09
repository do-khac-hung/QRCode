package com.example.huuquang.qrcode.fragment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huuquang.qrcode.service.ConnectionReceiver;
import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.ItemLocation;
import com.example.huuquang.qrcode.model.Transaction;
import com.example.huuquang.qrcode.model.User;
import com.example.huuquang.qrcode.util.FragmentUtil;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.math.BigDecimal;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class OutcomeFragment extends Fragment
        implements View.OnClickListener {
    private static final int NO_SCAN = 0;
    private static final int ID_SCAN = 1;
    private static final int LOCATE_SCAN = 2;
    private static final int DESCRIPTION_SCAN = 3;
    private static final int PL_SCAN = 4;
    private int scanType = 0;

    // UI Component
    EditText et_id, et_locate, et_description, et_pl;
    Button btn_scan_id, btn_scan_locate, btn_scan_description, btn_scan_pl, btn_save;
    TextView tv_username;
    DecoratedBarcodeView barcodeScannerView;

    FirebaseAuth mAuth;
    DatabaseReference rootRef;

    public OutcomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outcome, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();

        et_id = (EditText) view.findViewById(R.id.outcome_id_item);
        et_locate = (EditText) view.findViewById(R.id.outcome_locate_item);
        et_description = (EditText) view.findViewById(R.id.outcome_description_item);
        et_pl = (EditText) view.findViewById(R.id.outcome_pl_item);

        tv_username = (TextView) view.findViewById(R.id.username);
        tv_username.setText(UserUtil.getCurrentUser().getFullname());

        btn_scan_id = (Button) view.findViewById(R.id.outcome_scan_id);
        btn_scan_id.setOnClickListener(this);

        btn_scan_locate = (Button) view.findViewById(R.id.outcome_scan_locate);
        btn_scan_locate.setOnClickListener(this);

        btn_scan_description = (Button) view.findViewById(R.id.outcome_scan_desc);
        btn_scan_description.setOnClickListener(this);

        btn_scan_pl = (Button) view.findViewById(R.id.outcome_scan_pl);
        btn_scan_pl.setOnClickListener(this);

        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        barcodeScannerView = (DecoratedBarcodeView) view.findViewById(R.id.barcode_view);
        barcodeScannerView.getStatusView().setText("");
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if(result.getText() == null){
                    return;
                }
                switch (scanType){
                    case ID_SCAN:
                        processID(result.getText());

                        et_id.setHint("");
                        et_id.setEnabled(true);

                        btn_scan_id.setText(getString(R.string.scan_label));
                        btn_scan_locate.setEnabled(true);
                        btn_scan_description.setEnabled(true);
                        btn_scan_pl.setEnabled(true);
                        btn_save.setEnabled(true);

                        et_pl.setEnabled(true);
                        et_description.setEnabled(true);
                        et_locate.setEnabled(true);

                        barcodeScannerView.getStatusView().setText("");

                        scanType = NO_SCAN;
                        break;
                    case LOCATE_SCAN:
                        processLocate(result.getText());

                        et_locate.setHint("");
                        et_locate.setEnabled(true);

                        btn_scan_locate.setText(getString(R.string.scan_label));
                        btn_scan_id.setEnabled(true);
                        btn_scan_pl.setEnabled(true);
                        btn_scan_description.setEnabled(true);
                        btn_save.setEnabled(true);

                        et_pl.setEnabled(true);
                        et_description.setEnabled(true);
                        et_id.setEnabled(true);

                        barcodeScannerView.getStatusView().setText("");

                        scanType = NO_SCAN;
                        break;
                    case DESCRIPTION_SCAN:
                        processQuantity(result.getText());

                        et_description.setHint("");
                        et_description.setEnabled(true);

                        btn_scan_description.setText(getString(R.string.scan_label));
                        btn_scan_id.setEnabled(true);
                        btn_scan_pl.setEnabled(true);
                        btn_scan_locate.setEnabled(true);
                        btn_save.setEnabled(true);

                        et_pl.setEnabled(true);
                        et_id.setEnabled(true);
                        et_locate.setEnabled(true);

                        barcodeScannerView.getStatusView().setText("");

                        scanType = NO_SCAN;
                        break;
                    case PL_SCAN:
                        processPL(result.getText());

                        et_pl.setHint("");
                        et_pl.setEnabled(true);

                        btn_scan_pl.setText(getString(R.string.scan_label));
                        btn_scan_id.setEnabled(true);
                        btn_scan_locate.setEnabled(true);
                        btn_scan_description.setEnabled(true);
                        btn_save.setEnabled(true);

                        et_id.setEnabled(true);
                        et_description.setEnabled(true);
                        et_locate.setEnabled(true);

                        barcodeScannerView.getStatusView().setText("");

                        scanType = NO_SCAN;
                        break;
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) { }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(barcodeScannerView != null) {
            if (isVisibleToUser) {
                showBarcodeView(true);
            } else {
                showBarcodeView(false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        showBarcodeView(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        showBarcodeView(true);
    }

    private void showBarcodeView(boolean show){
        if(show){
            barcodeScannerView.resume();
            barcodeScannerView.setVisibility(View.VISIBLE);
        }else{
            barcodeScannerView.pauseAndWait();
            barcodeScannerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.outcome_scan_id:
                if(((TextView)v).getText().toString().equals(getString(R.string.scan_cancel_label))){
                    et_id.setEnabled(true);
                    et_id.setHint("");

                    et_pl.setEnabled(true);
                    et_description.setEnabled(true);
                    et_locate.setEnabled(true);

                    btn_scan_id.setText(getString(R.string.scan_label));
                    btn_scan_locate.setEnabled(true);
                    btn_scan_description.setEnabled(true);
                    btn_scan_pl.setEnabled(true);
                    btn_save.setEnabled(true);

                    scanType = NO_SCAN;
                    barcodeScannerView.getStatusView().setText("");
                    break;
                }

                et_id.setHint(getString(R.string.scan_hint).toUpperCase());
                et_id.setEnabled(false);

                et_pl.setEnabled(false);
                et_description.setEnabled(false);
                et_locate.setEnabled(false);

                btn_scan_id.setText(getString(R.string.scan_cancel_label));
                btn_scan_locate.setEnabled(false);
                btn_scan_description.setEnabled(false);
                btn_scan_pl.setEnabled(false);
                btn_save.setEnabled(false);

                scanType = ID_SCAN;
                barcodeScannerView.getStatusView().setText(R.string.scan_id_label);
                break;
            case R.id.outcome_scan_locate:
                if(((TextView)v).getText().toString().equals(getString(R.string.scan_cancel_label))){
                    et_locate.setEnabled(true);
                    et_locate.setHint("");

                    et_pl.setEnabled(true);
                    et_description.setEnabled(true);
                    et_id.setEnabled(true);

                    btn_scan_locate.setText(getString(R.string.scan_label));
                    btn_scan_id.setEnabled(true);
                    btn_scan_description.setEnabled(true);
                    btn_scan_pl.setEnabled(true);
                    btn_save.setEnabled(true);

                    scanType = NO_SCAN;
                    barcodeScannerView.getStatusView().setText("");
                    break;
                }

                et_locate.setHint(getString(R.string.scan_hint).toUpperCase());
                et_locate.setEnabled(false);

                et_pl.setEnabled(false);
                et_description.setEnabled(false);
                et_id.setEnabled(false);

                btn_scan_locate.setText(getString(R.string.scan_cancel_label));
                btn_scan_id.setEnabled(false);
                btn_scan_description.setEnabled(false);
                btn_scan_pl.setEnabled(false);
                btn_save.setEnabled(false);

                scanType = LOCATE_SCAN;
                barcodeScannerView.getStatusView().setText(getString(R.string.scan_location_label));
                break;
            case R.id.outcome_scan_desc:
                if(((TextView)v).getText().toString().equals(getString(R.string.scan_cancel_label))){
                    et_description.setEnabled(true);
                    et_description.setHint("");

                    et_pl.setEnabled(true);
                    et_id.setEnabled(true);
                    et_locate.setEnabled(true);

                    btn_scan_description.setText(getString(R.string.scan_label));
                    btn_scan_id.setEnabled(true);
                    btn_scan_locate.setEnabled(true);
                    btn_scan_pl.setEnabled(true);
                    btn_save.setEnabled(true);

                    scanType = NO_SCAN;
                    barcodeScannerView.getStatusView().setText("");
                    break;
                }

                et_description.setHint(getString(R.string.scan_hint).toUpperCase());
                et_description.setEnabled(false);

                et_pl.setEnabled(false);
                et_id.setEnabled(false);
                et_locate.setEnabled(false);

                btn_scan_description.setText(getString(R.string.scan_cancel_label));
                btn_scan_id.setEnabled(false);
                btn_scan_locate.setEnabled(false);
                btn_scan_pl.setEnabled(false);
                btn_save.setEnabled(false);

                scanType = DESCRIPTION_SCAN;
                barcodeScannerView.getStatusView().setText(getString(R.string.scan_description_label));
                break;
            case R.id.outcome_scan_pl:
                if(((TextView)v).getText().toString().equals(getString(R.string.scan_cancel_label))){
                    et_pl.setEnabled(true);
                    et_pl.setHint("");

                    et_id.setEnabled(true);
                    et_description.setEnabled(true);
                    et_locate.setEnabled(true);

                    btn_scan_pl.setText(getString(R.string.scan_label));
                    btn_scan_id.setEnabled(true);
                    btn_scan_locate.setEnabled(true);
                    btn_scan_description.setEnabled(true);
                    btn_save.setEnabled(true);

                    scanType = NO_SCAN;
                    barcodeScannerView.getStatusView().setText("");
                    break;
                }

                et_pl.setHint(getString(R.string.scan_hint).toUpperCase());
                et_pl.setEnabled(false);

                et_id.setEnabled(false);
                et_description.setEnabled(false);
                et_locate.setEnabled(false);

                btn_scan_pl.setText(getString(R.string.scan_cancel_label));
                btn_scan_id.setEnabled(false);
                btn_scan_locate.setEnabled(false);
                btn_scan_description.setEnabled(false);
                btn_save.setEnabled(false);

                scanType = PL_SCAN;
                barcodeScannerView.getStatusView().setText(getString(R.string.scan_pl_label));
                break;
            case R.id.btn_save:
                showProgress();
                if(!checkValid()){
                    break;
                }
                makeUpdate();
                break;
        }
    }

    private boolean checkValid(){
        boolean valid = true;

        String id = et_id.getText().toString();
        if(TextUtils.isEmpty(id)) {
            et_id.setError("Empty");
            valid = false;
        }

        String locate = et_locate.getText().toString();
        if(TextUtils.isEmpty(locate)) {
            et_locate.setError("Empty");
            valid = false;
        }

        String description = et_description.getText().toString();
        if(TextUtils.isEmpty(description)) {
            et_description.setError("Empty");
            valid = false;
        }

        String pl = et_pl.getText().toString();
        if(TextUtils.isEmpty(pl)) {
            et_pl.setError("Empty");
            valid = false;
        }else {
            try {
                Float.parseFloat(description);
            } catch (Exception e) {
                et_description.setError("Number only");
                valid = false;
            }
        }

        return valid;
    }

    private void makeUpdate(){
        User currentUser = UserUtil.getCurrentUser();

        String itemId = et_id.getText().toString();
        if(ConnectionReceiver.isConnected(getContext()) && !checkPreferenceFreeId()){
            rootRef.child("companies").child(currentUser.getCompany_id())
                    .child("items").orderByKey().equalTo(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildren().iterator().hasNext()){
                        updateQuantity();
                    };
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            updateQuantity();
        }
    }

    private boolean checkPreferenceFreeId(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getBoolean(getString(R.string.pref_key_free_id), false);
    }

    private void makeOutcomeTransaction(){
        User currentUser = UserUtil.getCurrentUser();

        final String itemId = et_id.getText().toString();
        final String locate = et_locate.getText().toString();
        final String description = et_description.getText().toString();
        String pl = et_pl.getText().toString();

        et_description.getText().clear();
        et_id.getText().clear();
        et_locate.getText().clear();

        Transaction outTransaction = new Transaction();
        outTransaction.setStatus(Transaction.OUTCOME);
        outTransaction.setItem_id(itemId);
        outTransaction.setDescription(description);
        outTransaction.setLocate_id(locate);
        outTransaction.setUser_id(currentUser.getUid());
        outTransaction.setPl(pl);

        rootRef.child("companies").child(currentUser.getCompany_id())
                .child("plants").child(currentUser.getPlant_id())
                .child("transactions").push().setValue(outTransaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OutcomeFragment.this.getContext(), "Saved", Toast.LENGTH_LONG).show();
                        hideProgress();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress();
                    }
                });
    }

    private void updateQuantity(){
        User currentUser = UserUtil.getCurrentUser();

        final String itemId = et_id.getText().toString();
        final String locationId = et_locate.getText().toString();
        final String quantity = et_description.getText().toString();

        //check if exist
        rootRef.child("companies").child(currentUser.getCompany_id())
                .child("plants").child(currentUser.getPlant_id())
                .child("item_location").orderByChild("location_id").equalTo(locationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean flag = false;
                        if(dataSnapshot.getChildren().iterator().hasNext()){
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                //String key = child.getKey();
                                ItemLocation data = child.getValue(ItemLocation.class);
                                if(data.getItem_id().equals(itemId)){
                                    flag = true;
                                    //check if minus
                                    BigDecimal newQuantity = new BigDecimal(data.getQuantity()).subtract(new BigDecimal(quantity));
                                    BigDecimal zero = new BigDecimal("0");
                                    //if not update quantity
                                    switch (newQuantity.compareTo(zero)){
                                        case 0: //equal
                                            child.getRef().setValue(null);
                                            makeOutcomeTransaction();
                                            break;
                                        case 1:
                                            data.setQuantity(newQuantity.toString());
                                            child.getRef().setValue(data);
                                            makeOutcomeTransaction();
                                            break;
                                        case -1:
                                            onCancelled(DatabaseError.zzbc("-999", "Âm số lượng"));
                                            break;
                                    }
                                    break;
                                }
                            }
                        }
                        if(!flag){
                            onCancelled(DatabaseError.zzbc("-999", "Lỗi Nhập Xuất Vật Tư"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(OutcomeFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        hideProgress();
                    }
                });
    }

    private void processID(String scanText){
        String tmp[] = scanText.split(":");
        if(tmp[0].equals("id")){
            et_id.setText(tmp[1]);
        }
    }

    private void processQuantity(String scanText){
        String tmp[] = scanText.split(":");
        if(tmp[0].equals("quantity")){
            et_description.setText(tmp[1]);
        }
    }

    private void processLocate(String scanText){
        String tmp[] = scanText.split(":");
        if(tmp[0].equals("locate")){
            et_locate.setText(tmp[1]);
        }
    }

    private void processPL(String scanText){
        String tmp[] = scanText.split(":");
        if(tmp[0].equals("pl")){
            et_pl.setText(tmp[1]);
        }
    }

    private void showProgress(){
        getActivity().findViewById(R.id.main_progress).setVisibility(View.VISIBLE);
        FragmentUtil.setViewAndChildrenEnabled(this.getActivity().findViewById(android.R.id.content), false);
    }

    private void hideProgress(){
        getActivity().findViewById(R.id.main_progress).setVisibility(View.GONE);
        FragmentUtil.setViewAndChildrenEnabled(this.getActivity().findViewById(android.R.id.content), true);
    }
}
