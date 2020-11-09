package com.example.huuquang.qrcode.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Item;
import com.example.huuquang.qrcode.model.Transaction;
import com.example.huuquang.qrcode.model.TransactionItem;

import java.util.ArrayList;
import java.util.List;

public class ReportItemTableAdapter extends RecyclerView.Adapter<ReportItemTableAdapter.ReportItemTableViewHolder> {
    List<TransactionItem> list;

    public ReportItemTableAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public ReportItemTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_item_table, parent, false);
        return new ReportItemTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportItemTableViewHolder holder, int position) {
        Transaction transaction = list.get(position).getTransaction();
        Item item = list.get(position).getItem();
        holder.indexTextView.setText(String.valueOf(position+1));
        holder.idItemTextView.setText(transaction.getItem_id());
        holder.idLocationTextView.setText(transaction.getLocate_id());
        holder.descriptionTextView.setText(transaction.getDescription());
        if(item != null){
            holder.unitTextView.setText(item.getUnit());
        }
        switch (position % 2){
            case 0:
                holder.indexTextView.setBackgroundColor(Color.parseColor("#e1e1e1"));
                holder.idItemTextView.setBackgroundColor(Color.parseColor("#e1e1e1"));
                holder.idLocationTextView.setBackgroundColor(Color.parseColor("#e1e1e1"));
                holder.descriptionTextView.setBackgroundColor(Color.parseColor("#e1e1e1"));
                holder.unitTextView.setBackgroundColor(Color.parseColor("#e1e1e1"));
                break;
            case 1:
                holder.indexTextView.setBackgroundColor(Color.parseColor("#f0f0f0"));
                holder.idItemTextView.setBackgroundColor(Color.parseColor("#f0f0f0"));
                holder.idLocationTextView.setBackgroundColor(Color.parseColor("#f0f0f0"));
                holder.descriptionTextView.setBackgroundColor(Color.parseColor("#f0f0f0"));
                holder.unitTextView.setBackgroundColor(Color.parseColor("#f0f0f0"));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(Transaction transaction, Item item) {
        TransactionItem element = new TransactionItem();
        element.setItem(item);
        element.setTransaction(transaction);

        list.add(element);
        notifyDataSetChanged();
    }

    public List<TransactionItem> getList() {
        return list;
    }

    public class ReportItemTableViewHolder extends RecyclerView.ViewHolder{
        public TextView indexTextView;
        public TextView idItemTextView;
        public TextView idLocationTextView;
        public TextView descriptionTextView;
        public TextView unitTextView;

        public ReportItemTableViewHolder(View itemView) {
            super(itemView);
            indexTextView = itemView.findViewById(R.id.table_item_index);
            idItemTextView = itemView.findViewById(R.id.table_item_id_item);
            idLocationTextView = itemView.findViewById(R.id.table_item_id_location);
            descriptionTextView = itemView.findViewById(R.id.table_item_description);
            unitTextView = itemView.findViewById(R.id.table_item_unit);
        }
    }
}
