package com.example.calorie_counter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class loginMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ///////uncomment to clear database/////
      //getApplicationContext().deleteDatabase("CalorieDB15");

        EditText emailtxt = (EditText)findViewById(R.id.emailTxt);
        EditText password  = (EditText)findViewById(R.id.passwordTxt);
        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        Button registerBtn = (Button)findViewById(R.id.registerBtn);
        Database db = new Database(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = db.signIn(emailtxt.getText().toString(),password.getText().toString());
                userSingleton.setId(id);
//                Toast.makeText(getApplicationContext(),id+"",Toast.LENGTH_LONG).show();
                Intent i = new Intent(loginMenu.this,mainTabs.class);
                startActivity(i);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(loginMenu.this,registerationMenu.class);
                startActivity(i);
            }
        });
    }

}