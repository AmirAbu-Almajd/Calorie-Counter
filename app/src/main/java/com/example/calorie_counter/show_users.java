package com.example.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class show_users extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);
        ListView usersList = (ListView)findViewById(R.id.usersList);
        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        usersList.setAdapter(userAdapter);
        final Database calorieDB = new Database(this);
        Cursor res = calorieDB.getAllUsers();
        while (!res.isAfterLast()){
            userAdapter.add(res.getString(0)+"\n"+res.getString(1)+"\n"+res.getString(2));
            res.moveToNext();
        }
        userAdapter.notifyDataSetChanged();
    }
}