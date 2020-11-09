package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Item;
import com.example.huuquang.qrcode.model.Item2Location;
import com.example.huuquang.qrcode.model.ItemLocation;
import com.example.huuquang.qrcode.model.Location;
import com.example.huuquang.qrcode.model.Location2Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportItemGroupAdapter
        extends RecyclerView.Adapter<ReportItemGroupAdapter.ReportItemGroupViewHolder> {
    List<Item2Location> list;

    public ReportItemGroupAdapter(List<Item2Location> list) {
        this.list = list;
    }

    public ReportItemGroupAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public ReportItemGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_item_group, parent, false);
        return new ReportItemGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportItemGroupViewHolder holder, int position) {
        Item2Location item2Location = list.get(position);
        holder.idTextView.setText(item2Location.getIdItem());
        holder.detailLayout.setVisibility(View.GONE);

        ReportItemGroupItemListAdapter adapter = new ReportItemGroupItemListAdapter();
        adapter.setList(item2Location.getLocations());
        holder.itemsRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(ItemLocation itemLocation){
        String itemId = itemLocation.getItem_id();
        String locationId = itemLocation.getLocation_id();
        Float quantity = Float.parseFloat(itemLocation.getQuantity());

        Item2Location newItem = null;

        Location location = new Location();
        location.setId(locationId);

        for(Item2Location item : list){
            if(item.getIdItem().equals(itemId)){
                item.addItem(location, quantity);
                newItem = item;
            }
        }

        if(newItem == null){
            newItem = new Item2Location();
            newItem.setIdItem(itemId);
            newItem.addItem(location, quantity);

            list.add(newItem);
        }

        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void setList(List<Item2Location> newList){
        if(newList == null){
            clear();
        }else{
            this.list = newList;
            notifyDataSetChanged();
        }
    }

    public class ReportItemGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView idTextView;
        public RecyclerView itemsRecyclerView;
        public ViewGroup detailLayout;
        public ImageView icon;

        public ReportItemGroupViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.report_item_item_id);
            ((ViewGroup)idTextView.getParent()).setOnClickListener(this);
            detailLayout = itemView.findViewById(R.id.report_item_item_detail);
            itemsRecyclerView = itemView.findViewById(R.id.report_item_item_list_item_recycle_view);
            itemsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            icon = itemView.findViewById(R.id.report_item_icon_more);
        }

        @Override
        public void onClick(View v) {
            switch (detailLayout.getVisibility()){
                case View.GONE:
                    detailLayout.setVisibility(View.VISIBLE);
                    icon.setRotation(180);
                    break;
                case View.VISIBLE:
                    detailLayout.setVisibility(View.GONE);
                    icon.setRotation(0);
                    break;
            }
        }
    }
}
