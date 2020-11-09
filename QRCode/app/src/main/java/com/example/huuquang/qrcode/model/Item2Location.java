package com.example.huuquang.qrcode.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Item2Location {
    private String idItem;
    private Map<Location, Float> locations;

    public Item2Location() {
        locations = new LinkedHashMap<>();
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public Map<Location, Float> getLocations() {
        return locations;
    }

    public void setLocations(Map<Location, Float> locations) {
        this.locations = locations;
    }

    public void addItem(Location location, Float quantity){
        Float currentQuantity = locations.get(location);
        if(currentQuantity == null){
            locations.put(location, quantity);
        }else{
            currentQuantity += quantity;
            locations.put(location, currentQuantity);
        }
    }

    public void clear(){
        locations.clear();
    }
}
