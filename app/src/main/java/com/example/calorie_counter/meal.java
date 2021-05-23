package com.example.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class meal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(today);
        final Database calorieDB = new Database(this);
        EditText mealtxt = (EditText)findViewById(R.id.mealtxt);
        EditText quantitytxt = (EditText)findViewById(R.id.quantitytxt);
        EditText caloriestxt = (EditText)findViewById(R.id.caloriestxt);
        Button addMealBtn = (Button)findViewById(R.id.addMealBtn);
        Button showMealsBtn = (Button)findViewById(R.id.showMealBtn);
        ListView mealsList = (ListView)findViewById(R.id.mealsList);
        ArrayAdapter<String> mealsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        mealsList.setAdapter(mealsAdapter);
        addMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieDB.add_meal(getIntent().getExtras().getString("email"),
                        mealtxt.getText().toString(),
                        today,Double.parseDouble(quantitytxt.getText().toString()),
                        Double.parseDouble(caloriestxt.getText().toString()));
                Toast.makeText(getApplicationContext(),"Meal added Successfully",Toast.LENGTH_LONG).show();
            }
        });
        showMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = calorieDB.getUserMeals(getIntent().getExtras().getString("email"));
                while (!res.isAfterLast()){
                    mealsAdapter.add(res.getString(0)+"\n"+res.getString(1)+"\n"+res.getDouble(2));
                    res.moveToNext();
                }
                mealsAdapter.notifyDataSetChanged();
            }
        });

    }
}