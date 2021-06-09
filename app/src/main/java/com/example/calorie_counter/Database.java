package com.example.calorie_counter;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import org.joda.time.LocalDate;


import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static String databaseName = "CalorieDB15";
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
        db.execSQL("create table user_weights(user_id integer not null , weight real , date LocalDate, " +
                "PRIMARY KEY(user_id,date),CONSTRAINT fk_col FOREIGN KEY(user_id) REFERENCES users(id))");
        db.execSQL("create table user_water_intake(user_id integer not null , cups_of_water integer , date LocalDate, target_cups integer, " +
                "PRIMARY KEY(user_id,date),CONSTRAINT fk_col FOREIGN KEY(user_id) REFERENCES users(id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists user_meals");
        db.execSQL("drop table if exists lists_items");
        db.execSQL("drop table if exists user_lists_ids");
        db.execSQL("drop table if exists user_weights");
        db.execSQL("drop table if exists user_water_intake");
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
        ////////insert temp weight records for graph(clear dataset first)//////
        /*LocalDate c_yesterday=LocalDate.now();
        c_yesterday.minusDays(3);
        insert_weight_entry_temp(get_user_id(email),55,c_yesterday);
        c_yesterday.minusDays(3);
        insert_weight_entry_temp(get_user_id(email),50,c_yesterday);*/
        insert_weight_entry(get_user_id(email),weight);
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

    public void add_meal(int id, String meal, Date d, double quantity, double cal) {
        ContentValues row = new ContentValues();
        row.put("user_id", id);
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

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public List<Pair<Integer,Cursor>> get_user_lists(int user_id){
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {user_id+""};
        List<Pair<Integer,Cursor>> user_lists = new LinkedList<Pair<Integer,Cursor>>();
        Cursor user_lists_ids = calorieDatabase.rawQuery("select list_id from user_lists_ids " +
                "where user_id like ?",rowDetails);
        user_lists_ids.moveToFirst();
        while (!user_lists_ids.isAfterLast()){
            Log.e("Size",user_lists_ids.getCount()+"");
            String[] rowDetails2 = {user_lists_ids.getInt(0)+""};
            Cursor list = calorieDatabase.rawQuery("select item, quantity from lists_items where list_id like ?",rowDetails2);
            Pair<Integer,Cursor> pairList = new Pair<Integer, Cursor>(user_lists_ids.getInt(0),list);
            user_lists.add(pairList);
            user_lists_ids.moveToNext();
        }
        return user_lists;
    }
    public void delete_list(int id , int list_id){
        calorieDatabase = getWritableDatabase();
        String[] rowDetails = {list_id+""};
        String[] rowDetails2 = {id+"", list_id+""};
        calorieDatabase.delete("lists_items",  "list_id"+ "=?", new String[]{list_id+""});
        calorieDatabase.delete("user_lists_ids",  "user_id=? and list_id=?", new String[]{id+"",list_id+""});
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

    public int get_daily_intake(int id_in)
    {
        String id = id_in + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id};
        Cursor u_daily_intake = calorieDatabase.rawQuery("select daily_intake from users where id like ?", rowDetails);
        u_daily_intake.moveToFirst();
        calorieDatabase.close();
        return u_daily_intake.getInt(0);
    }

    public int get_calories_consumed(int id_in,Date date_in)
    {
        int calories=0;
        String id = id_in + "";
        String date = date_in + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id,date};
        Cursor meals = calorieDatabase.rawQuery("select calories from user_meals where user_id like ? and date like ?", rowDetails);
        meals.moveToFirst();
        if (meals != null)
        {
            meals.moveToFirst();
            while (!meals.isAfterLast()) {
                calories+=Integer.parseInt(meals.getString(0));
                meals.moveToNext();
            }
        }
        calorieDatabase.close();
        return calories;
    }

    public void update_profile(int id_in,String name, String email, String password, int age, double height,
                               double weight,String gender)
    {
        String id = id_in + "";
        Double daily_intake=userSingleton.cal_dailyIntake(weight,height,gender,age);
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("email", email);
        row.put("password", password);
        row.put("age", age);
        row.put("height", height);
        row.put("weight", weight);
        row.put("daily_intake", daily_intake);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.update("users",row,"id like ?",new String[]{id});
        calorieDatabase.close();
    }

    public Cursor get_user_info(int id)
    {
        calorieDatabase = getReadableDatabase();
        Cursor cursor = calorieDatabase.rawQuery("select name,email,password,age,height,weight,gender from users where id like ? ",new String[]{id+""});
        if (cursor != null)
            cursor.moveToFirst();
        calorieDatabase.close();
        return cursor;
    }

    public Cursor getTodayMeals(int id_in ,Date date_in) {
        String id =id_in + "";
        String date = date_in + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id,date};
        Cursor meals = calorieDatabase.rawQuery("select meal,quantity,calories from user_meals where user_id like ? and date like ?", rowDetails);
        if (meals != null)
            meals.moveToFirst();
        calorieDatabase.close();
        return meals;
    }

    public void update_weight(int id_in,double newWeight)
    {
        String id =id_in + "";
        ContentValues row2 = new ContentValues();
        row2.put("weight", newWeight);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.update("users",row2,"id like ?",new String[]{id});
        calorieDatabase.close();
    }

    public Cursor get_user_weights(int id_in){

        String id =id_in + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id};
        Cursor weights = calorieDatabase.rawQuery("select weight,date from user_weights where user_id like ? ", rowDetails);
        if (weights != null)
            weights.moveToFirst();
        calorieDatabase.close();
        return weights;
    }

    public void insert_weight_entry(int id_in,double newWeight)
    {
        String id =id_in + "";
        ContentValues row = new ContentValues();
        row.put("user_id", id);
        row.put("date", String.valueOf(LocalDate.now()));
        row.put("weight", newWeight);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.insert("user_weights", null, row);
    }

    public LocalDate get_last_weight_update_date(int id_in)
    {
        String id =id_in + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id};
        Cursor lastDate = calorieDatabase.rawQuery("select max(date) from user_weights where user_id like ? ", rowDetails);
        if (lastDate != null)
            lastDate.moveToFirst();
        calorieDatabase.close();
        return new LocalDate(lastDate.getString(0));
    }

    public void insert_weight_entry_temp(int id_in,double newWeight,LocalDate c)
    {
        String id =id_in + "";
        ContentValues row = new ContentValues();
        row.put("user_id", id);
        row.put("date", c.toString());
        row.put("weight", newWeight);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.insert("user_weights", null, row);
    }

    /*public boolean check_date_exist(int id_in,Date d)
    {
        String id =id_in + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id,d.toString()};
        Cursor lastDate = calorieDatabase.rawQuery("select weight from user_weights where user_id like ? and date like ? ", rowDetails);
        calorieDatabase.close();
        if (lastDate != null)
            return true;
        else
            return false;


    }*/

    public int get_waterCups(int id_in, LocalDate date) {
        String id = id_in + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id, date.toString()};
        Cursor waterCups = calorieDatabase.rawQuery("select cups_of_water from user_water_intake where user_id like ? and date like ? ", rowDetails);
        if(waterCups.getCount()==0)
        {
            ContentValues row = new ContentValues();
            row.put("user_id", id);
            row.put("date", date.toString());
            row.put("cups_of_water", 0);
            row.put("target_cups", userSingleton.cal_waterIntake(get_user_weight(id_in)));
            calorieDatabase = getWritableDatabase();
            calorieDatabase.insert("user_water_intake", null, row);
            return 0;
        }
        if (waterCups != null) {
            waterCups.moveToFirst();
        }
        calorieDatabase.close();
        return waterCups.getInt(0);
    }

    public void update_water_intake(int id_in, int cups, LocalDate d)
    {
        String id = id_in + "";
        ContentValues row2 = new ContentValues();
        row2.put("cups_of_water", cups);
        calorieDatabase = getWritableDatabase();
        calorieDatabase.update("user_water_intake",row2,"user_id like ? and date like ?",new String[]{id,d.toString()});
        calorieDatabase.close();
    }
    public double get_user_weight(int id_in)
    {
        calorieDatabase = getReadableDatabase();
        Cursor cursor = calorieDatabase.rawQuery("select weight from users where id like ? ",new String[]{id_in+""});
        if (cursor != null)
            cursor.moveToFirst();
        calorieDatabase.close();
        return cursor.getDouble(0);
    }
    public int get_water_goal(int id_in,LocalDate date)
    {
        String id = id_in + "";
        calorieDatabase = getReadableDatabase();
        String[] rowDetails = {id, date.toString()};
        Cursor waterCups = calorieDatabase.rawQuery("select target_cups from user_water_intake where user_id like ? and date like ? ", rowDetails);
        if (waterCups != null) {
            waterCups.moveToFirst();
        }
        calorieDatabase.close();
        return waterCups.getInt(0);
    }
}
