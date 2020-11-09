package com.example.huuquang.qrcode.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.huuquang.qrcode.R;

import java.util.List;

public class ReportFunctionButtonAdapter extends RecyclerView.Adapter<ReportFunctionButtonAdapter.ReportLocationViewHolder> {
    List<Button> fnButtons;

    public ReportFunctionButtonAdapter(List<Button> fnButtons) {
        this.fnButtons = fnButtons;
    }

    @Override
    public ReportLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_fn, parent, false);
        return new ReportFunctionButtonAdapter.ReportLocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportLocationViewHolder holder, int position) {
        final Button fnButton = fnButtons.get(position);
        holder.mButton.setText(fnButton.getText());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnButton.callOnClick();
            }
        });
        holder.mButton.setCompoundDrawables(null, fnButton.getCompoundDrawables()[1], null, null);
    }
    @Override
    public int getItemCount() {
        return fnButtons.size();
    }

    public class ReportLocationViewHolder extends RecyclerView.ViewHolder{
        public Button mButton;

        public ReportLocationViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.report_fn_button);
        }
    }
}
