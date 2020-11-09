package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportLocationGroupItemListAdapter extends RecyclerView.Adapter<ReportLocationGroupItemListAdapter.ReportLocationGroupItemListViewHolder>{
    private Map<Item, Float> list;

    public ReportLocationGroupItemListAdapter() {
        list = new LinkedHashMap<>();
    }

    public void setList(Map<Item, Float> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ReportLocationGroupItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_location_group_item, parent, false);
        return new ReportLocationGroupItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportLocationGroupItemListViewHolder holder, int position) {
        Item item = list.keySet().toArray(new Item[0])[position];
        Float quantity = list.get(item);

        holder.idTextView.setText(item.getId());
        holder.quantityTextView.setText(String.valueOf(quantity));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Item item, Float quantity){
        Float currentQuantity = list.get(item);
        if(currentQuantity == null){
            list.put(item, quantity);
        }else{
            currentQuantity += quantity;
            list.put(item, currentQuantity);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public class ReportLocationGroupItemListViewHolder extends RecyclerView.ViewHolder{
        public TextView idTextView;
        public TextView quantityTextView;

        public ReportLocationGroupItemListViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.item_id);
            quantityTextView = itemView.findViewById(R.id.item_quantity);
        }
    }
}
