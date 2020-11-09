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
import com.example.huuquang.qrcode.model.ItemLocation;
import com.example.huuquang.qrcode.model.Location2Item;

import java.util.ArrayList;
import java.util.List;


public class ReportLocationGroupAdapter extends RecyclerView.Adapter<ReportLocationGroupAdapter.ReportLocationGroupViewHolder> {

    List<Location2Item> list;

    public ReportLocationGroupAdapter() {
        list = new ArrayList<>();
    }

    public ReportLocationGroupAdapter(List<Location2Item> locationList) {
        this.list = locationList;
    }

    @Override
    public ReportLocationGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_location_group, parent, false);
        return new ReportLocationGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportLocationGroupViewHolder holder, int position) {
        Location2Item location2Item = list.get(position);
        holder.idTextView.setText(location2Item.getLocationId());
        holder.detailLayout.setVisibility(View.GONE);

        ReportLocationGroupItemListAdapter adapter = new ReportLocationGroupItemListAdapter();
        adapter.setList(location2Item.getItems());
        holder.itemsRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(ItemLocation item){
        addItem(item.getLocation_id(), item.getItem_id(), Float.parseFloat(item.getQuantity() ));
    }

    public void addItem(String locationId, String itemId, Float quantity){
        Location2Item newLocation2Item = null;

        Item newItem = new Item();
        newItem.setId(itemId);

        for(Location2Item item : list){
            if(item.getLocationId().equals(locationId)){
                item.addItem(newItem, quantity);
                newLocation2Item = item;
                break;
            }
        }

        if(newLocation2Item == null){
            newLocation2Item = new Location2Item();
            newLocation2Item.setLocationId(locationId);
            newLocation2Item.addItem(newItem, quantity);

            list.add(newLocation2Item);
        }

        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public List<Location2Item> getList() {
        return list;
    }

    public void setList(List<Location2Item> newList){
        if(newList == null){
            clear();
        }else{
            this.list = newList;
            notifyDataSetChanged();
        }
    }

    public class ReportLocationGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView idTextView;
        public RecyclerView itemsRecyclerView;
        public ViewGroup detailLayout;
        public ImageView icon;

        public ReportLocationGroupViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.report_location_item_id);
            ((ViewGroup)idTextView.getParent()).setOnClickListener(this);
            detailLayout = itemView.findViewById(R.id.report_location_item_detail);
            itemsRecyclerView = itemView.findViewById(R.id.report_location_item_list_item_recycle_view);
            itemsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            icon = itemView.findViewById(R.id.report_location_icon_more);
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
