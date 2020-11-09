package com.example.huuquang.qrcode.model;

/**
 * Created by HuuQuang on 3/10/2018.
 */

public class Item {
    public static final int ITEM_USED = 1;
    public static final int ITEM_UNUSED = 2;

    private String id;
    private String code;
    private String material_id;
    private int status;
    private String unit;
//    private double quantity;

    public Item() {}

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

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

//    public double getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(double quantity) {
//        this.quantity = quantity;
//    }

    public String toCSV(){
        return id + "," + material_id + "," + code + "," + status + "," + unit;
    }
}
