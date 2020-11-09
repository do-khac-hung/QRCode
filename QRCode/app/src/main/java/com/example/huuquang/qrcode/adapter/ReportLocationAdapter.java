package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Location;

import java.util.ArrayList;
import java.util.List;

public class ReportLocationAdapter extends RecyclerView.Adapter<ReportLocationAdapter.ReportLocationViewHolder> {
    private List<Location> list;

    public ReportLocationAdapter() {
        list = new ArrayList<>();
    }

    public ReportLocationAdapter(List<Location> locationList) {
        this.list = locationList;
    }

    @Override
    public ReportLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_location_info, parent, false);
        return new ReportLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportLocationViewHolder holder, int position) {
        Location location = list.get(position);
        holder.idTextView.setText(location.getId());
        holder.codeTextView.setText(location.getCode());
        holder.descriptionTextView.setText(location.getDescription());
        switch (location.getStatus()){
            case Location.STATUS_USED:
                holder.statusTextView.setText("Sử Dụng");
                break;
            case Location.STATUS_UNUSED:
                holder.statusTextView.setText("Không Sử Dụng");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Location location){
        list.add(location);
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void setList(List<Location> newList){
        if(newList == null){
            clear();
        }else{
            this.list = newList;
            notifyDataSetChanged();
        }
    }

    public class ReportLocationViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public TextView idTextView;
        public TextView codeTextView;
        public TextView descriptionTextView;
        public TextView statusTextView;
        public ViewGroup detailLayout;
        public ImageView icon;

        public ReportLocationViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.report_location_item_id);
            codeTextView = itemView.findViewById(R.id.report_location_item_code);
            descriptionTextView = itemView.findViewById(R.id.report_location_item_description);
            statusTextView = itemView.findViewById(R.id.report_location_item_status);
            detailLayout = itemView.findViewById(R.id.report_location_item_detail);
            icon = itemView.findViewById(R.id.report_location_icon_more);

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
