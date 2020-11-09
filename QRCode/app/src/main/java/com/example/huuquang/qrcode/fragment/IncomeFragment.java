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
public class IncomeFragment extends Fragment
        implements View.OnClickListener {
    public static final int NO_SCAN = 0;
    private static final int ID_SCAN = 1;
    private static final int LOCATE_SCAN = 2;
    private static final int DESCRIPTION_SCAN = 3;
    private static final int PO_SCAN = 4;
    private int scanType = 0;

    // UI Component
    EditText et_id, et_locate, et_description, et_po;
    Button btn_scan_id, btn_scan_locate, btn_scan_description, btn_scan_po, btn_save;
    TextView tv_username;
    DecoratedBarcodeView barcodeScannerView;

    FirebaseAuth mAuth;
    DatabaseReference rootRef;

    public IncomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_income, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();

        et_id = (EditText) view.findViewById(R.id.income_id_item);
        et_locate = (EditText) view.findViewById(R.id.income_locate_item);
        et_description = (EditText) view.findViewById(R.id.income_description_item);
        et_po = (EditText) view.findViewById(R.id.income_po_item);

        tv_username = (TextView) view.findViewById(R.id.username);
        tv_username.setText(UserUtil.getCurrentUser().getFullname());

        btn_scan_id = (Button) view.findViewById(R.id.income_scan_id);
        btn_scan_id.setOnClickListener(this);

        btn_scan_locate = (Button) view.findViewById(R.id.income_scan_locate);
        btn_scan_locate.setOnClickListener(this);

        btn_scan_description = (Button) view.findViewById(R.id.income_scan_desc);
        btn_scan_description.setOnClickListener(this);

        btn_scan_po = (Button) view.findViewById(R.id.income_scan_po);
        btn_scan_po.setOnClickListener(this);

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
                        btn_scan_po.setEnabled(true);
                        btn_save.setEnabled(true);

                        et_po.setEnabled(true);
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
                        btn_scan_po.setEnabled(true);
                        btn_scan_description.setEnabled(true);
                        btn_save.setEnabled(true);

                        et_po.setEnabled(true);
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
                        btn_scan_po.setEnabled(true);
                        btn_scan_locate.setEnabled(true);
                        btn_save.setEnabled(true);

                        et_po.setEnabled(true);
                        et_id.setEnabled(true);
                        et_locate.setEnabled(true);

                        barcodeScannerView.getStatusView().setText("");

                        scanType = NO_SCAN;
                        break;
                    case PO_SCAN:
                        processPO(result.getText());

                        et_po.setHint("");
                        et_po.setEnabled(true);

                        btn_scan_po.setText(getString(R.string.scan_label));
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
            case R.id.income_scan_id:
                if(((TextView)v).getText().toString().equals(getString(R.string.scan_cancel_label))){
                    et_id.setEnabled(true);
                    et_id.setHint("");

                    et_po.setEnabled(true);
                    et_description.setEnabled(true);
                    et_locate.setEnabled(true);

                    btn_scan_id.setText(getString(R.string.scan_label));
                    btn_scan_locate.setEnabled(true);
                    btn_scan_description.setEnabled(true);
                    btn_scan_po.setEnabled(true);
                    btn_save.setEnabled(true);

                    scanType = NO_SCAN;
                    barcodeScannerView.getStatusView().setText("");
                    break;
                }

                et_id.setHint(getString(R.string.scan_hint).toUpperCase());
                et_id.setEnabled(false);

                et_po.setEnabled(false);
                et_description.setEnabled(false);
                et_locate.setEnabled(false);

                btn_scan_id.setText(getString(R.string.scan_cancel_label));
                btn_scan_locate.setEnabled(false);
                btn_scan_description.setEnabled(false);
                btn_scan_po.setEnabled(false);
                btn_save.setEnabled(false);

                scanType = ID_SCAN;
                barcodeScannerView.getStatusView().setText(R.string.scan_id_label);
                break;
            case R.id.income_scan_locate:
                if(((TextView)v).getText().toString().equals(getString(R.string.scan_cancel_label))){
                    et_locate.setEnabled(true);
                    et_locate.setHint("");

                    et_po.setEnabled(true);
                    et_description.setEnabled(true);
                    et_id.setEnabled(true);

                    btn_scan_locate.setText(getString(R.string.scan_label));
                    btn_scan_id.setEnabled(true);
                    btn_scan_description.setEnabled(true);
                    btn_scan_po.setEnabled(true);
                    btn_save.setEnabled(true);

                    scanType = NO_SCAN;
                    barcodeScannerView.getStatusView().setText("");
                    break;
                }

                et_locate.setHint(getString(R.string.scan_hint).toUpperCase());
                et_locate.setEnabled(false);

                et_po.setEnabled(false);
                et_description.setEnabled(false);
                et_id.setEnabled(false);

                btn_scan_locate.setText(getString(R.string.scan_cancel_label));
                btn_scan_id.setEnabled(false);
                btn_scan_description.setEnabled(false);
                btn_scan_po.setEnabled(false);
                btn_save.setEnabled(false);

                scanType = LOCATE_SCAN;
                barcodeScannerView.getStatusView().setText(getString(R.string.scan_location_label));
                break;
            case R.id.income_scan_desc:
                if(((TextView)v).getText().toString().equals(getString(R.string.scan_cancel_label))){
                    et_description.setEnabled(true);
                    et_description.setHint("");

                    et_po.setEnabled(true);
                    et_id.setEnabled(true);
                    et_locate.setEnabled(true);

                    btn_scan_description.setText(getString(R.string.scan_label));
                    btn_scan_id.setEnabled(true);
                    btn_scan_locate.setEnabled(true);
                    btn_scan_po.setEnabled(true);
                    btn_save.setEnabled(true);

                    scanType = NO_SCAN;
                    barcodeScannerView.getStatusView().setText("");
                    break;
                }

                et_description.setHint(getString(R.string.scan_hint).toUpperCase());
                et_description.setEnabled(false);

                et_po.setEnabled(false);
                et_id.setEnabled(false);
                et_locate.setEnabled(false);

                btn_scan_description.setText(getString(R.string.scan_cancel_label));
                btn_scan_id.setEnabled(false);
                btn_scan_locate.setEnabled(false);
                btn_scan_po.setEnabled(false);
                btn_save.setEnabled(false);

                scanType = DESCRIPTION_SCAN;
                barcodeScannerView.getStatusView().setText(getString(R.string.scan_description_label));
                break;
            case R.id.income_scan_po:
                if(((TextView)v).getText().toString().equals(getString(R.string.scan_cancel_label))){
                    et_po.setEnabled(true);
                    et_po.setHint("");

                    et_id.setEnabled(true);
                    et_description.setEnabled(true);
                    et_locate.setEnabled(true);

                    btn_scan_po.setText(getString(R.string.scan_label));
                    btn_scan_id.setEnabled(true);
                    btn_scan_locate.setEnabled(true);
                    btn_scan_description.setEnabled(true);
                    btn_save.setEnabled(true);

                    scanType = NO_SCAN;
                    barcodeScannerView.getStatusView().setText("");
                    break;
                }

                et_po.setHint(getString(R.string.scan_hint).toUpperCase());
                et_po.setEnabled(false);

                et_id.setEnabled(false);
                et_description.setEnabled(false);
                et_locate.setEnabled(false);

                btn_scan_po.setText(getString(R.string.scan_cancel_label));
                btn_scan_id.setEnabled(false);
                btn_scan_locate.setEnabled(false);
                btn_scan_description.setEnabled(false);
                btn_save.setEnabled(false);

                scanType = PO_SCAN;
                barcodeScannerView.getStatusView().setText(getString(R.string.scan_po_label));
                break;
            case R.id.btn_save:
                showProgress();
                if(!checkValid()){
                    hideProgress();
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
        }else {
            try {
                Float.parseFloat(description);
            } catch (Exception e) {
                et_description.setError("Number only");
                valid = false;
            }
        }

        String po = et_po.getText().toString();
        if(TextUtils.isEmpty(description)) {
            et_po.setError("Empty");
            valid = false;
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
                    }else{
                        Toast.makeText(getContext(),"ID item invalid", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    };
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    hideProgress();
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

    private void makeIncomeTransaction(){
        User currentUser = UserUtil.getCurrentUser();

        final String itemId = et_id.getText().toString();
        final String locate = et_locate.getText().toString();
        final String description = et_description.getText().toString();
        String po = et_po.getText().toString();

        et_description.getText().clear();
        et_id.getText().clear();
        et_locate.getText().clear();

        Transaction inTransaction = new Transaction();
        inTransaction.setStatus(Transaction.INCOME);
        inTransaction.setItem_id(itemId);
        inTransaction.setDescription(description);
        inTransaction.setLocate_id(locate);
        inTransaction.setUser_id(currentUser.getUid());
        inTransaction.setPo(po);

        rootRef.child("companies").child(currentUser.getCompany_id())
                .child("plants").child(currentUser.getPlant_id())
                .child("transactions").push().setValue(inTransaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(IncomeFragment.this.getContext(), "Saved", Toast.LENGTH_LONG).show();
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
                                    BigDecimal newQuantity = new BigDecimal(quantity).add(new BigDecimal(data.getQuantity()));
                                    data.setQuantity(newQuantity.toString());
                                    child.getRef().setValue(data);
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if(!flag){
                            BigDecimal quantityBigDecimal = new BigDecimal(quantity);

                            ItemLocation data = new ItemLocation();
                            data.setItem_id(itemId);
                            data.setLocation_id(locationId);
                            data.setItem_id(itemId);
                            data.setQuantity(quantityBigDecimal.toString());
                            dataSnapshot.getRef().push().setValue(data);
                        }

                        makeIncomeTransaction();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
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

    private void processPO(String scanText){
        String tmp[] = scanText.split(":");
        if(tmp[0].equals("po")){
            et_po.setText(tmp[1]);
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
