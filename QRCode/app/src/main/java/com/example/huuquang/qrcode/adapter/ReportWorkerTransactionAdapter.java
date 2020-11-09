package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Transaction;
import com.example.huuquang.qrcode.model.User;
import com.example.huuquang.qrcode.model.User2Transaction;

import java.util.ArrayList;
import java.util.List;

public class ReportWorkerTransactionAdapter extends RecyclerView.Adapter<ReportWorkerTransactionAdapter.ReportWorkerTransactionViewHolder> {
    private List<User2Transaction> list;

    public ReportWorkerTransactionAdapter() {
        list = new ArrayList<>();
    }

    public ReportWorkerTransactionAdapter(List<User2Transaction> list) {
        this.list = list;
    }

    @Override
    public ReportWorkerTransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_worker_transaction, parent, false);
        return new ReportWorkerTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportWorkerTransactionViewHolder holder, int position) {
        User2Transaction item = list.get(position);
        holder.fullnameTextView.setText(item.getUser().getFullname());

        ReportWorkerTransactionItemListAdapter adapter = new ReportWorkerTransactionItemListAdapter();
        holder.itemRecycleView.setAdapter(adapter);
        adapter.setList(item.getList());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(User user, Transaction transaction){
        User2Transaction newItem = null;
        for(User2Transaction item : list){
            if(item.getUser().getUid().equals(user.getUid())){
                item.addItem(transaction);
                newItem = item;
                break;
            }
        }
        if(newItem == null){
            newItem = new User2Transaction();
            newItem.setUser(user);
            newItem.addItem(transaction);

            list.add(newItem);
        }

        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void setList(List<User2Transaction> newList){
        if(newList == null){
            clear();
        }else{
            this.list = newList;
            notifyDataSetChanged();
        }
    }

    public class ReportWorkerTransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView fullnameTextView;
        public RecyclerView itemRecycleView;
        public ViewGroup detailLayout;
        public ImageView icon;

        public ReportWorkerTransactionViewHolder(View itemView) {
            super(itemView);
            fullnameTextView = itemView.findViewById(R.id.report_worker_item_fullname);
            itemRecycleView = itemView.findViewById(R.id.report_worker_item_list_item_recycle_view);
            itemRecycleView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            detailLayout = itemView.findViewById(R.id.report_worker_item_detail);
            icon = itemView.findViewById(R.id.report_worker_icon_more);

            ((ViewGroup)fullnameTextView.getParent()).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (detailLayout.getVisibility()){
                case View.VISIBLE:
                    detailLayout.setVisibility(View.GONE);
                    icon.setRotation(0);
                    break;
                case View.GONE:
                    detailLayout.setVisibility(View.VISIBLE);
                    icon.setRotation(180);
                    break;
            }
        }
    }
}
