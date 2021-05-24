package com.example.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText nametxt = (EditText)findViewById(R.id.nametxt);
        EditText emailtxt = (EditText)findViewById(R.id.emailtxt);
        EditText passwordtxt = (EditText)findViewById(R.id.passwordtxt);
        EditText agetxt = (EditText)findViewById(R.id.agetxt);
        EditText heighttxt = (EditText)findViewById(R.id.heighttxt);
        EditText weighttxt = (EditText)findViewById(R.id.weighttxt);
        EditText gendertxt = (EditText)findViewById(R.id.gendertxt);
        EditText daily_intaketxt = (EditText)findViewById(R.id.daily_intaketxt);
        Button submitbtn = (Button)findViewById(R.id.submitBtn);
        Button showUsersbtn = (Button)findViewById(R.id.showUsers);
        Button showMealsForm = (Button)findViewById(R.id.mealsIntentBtn);

        final Database calorieDB = new Database(this);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieDB.createUser(nametxt.getText().toString(),emailtxt.getText().toString(),passwordtxt.getText().toString(),
                        Integer.parseInt(agetxt.getText().toString()),Double.parseDouble(heighttxt.getText().toString()),
                        Double.parseDouble(weighttxt.getText().toString()),gendertxt.getText().toString(),
                        Integer.parseInt(daily_intaketxt.getText().toString()));
                Toast.makeText(getApplicationContext(),"User added successfully",Toast.LENGTH_LONG).show();
            }
        });
        showUsersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,show_users.class);
                startActivity(i);
            }
        });

        showMealsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = calorieDB.signIn(emailtxt.getText().toString(),passwordtxt.getText().toString());
                Intent i = new Intent(MainActivity.this,meal.class);
                i.putExtra("email",emailtxt.getText().toString());
                i.putExtra("userId",userId+"");
                startActivity(i);

            }
        });
    }
}