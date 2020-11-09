package com.example.huuquang.qrcode.fragment.report;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huuquang.qrcode.BuildConfig;
import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.adapter.ReportItemTableAdapter;
import com.example.huuquang.qrcode.model.Item;
import com.example.huuquang.qrcode.model.TransactionItem;
import com.example.huuquang.qrcode.util.UserUtil;
import com.example.huuquang.qrcode.model.Transaction;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportDetailItemPOFragment extends Fragment {
    private TextView titleTextView, typeTextView;
    private EditText searchEditText;
    private Button searchButton, toCSV1Button, toCSV2Button;
    private RecyclerView reportTableRecycleView;
    private ReportItemTableAdapter adapter;

    private DatabaseReference transactionRef;
    private DatabaseReference itemRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_detail_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionRef = FirebaseDatabase.getInstance().getReference("companies").child(UserUtil.getCurrentUser().getCompany_id())
                .child("plants").child(UserUtil.getCurrentUser().getPlant_id())
                .child("transactions");
        itemRef = FirebaseDatabase.getInstance().getReference("companies").child(UserUtil.getCurrentUser().getCompany_id())
                .child("items");

        adapter = new ReportItemTableAdapter();

        titleTextView = view.findViewById(R.id.report_title);
        titleTextView.setText("Vật Tư PO");
        typeTextView = view.findViewById(R.id.report_search_type_button);
        typeTextView.setText("PO");

        reportTableRecycleView = view.findViewById(R.id.report_table);
        reportTableRecycleView.setAdapter(adapter);
        reportTableRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        searchEditText = view.findViewById(R.id.report_search_edit_text);
        searchButton = view.findViewById(R.id.report_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String po = searchEditText.getText().toString();
                if(po.trim().isEmpty()) return;
                transactionRef.orderByChild("po").equalTo(po).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            for(DataSnapshot childSnap : dataSnapshot.getChildren()){
                                final Transaction transaction = childSnap.getValue(Transaction.class);
                                itemRef.orderByKey().equalTo(transaction.getItem_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChildren() && dataSnapshot.getChildrenCount() == 1){
                                            for(DataSnapshot tmp : dataSnapshot.getChildren()){
                                                Item item = tmp.getValue(Item.class);
                                                adapter.add(transaction, item);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }else{
                            Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        toCSV1Button = view.findViewById(R.id.report_to_csv1);
        toCSV1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getItemCount() == 0){
                    return;
                }

                File file = saveFile();
                if(file!=null) {
                    Toast.makeText(getContext(), "Đã Lưu", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lỗi File", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toCSV2Button = view.findViewById(R.id.report_to_csv2);
        toCSV2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getItemCount() == 0){
                    return;
                }

                File file = saveFile();
                Uri uri = null;
                if(file != null){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        uri = FileProvider.getUriForFile(getActivity(),
                                BuildConfig.APPLICATION_ID + ".provider",
                                file);
                    }else{
                        uri = Uri.fromFile(file);
                    }

                    Log.d(ReportDetailItemPOFragment.this.getClass().getSimpleName(), uri.toString());

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Báo Cáo " + searchEditText.getText());
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.putExtra(Intent.EXTRA_TEXT, "Chào anh/chị,\n\n" +
                            "Em xin phép gửi lại báo cáo cho anh/chị.\n\n" +
                            UserUtil.getCurrentUser().getFullname());
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(),"File Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private File saveFile(){
        String po = adapter.getList().get(0).getTransaction().getPo();
        List<String> lines = toCSV();

        File file = new File(getActivity().getExternalFilesDir(null), po + ".csv");

        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            for(String line : lines){
                fos.write(line.getBytes());
            }
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return file;
    }

    private List<String> toCSV(){
        List<String> lines = new ArrayList<>();
        List<TransactionItem> transactions = adapter.getList();
        for(TransactionItem child : transactions){
            Transaction transaction = child.getTransaction();
            lines.add(transaction.toCSV() + "\n");
        }
        return lines;
    }
}
