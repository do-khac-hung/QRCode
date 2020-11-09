package com.example.huuquang.qrcode.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Location2Item {
    private String locationId;
    private Map<Item, Float> items;

    public Location2Item() {
        items = new LinkedHashMap<>();
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Map<Item, Float> getItems() {
        return items;
    }

    public void setItems(Map<Item, Float> items) {
        this.items = items;
    }

    public void addItem(Item item, Float quantity){
        Float currentQuantity = items.get(item);
        if(currentQuantity == null){
            items.put(item, quantity);
        }else{
            currentQuantity += quantity;
            items.put(item, currentQuantity);
        }
    }

    public void clear(){
        items.clear();
    }
}
