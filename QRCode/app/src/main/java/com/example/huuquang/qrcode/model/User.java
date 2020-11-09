package com.example.huuquang.qrcode.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String email;
    private String fullname;
    private String company_id;
    private String plant_id;
    private String uid;

    public User() { }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("email", this.email);
        data.put("fullname", this.fullname);
        data.put("company_id", this.company_id);
        data.put("plant_id", this.plant_id);

        return data;
    }
}
