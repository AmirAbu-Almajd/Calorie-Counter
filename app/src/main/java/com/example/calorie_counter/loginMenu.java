package com.example.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class loginMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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