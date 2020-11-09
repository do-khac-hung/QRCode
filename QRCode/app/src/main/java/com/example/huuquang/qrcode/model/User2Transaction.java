package com.example.huuquang.qrcode.model;

import java.util.ArrayList;
import java.util.List;

public class User2Transaction {
    private User user;
    private List<Transaction> list;

    public User2Transaction() {
        list = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getList() {
        return list;
    }

    public void setList(List<Transaction> list) {
        this.list = list;
    }

    public void addItem(Transaction transaction){
        list.add(transaction);
    }
}
