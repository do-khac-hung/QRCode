package com.example.huuquang.qrcode.model;

public class Location {
    public static final int STATUS_USED = 1;
    public static final int STATUS_UNUSED = 2;

    private String id;
    private String code;
    private String description;
    private int status;

    public Location() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
