package com.example.calorie_counter;

public class userSingleton {
    private static int id = 0;
    protected userSingleton() {
        // Exists only to defeat instantiation.
    }
    public static int getId() {
        return id;
    }

    public static void setId(int idIn){
        id = idIn;
    }
}
