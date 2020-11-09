package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.ReportItemViewHolder>{
    List<Item> list;

    public ReportItemAdapter() {
        list = new ArrayList<>();
    }

    public ReportItemAdapter(List<Item> list) {
        this.list = list;
    }

    @Override
    public ReportItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_item_info, parent, false);
        return new ReportItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportItemViewHolder holder, int position) {
        Item item = list.get(position);
        holder.codeTextView.setText(item.getCode());
        holder.idTextView.setText(item.getId());
        holder.materialIdTextView.setText(item.getMaterial_id());
        holder.unitTextView.setText(item.getUnit());
        holder.unitTextView.setText(item.getUnit());

        switch (item.getStatus()){
            case Item.ITEM_USED:
                holder.statusTextView.setText("Sử Dụng");
                break;
            case Item.ITEM_UNUSED:
                holder.statusTextView.setText("Không Dùng");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Item item){
        list.add(item);
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void setList(List<Item> newList){
        if(newList == null){
            clear();
        }else{
            this.list = newList;
            notifyDataSetChanged();
        }
    }

    public class ReportItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView idTextView;
        public TextView codeTextView;
        public TextView materialIdTextView;
        public TextView statusTextView;
        public TextView unitTextView;
        public ViewGroup detailLayout;
        public ImageView icon;

        public ReportItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.report_item_item_id);
            codeTextView = itemView.findViewById(R.id.report_item_item_code);
            materialIdTextView = itemView.findViewById(R.id.report_item_item_material_id);
            statusTextView = itemView.findViewById(R.id.report_item_item_status);
            unitTextView = itemView.findViewById(R.id.report_item_item_unit);
            detailLayout = itemView.findViewById(R.id.report_item_item_detail);
            icon = itemView.findViewById(R.id.report_item_icon_more);

            ((ViewGroup)idTextView.getParent()).setOnClickListener(this);
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
