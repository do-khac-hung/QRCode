package com.example.huuquang.qrcode.model;

public class TransactionItem {
    private Transaction transaction;
    private Item item;

    public TransactionItem() {
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
