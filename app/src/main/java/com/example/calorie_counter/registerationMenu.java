package com.example.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class registerationMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_menu);

        EditText nametxt = (EditText) findViewById(R.id.nameTxt);
        EditText emailtxt = (EditText) findViewById(R.id.emailtxtRegisteration);
        EditText passwordtxt = (EditText) findViewById(R.id.passwordRegisteration);
        EditText agetxt = (EditText) findViewById(R.id.ageTxt);
        EditText heighttxt = (EditText) findViewById(R.id.heightTxt);
        EditText weighttxt = (EditText) findViewById(R.id.weightTxt);
        RadioGroup genderRadio = (RadioGroup) findViewById(R.id.genderRadio);
        Button registerbtn = (Button) findViewById(R.id.registerBtnRegisteration);

        Database db = new Database(this);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = genderRadio.getCheckedRadioButtonId();
                RadioButton genderBtn = (RadioButton) findViewById(selectedGenderId);
                String gender = genderBtn.getText().toString();
                int age = Integer.parseInt(agetxt.getText().toString());

                Double weight = Double.parseDouble(weighttxt.getText().toString());
                Double height = Double.parseDouble(heighttxt.getText().toString());
                Double calorie_calculation = userSingleton.cal_dailyIntake(weight,height,gender,age);
                Log.e("Calories : ", calorie_calculation+"");
                db.createUser(nametxt.getText().toString(), emailtxt.getText().toString(), passwordtxt.getText().toString(),
                        age, height, weight, genderBtn.getText().toString(), calorie_calculation);
                int id = db.get_user_id(emailtxt.getText().toString());
                userSingleton.setId(id);
                Intent i = new Intent(registerationMenu.this,mainTabs.class);

                startActivity(i);
            }
        });


    }
}