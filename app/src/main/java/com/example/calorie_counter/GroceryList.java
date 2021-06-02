package com.example.calorie_counter;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class GroceryList {
    List<String> items = new LinkedList<>();
    List<Double> quantities = new LinkedList<>();
    public Double add(String itemName){
        if(items.isEmpty()){
            items.add(itemName);
            quantities.add(1.0);
            return 1.0;
        }
        for(int i = 0 ; i<items.size();i++){
            if(items.get(i).equals(itemName)){
                quantities.set(i,quantities.get(i)+1);
                return quantities.get(i);
            }
            if(i==(items.size()-1)){
                items.add(itemName);
                quantities.add(1.0);
                return 1.0;
            }
        }
        return 0.0;
    }
    public Double set(String itemName, Double quantity){
        if(quantity == 0.0)
            return 0.0;
        for(int i = 0 ; i<items.size();i++){
            if(items.get(i).equals(itemName)){
                quantities.set(i,quantity);
                return quantities.get(i);
            }
        }
        return 0.0;
    }
    public void show(){
        for(int i = 0 ; i<items.size();i++){
            Log.e("Items",items.get(i)+ " " + quantities.get(i));
        }
    }
}
