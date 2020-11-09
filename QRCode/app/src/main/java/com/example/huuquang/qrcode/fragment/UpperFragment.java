package com.example.huuquang.qrcode.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.huuquang.qrcode.R;

public class UpperFragment extends Fragment{
    private ImageButton mChatButton;
    //private TextView mNotiText;
    private UpperFragmentCallback callback;

    public UpperFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (UpperFragmentCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upper, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChatButton = view.findViewById(R.id.action_chat);
        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onChatButtonClick();
            }
        });

        //mNotiText = view.findViewById(R.id.upper_noti);
    }

//    public void setNotiText(String text){
//        mNotiText.setText(text);
//    }

    public interface UpperFragmentCallback{
        void onChatButtonClick();
    }
}
