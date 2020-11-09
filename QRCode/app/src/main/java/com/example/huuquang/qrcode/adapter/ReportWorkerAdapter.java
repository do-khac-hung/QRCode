package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.User;

import java.util.ArrayList;
import java.util.List;

public class ReportWorkerAdapter extends RecyclerView.Adapter<ReportWorkerAdapter.ReportWorkerViewHolder> {
    private List<User> list;

    public ReportWorkerAdapter() {
        list = new ArrayList<>();
    }

    public ReportWorkerAdapter(List<User> userList) {
        this.list = userList;
    }

    @Override
    public ReportWorkerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_worker, parent, false);
        return new ReportWorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportWorkerViewHolder holder, int position) {
        User user = list.get(position);
        holder.idTextView.setText(user.getUid());
        holder.fullNameTextView.setText(user.getFullname());
        holder.emailTextView.setText(user.getEmail());
        holder.detailLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(User user){
        list.add(user);
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void setList(List<User> newList){
        if(newList == null){
            clear();
        }else{
            this.list = newList;
            notifyDataSetChanged();
        }
    }

    public  class ReportWorkerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView idTextView;
        public TextView fullNameTextView;
        public TextView emailTextView;
        public ViewGroup detailLayout;
        public ImageView icon;

        public ReportWorkerViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.report_worker_item_id);
            fullNameTextView = itemView.findViewById(R.id.report_worker_item_fullname);
            emailTextView = itemView.findViewById(R.id.report_worker_item_email);
            detailLayout = itemView.findViewById(R.id.report_worker_item_detail);
            icon = itemView.findViewById(R.id.report_worker_icon_more);

            ((ViewGroup)fullNameTextView.getParent()).setOnClickListener(this);
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
