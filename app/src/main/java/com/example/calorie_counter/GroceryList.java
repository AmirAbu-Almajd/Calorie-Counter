package com.example.calorie_counter;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class GroceryList {
    List<String> items = new LinkedList<>();
    List<Double> quantities = new LinkedList<>();
    GroceryList(){
        ItemsList itemsList = new ItemsList();
        for (int i = 0 ; i<itemsList.items.size();i++){
            items.add(itemsList.items.get(i));
            quantities.add(0.0);
        }
    }
    public void add(String itemName){
        for(int i = 0 ; i<items.size();i++){
            if(items.get(i).equals(itemName)){
                quantities.set(i,quantities.get(i)+1);
            }
        }
    }
    public void set(String itemName, Double quantity){
        for(int i = 0 ; i<items.size();i++){
            if(items.get(i).equals(itemName)){
                quantities.set(i,quantity);
            }
        }
    }
    public void show(){
        for(int i = 0 ; i<items.size();i++){
            Log.e("Items",items.get(i)+ " " + quantities.get(i));
        }
    }
}
