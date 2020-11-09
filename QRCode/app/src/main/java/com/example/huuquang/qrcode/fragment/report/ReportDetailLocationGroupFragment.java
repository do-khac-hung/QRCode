package com.example.huuquang.qrcode.fragment.report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.adapter.ReportLocationGroupAdapter;
import com.example.huuquang.qrcode.model.ItemLocation;
import com.example.huuquang.qrcode.model.Location2Item;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ReportDetailLocationGroupFragment extends Fragment {
    TextView mTitleTextView;
    EditText mSearchEditText;
    Button mSearchButton;
    RecyclerView mDetailRecycleView;

    List<Location2Item> locationList;
    List<Location2Item> locationSearchList;
    ReportLocationGroupAdapter adapter;

    DatabaseReference itemLocationRef;
    ChildEventListener itemLocationListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_detail_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleTextView = view.findViewById(R.id.report_title);
        mTitleTextView.setText("Vị Trí - Nhóm");

        mDetailRecycleView = view.findViewById(R.id.report_detail_recycle_view);
        mDetailRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        locationList = new ArrayList<>();
        locationSearchList = new ArrayList<>();
        adapter = new ReportLocationGroupAdapter(locationList);
        mDetailRecycleView.setAdapter(adapter);

        mSearchEditText = view.findViewById(R.id.report_search_edit_text);
        mSearchEditText.setHint("Mã Vị Trí Cần Tìm");
        mSearchButton = view.findViewById(R.id.report_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywordText = mSearchEditText.getText().toString().trim();
                if(!keywordText.isEmpty()){
                    locationSearchList.clear();
                    for(Location2Item item : locationList){
                        if(item.getLocationId().contains(keywordText)){
                            locationSearchList.add(item);
                        }
                    }

                    if(locationSearchList.isEmpty()){
                        Toast.makeText(getContext(), "Không Tồn Tại", Toast.LENGTH_SHORT).show();
                    }else{
                        adapter.setList(locationSearchList);
                    }
                }else{
                    adapter.setList(locationList);
                }
            }
        });

        itemLocationRef = FirebaseDatabase.getInstance().getReference("companies").child(UserUtil.getCurrentUser().getCompany_id())
                .child("plants").child(UserUtil.getCurrentUser().getPlant_id())
                .child("item_location");

        itemLocationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ItemLocation itemLocation = dataSnapshot.getValue(ItemLocation.class);
                adapter.addItem(itemLocation);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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
        itemLocationRef.addChildEventListener(itemLocationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.clear();
        itemLocationRef.removeEventListener(itemLocationListener);
    }
}
