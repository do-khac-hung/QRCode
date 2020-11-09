package com.example.huuquang.qrcode.model;

import java.util.Date;

public class Transaction {
    private Date date;
    private String item_id;
    private String description;
    private Integer status;
    private String user_id;
    private String locate_id;
    private String po;
    private String pl;

    public Transaction() { }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocate_id() {
        return locate_id;
    }

    public void setLocate_id(String locate_id) {
        this.locate_id = locate_id;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public static final int INCOME = 1;
    public static final int OUTCOME = 2;

    public String toCSV(){
        String line = null;
        switch (status){
            case INCOME:
                line = this.status + "," + this.po + "," + this.item_id + "," + this.locate_id + "," + this.description + "," + this.user_id;
                break;
            case OUTCOME:
                line = this.status + "," + this.pl + "," + this.item_id + "," + this.locate_id + "," + this.description + "," + this.user_id;
                break;
        }
        return line;
    }
}
