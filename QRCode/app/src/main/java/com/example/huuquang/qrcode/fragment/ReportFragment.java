package com.example.huuquang.qrcode.fragment;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.adapter.ReportFunctionButtonAdapter;
import com.example.huuquang.qrcode.fragment.report.ReportDetailItemGroupFragment;
import com.example.huuquang.qrcode.fragment.report.ReportDetailItemInfoFragment;
import com.example.huuquang.qrcode.fragment.report.ReportDetailItemPOFragment;
import com.example.huuquang.qrcode.fragment.report.ReportDetailLocationEmptyFragment;
import com.example.huuquang.qrcode.fragment.report.ReportDetailLocationGroupFragment;
import com.example.huuquang.qrcode.fragment.report.ReportDetailLocationInfoFragment;
import com.example.huuquang.qrcode.fragment.report.ReportDetailItemPLFragment;
import com.example.huuquang.qrcode.fragment.report.ReportDetailWorkerAllFragment;
import com.example.huuquang.qrcode.fragment.report.ReportDetailWorkerTransactionFragment;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReportFragment extends Fragment {

    private RecyclerView mLocateRecycleView;
    private ReportFunctionButtonAdapter mLocateButtonAdapter;
    private RecyclerView mWorkerRecycleView;
    private ReportFunctionButtonAdapter mWorkerButtonAdapter;
    private RecyclerView mItemRecycleView;
    private ReportFunctionButtonAdapter mItemButtonAdapter;

    private DatabaseReference locationRef;
    private DatabaseReference itemRef;
    private DatabaseReference userRef;

    public ReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLocateButton();
        initItemButton();
        initWorkerButton();

        mLocateRecycleView = view.findViewById(R.id.report_location_recycle_view);
        mLocateRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mLocateRecycleView.setAdapter(mLocateButtonAdapter);

        mWorkerRecycleView = view.findViewById(R.id.report_worker_recycle_view);
        mWorkerRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mWorkerRecycleView.setAdapter(mWorkerButtonAdapter);

        mItemRecycleView = view.findViewById(R.id.report_item_recycle_view);
        mItemRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mItemRecycleView.setAdapter(mItemButtonAdapter);

        itemRef = FirebaseDatabase.getInstance()
                .getReference("companies").child(UserUtil.getCurrentUser().getCompany_id())
                .child("items");
        locationRef = itemRef.getParent().child("plants").child(UserUtil.getCurrentUser().getPlant_id())
                .child("location");
        userRef = FirebaseDatabase.getInstance()
                .getReference("users");
    }

    private void initLocateButton(){
        Drawable img = getContext().getResources().getDrawable(R.drawable.shelf_ic);
        img.setBounds(0, 0, 180, 180 );

        Button btn1 = new Button(getContext());
        btn1.setText("Chung");
        btn1.setCompoundDrawables(null, img, null, null);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailLocationInfoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        Button btn2 = new Button(getContext());
        btn2.setText("Còn Trống");
        btn2.setCompoundDrawables(null, img, null, null);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailLocationEmptyFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        Button btn3 = new Button(getContext());
        btn3.setText("Gom Nhóm");
        btn3.setCompoundDrawables(null, img, null, null);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailLocationGroupFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        List<Button> list = new ArrayList<>();
        list.add(btn1);
        list.add(btn2);
        list.add(btn3);
        mLocateButtonAdapter = new ReportFunctionButtonAdapter(list);
    }

    private void initItemButton(){
        Drawable img = getContext().getResources().getDrawable(R.drawable.product);
        img.setBounds(0, 0, 180, 180 );

        Button btn1 = new Button(getContext());
        btn1.setText("Chung");
        btn1.setCompoundDrawables(null, img, null, null);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailItemInfoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

//        Button btn2 = new Button(getContext());
//        btn2.setText("Vị Trí");
//        btn2.setCompoundDrawables(null, img, null, null);
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });

        Button btn3 = new Button(getContext());
        btn3.setText("Gom Nhóm");
        btn3.setCompoundDrawables(null, img, null, null);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailItemGroupFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        Button btn4 = new Button(getContext());
        btn4.setText("PO");
        btn4.setCompoundDrawables(null, img, null, null);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailItemPOFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        Button btn5 = new Button(getContext());
        btn5.setText("PL");
        btn5.setCompoundDrawables(null, img, null, null);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailItemPLFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        List<Button> list = new ArrayList<>();
        list.add(btn1);
//        list.add(btn2);
        list.add(btn3);
        list.add(btn4);
        list.add(btn5);
        mItemButtonAdapter = new ReportFunctionButtonAdapter(list);
    }

    private void initWorkerButton(){
        Drawable img = getContext().getResources().getDrawable(R.drawable.engineer);
        img.setBounds(0, 0, 180, 180 );

        Button btn1 = new Button(getContext());
        btn1.setText("Tất Cả");
        btn1.setCompoundDrawables(null, img, null, null);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailWorkerAllFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        Button btn2 = new Button(getContext());
        btn2.setText("Hoàn Thành");
        btn2.setCompoundDrawables(null, img, null, null);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button b = (Button)v;
//                Toast.makeText(getContext(), b.getText(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new ReportDetailWorkerTransactionFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        List<Button> list = new ArrayList<>();
        list.add(btn1);
        list.add(btn2);
        mWorkerButtonAdapter = new ReportFunctionButtonAdapter(list);
    }
}
