package com.example.huuquang.qrcode.model;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Message {
    public static final int TYPE_MINE = 1;
    public static final int TYPE_YOUR = 2;

    private String content;
    private String fullName;
    private String uid;
    private long time;
    private int type;
    private String link;

    public Message() {
    }

    public Message(String content, String uid, long time) {
        this.content = content;
        this.uid = uid;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("content", this.content);
        map.put("uid", this.uid);
        map.put("fullName", this.fullName);
        map.put("link", this.link);

        return map;
    }

    public String toTimeString(){
        Date date = new Date(this.time);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);

        return format.format(date);
    }
}
