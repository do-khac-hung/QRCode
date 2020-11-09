package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ReportWorkerTransactionItemListAdapter extends RecyclerView.Adapter<ReportWorkerTransactionItemListAdapter.ReportWorkerTransactionItemListViewHolder> {
    private List<Transaction> list;

    public ReportWorkerTransactionItemListAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public ReportWorkerTransactionItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_worker_transaction_item, parent, false);
        return new ReportWorkerTransactionItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportWorkerTransactionItemListViewHolder holder, int position) {
        Transaction transaction = list.get(position);
        switch (transaction.getStatus()){
            case Transaction.INCOME:
                holder.typeTextView.setText("PO");
                holder.codeTextView.setText(transaction.getPo());
                break;
            case Transaction.OUTCOME:
                holder.typeTextView.setText("PL");
                holder.codeTextView.setText(transaction.getPl());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Transaction transaction){
        list.add(transaction);
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void setList(List<Transaction> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ReportWorkerTransactionItemListViewHolder extends RecyclerView.ViewHolder{
        public TextView typeTextView;
        public TextView codeTextView;

        public ReportWorkerTransactionItemListViewHolder(View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.report_worker_item_transaction_type);
            codeTextView = itemView.findViewById(R.id.report_worker_item_transaction_code);
        }
    }
}
