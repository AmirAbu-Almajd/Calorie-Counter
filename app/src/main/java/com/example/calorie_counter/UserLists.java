package com.example.calorie_counter;

import java.util.LinkedList;
import java.util.List;

public class UserLists {
   List<GroceryList> lists = new LinkedList<>();
   UserLists(GroceryList newList){
      lists.add(newList);
   }
   UserLists(){
   }
}
