package com.example.calorie_counter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static String databaseName = "CalorieDB5";

    SQLiteDatabase calorieDatabase;

    public Database(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(id integer primary key, name text not null, email text unique not null, password text not null " +
                ", age integer , height real, weight real , gender text , daily_intake integer )");
        db.execSQL("create table user_meals(user_id integer , meal text , date Date,quantity real, calories real," +
                "PRIMARY KEY(user_id,meal,date),CONSTRAINT fk_col FOREIGN KEY(user_id) REFERENCES users(id))");
        db.execSQL("create table lists_items(list_id integer not null, item text , quantity real," +
                "PRIMARY KEY(list_id,item) " +
                ", CONSTRAINT fk_col FOREIGN KEY(list_id) REFERENCES user_lists_ids(list_id))");
        db.execSQL("create table user_lists_ids(user_id integer , list_id integer, PRIMARY KEY(user_id,list_id)," +
                "CONSTRAINT fk_col FOREIGN KEY(user_id) REFERENCES users(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists user_meals");
        db.execSQL("drop table if exists user_lists");
        onCreate(db);
    }

    public void createUser(String name, String email, String password, int age, double height,
                           double weight, String gender, double daily_intake) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("email", email);
        row.put("password", password);
        row.put("age", age);
        row.put("height", height);
        row.put("weight", weight);
        row.put("gender", gender);
        row.put("daily_intake", daily_intake);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.insert("users", null, row);
        calorieDatabase.close();
    }

    public Cursor getAllUsers() {
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {"name", "email", "password"};
        Cursor cursor = calorieDatabase.query("users", rowDetails, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        calorieDatabase.close();
        return cursor;
    }

    public int get_user_id(String email) {
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {email};
        Cursor u_id = calorieDatabase.rawQuery("select id from users where email like ?", rowDetails);
        u_id.moveToFirst();
        calorieDatabase.close();
        return u_id.getInt(0);

    }

    public void add_meal(String email, String meal, Date d, double quantity, double cal) {
        ContentValues row = new ContentValues();
        row.put("user_id", this.get_user_id(email));
        row.put("meal", meal);
        row.put("date", String.valueOf(d));
        row.put("quantity", quantity);
        row.put("calories", cal);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.insert("user_meals", null, row);
        calorieDatabase.close();
    }

    public void add_grocery_list(int user_id, List<String> items, List<Double> quantities) {
        calorieDatabase = getReadableDatabase();
        int listId = 0;
        Cursor userLists = calorieDatabase.rawQuery("select MAX(list_id) from user_lists_ids",null);
        userLists.moveToFirst();
        if(userLists.getCount()!=0){
            listId = userLists.getInt(0)+1;
        }
        calorieDatabase = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("user_id", user_id);
        row.put("list_id", listId);
        calorieDatabase.insert("user_lists_ids", null, row);

        for (int i = 0; i < items.size(); i++) {
            ContentValues row2 = new ContentValues();
            row2.put("list_id", listId);
            row2.put("item", items.get(i));
            row2.put("quantity", quantities.get(i));
            calorieDatabase.insert("lists_items", null, row2);
        }

        calorieDatabase.close();
    }

    public List<Cursor> get_user_lists(int user_id){
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {user_id+""};
        List<Cursor> user_lists = new LinkedList<Cursor>();
        Cursor user_lists_ids = calorieDatabase.rawQuery("select list_id from user_lists_ids " +
                "where user_id like ?",rowDetails);
        user_lists_ids.moveToFirst();
        while (!user_lists_ids.isAfterLast()){
            Log.e("Size",user_lists_ids.getCount()+"");
            String[] rowDetails2 = {user_lists_ids.getInt(0)+""};
            Cursor list = calorieDatabase.rawQuery("select item, quantity from lists_items where list_id like ?",rowDetails2);
            user_lists.add(list);
            user_lists_ids.moveToNext();
        }
        return user_lists;
    }
    public void delete_list(int id){
        calorieDatabase = getWritableDatabase();
        calorieDatabase.rawQuery("delete from user_lists_ids where user_id = "+id,null);
        calorieDatabase.close();
    }

    public Cursor getUserMeals(String email) {
        String id = this.get_user_id(email) + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id};
        Cursor meals = calorieDatabase.rawQuery("select meal,date,quantity,calories from user_meals where id like ?", rowDetails);
        if (meals != null)
            meals.moveToFirst();
        calorieDatabase.close();
        return meals;
    }

    public int signIn(String emailIn, String passwordIn) {
//        emailIn = "adham@gmail.com";
//        passwordIn = "121313";
        String[] rowDetails = {emailIn, passwordIn};
        calorieDatabase = getReadableDatabase();
        Cursor id = calorieDatabase.rawQuery("select id from users where email like ? and password like ?", rowDetails);
        if (id != null)
            id.moveToFirst();
        calorieDatabase.close();
        return id.getInt(0);
    }
}
