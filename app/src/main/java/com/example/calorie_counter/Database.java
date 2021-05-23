package com.example.calorie_counter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
public class Database extends SQLiteOpenHelper {
    private static String databaseName = "CalorieDB";

    SQLiteDatabase calorieDatabase;

    public Database(Context context){
        super(context,databaseName,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table users(id integer primary key, name text not null, email text unique not null, password text not null "+
                ", age integer , height real, weight real , gender text , daily_intake integer )");
        db.execSQL("create table user_meals(user_id integer , meal text , date Date,quantity real, calories real," +
                "PRIMARY KEY(user_id,meal,date),CONSTRAINT fk_col FOREIGN KEY(user_id) REFERENCES users(id))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        onCreate(db);
    }

    public void createUser(String name , String email , String password , int age ,double height ,
                           double weight , String gender, double daily_intake ){
        ContentValues row = new ContentValues();
        row.put("name",name);
        row.put("email",email);
        row.put("password",password);
        row.put("age",age);
        row.put("height",height);
        row.put("weight",weight);
        row.put("gender",gender);
        row.put("daily_intake",daily_intake);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.insert("users",null,row);
        calorieDatabase.close();
    }

    public Cursor getAllUsers(){
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {"name","email","password"};
        Cursor cursor = calorieDatabase.query("users" , rowDetails,null,null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();
        calorieDatabase.close();
        return cursor;
    }

    public int get_user_id(String email)
    {
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {email};
        Cursor u_id=calorieDatabase.rawQuery("select id from users where email like ?",rowDetails);
        u_id.moveToFirst();
        calorieDatabase.close();
        return u_id.getInt(0);

    }
    public void add_meal(String email,String meal, Date d,double quantity,double cal)
    {
        ContentValues row = new ContentValues();
        row.put("user_id",this.get_user_id(email));
        row.put("meal",meal);
        row.put("date", String.valueOf(d));
        row.put("quantity",quantity);
        row.put("calories",cal);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.insert("user_meals",null,row);
        calorieDatabase.close();
    }
    public Cursor getUserMeals(String email){
        String id = this.get_user_id(email)+"";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id};
        Cursor meals=calorieDatabase.rawQuery("select meal,date,quantity,calories from user_meals where id like ?",rowDetails);
        if (meals != null)
            meals.moveToFirst();
        calorieDatabase.close();
        return meals;
    }
}
