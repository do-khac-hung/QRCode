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
import com.example.huuquang.qrcode.adapter.ReportLocationAdapter;
import com.example.huuquang.qrcode.model.Location;
import com.example.huuquang.qrcode.util.UserUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ReportDetailLocationInfoFragment extends Fragment {
    TextView mTitleTextView;
    EditText mSearchEditText;
    Button mSearchButton;
    RecyclerView mDetailRecycleView;

    List<Location> locationList;
    List<Location> locationSearchList;
    ReportLocationAdapter adapter;

    DatabaseReference locationRef;
    ChildEventListener locationListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_detail_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleTextView = view.findViewById(R.id.report_title);
        mTitleTextView.setText("Vị Trí - Chung");

        mDetailRecycleView = view.findViewById(R.id.report_detail_recycle_view);
        mDetailRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        locationList = new ArrayList<>();
        locationSearchList = new ArrayList<>();
        adapter = new ReportLocationAdapter(locationList);
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
                    for(Location item : locationList){
                        if(item.getId().contains(keywordText)){
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


        locationRef = FirebaseDatabase.getInstance().getReference("companies").child(UserUtil.getCurrentUser().getCompany_id())
                .child("plants").child(UserUtil.getCurrentUser().getPlant_id())
                .child("locations");
        locationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Location location = dataSnapshot.getValue(Location.class);
                location.setId(dataSnapshot.getKey());
                adapter.addItem(location);
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
        locationRef.addChildEventListener(locationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.clear();
        locationRef.removeEventListener(locationListener);
    }
}
