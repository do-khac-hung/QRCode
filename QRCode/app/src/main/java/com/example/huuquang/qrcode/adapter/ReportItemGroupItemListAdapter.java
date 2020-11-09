package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Location;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReportItemGroupItemListAdapter extends RecyclerView.Adapter<ReportItemGroupItemListAdapter.ReportItemGroupItemListViewHolder> {

    private Map<Location, Float> list;

    public ReportItemGroupItemListAdapter() {
        list = new LinkedHashMap<>();
    }

    @Override
    public ReportItemGroupItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_item_group_item, parent, false);
        return new ReportItemGroupItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportItemGroupItemListViewHolder holder, int position) {
        Location location = list.keySet().toArray(new Location[0])[position];
        holder.idTextView.setText(location.getId());
        holder.quantityTextView.setText(String.valueOf(list.get(location)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(Map<Location, Float> list) {
        this.list = list;
    }

    public class ReportItemGroupItemListViewHolder extends RecyclerView.ViewHolder{
        public TextView idTextView;
        public TextView quantityTextView;

        public ReportItemGroupItemListViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.location_id);
            quantityTextView = itemView.findViewById(R.id.location_quantity);
        }
    }
}
