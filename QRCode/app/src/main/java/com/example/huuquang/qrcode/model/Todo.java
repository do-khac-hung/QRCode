package com.example.huuquang.qrcode.model;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Todo {
    private String id;
    private String content;
    private long time;
    private String petitioner;
    private boolean done;

    public Todo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(String petitioner) {
        this.petitioner = petitioner;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String toTimeString(){
        Date date = new Date(this.time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        return format.format(date);
    }

    public String toDateString(){
        Date date = new Date(this.time);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        return format.format(date);
    }
}
