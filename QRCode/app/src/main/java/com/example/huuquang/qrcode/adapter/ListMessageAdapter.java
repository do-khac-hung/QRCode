package com.example.huuquang.qrcode.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.example.huuquang.qrcode.R;
import com.example.huuquang.qrcode.model.Message;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.List;

/**
 * Created by HuuQuang on 3/11/2018.
 */

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.MessageViewHolder> {
    private List<Message> messages;
    private final MessageAdapterOnTouchListener mTouchListener;
    private Context context;

    public ListMessageAdapter(List<Message> messages, MessageAdapterOnTouchListener listener, Context context) {
        this.messages = messages;
        this.mTouchListener = listener;
        this.context = context;
    }

    public void addItem(Message item){
        messages.add(item);
        notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case Message.TYPE_MINE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_message_mine, parent, false);
                break;
            case Message.TYPE_YOUR:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_message_your, parent, false);
        }

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        if(message.getLink() == null) {
            holder.mContent.setTypeface(null, Typeface.NORMAL);
            holder.mContent.setText(message.getContent());
        }else{
            final Uri uri = Uri.parse(message.getLink());
            String filename = getFileName(uri);
            if(filename.contains("/")){
                String tmp[] = filename.split("/");
                filename = tmp[tmp.length - 1];
            }

            holder.mContent.setText(filename);
            holder.mContent.setTypeface(null, Typeface.BOLD);
            holder.mContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "DOWNLOAD STARTED", Toast.LENGTH_SHORT).show();
                    String filename = getFileName(uri);
                    PRDownloader.download(uri.toString(), context.getExternalFilesDir(null).getPath(), filename)
                            .build().start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            Toast.makeText(context, "DOWNLOAD COMPLETED", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Error error) {
                            Toast.makeText(context, "DOWNLOAD FAILED", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
        holder.mUser.setText(message.getFullName());
        holder.mTime.setText(message.toTimeString());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public TextView mContent;
        public TextView mUser;
        public TextView mTime;

        public MessageViewHolder(View view) {
            super(view);
            mContent = (TextView) view.findViewById(R.id.message_content);
            mUser = (TextView) view.findViewById(R.id.message_user);
            mTime = (TextView) view.findViewById(R.id.message_time);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mTouchListener.onLongTouch(messages.get(getAdapterPosition()));
            return false;
        }
    }

    public void clear(){
        messages.clear();
        notifyDataSetChanged();
    }

    private String getFileName(Uri uri){
        String filename = uri.getLastPathSegment();
        if(filename.contains("/")){
            String tmp[] = filename.split("/");
            filename = tmp[tmp.length - 1];
        }
        return filename;
    }

    public interface MessageAdapterOnTouchListener{
        void onLongTouch(Message message);
    }
}
