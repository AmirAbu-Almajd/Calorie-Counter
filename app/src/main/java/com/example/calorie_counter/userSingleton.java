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
    public static Double cal_dailyIntake(Double weight,Double height,String gender,int age)
    {
        Double bmr = 0.0;
        if(gender.equals("Female")){
            bmr = 447.593 + (9.247*weight) + (height*3.098) - (4.330*age);
        }
        else if(gender.equals("Male")){
            bmr = 88.362 + (13.397*weight) + (height*4.799) - (5.677*age);
        }
        Double calorie_calculation = bmr*1.2;
        return calorie_calculation;
    }
}
