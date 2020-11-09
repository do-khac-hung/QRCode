package com.example.huuquang.qrcode.model;

import android.view.View;

public class ButtonReport {
    private int drawableSrc;
    private String label;
    private View.OnClickListener onClickListener;

    public ButtonReport() {
    }

    public int getDrawableSrc() {
        return drawableSrc;
    }

    public void setDrawableSrc(int drawableSrc) {
        this.drawableSrc = drawableSrc;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
