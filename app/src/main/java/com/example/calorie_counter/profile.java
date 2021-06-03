package com.example.calorie_counter;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Database db = new Database(this);
        EditText name_txt_update=findViewById(R.id.name_txt_update);
        EditText email_txt_update=findViewById(R.id.email_txt_update);
        EditText password_txt_update=findViewById(R.id.password_txt_update);
        EditText age_txt_update=findViewById(R.id.age_txt_update);
        EditText height_txt_update=findViewById(R.id.height_txt_update);
        TextView weight_txt_update=findViewById(R.id.weight_txt_update);
        Button update_btn=findViewById(R.id.update_btn);

        Cursor user=db.get_user_info(userSingleton.getId());
        name_txt_update.setText(user.getString(0));
        email_txt_update.setText(user.getString(1));
        password_txt_update.setText(user.getString(2));
        age_txt_update.setText(user.getString(3));
        height_txt_update.setText(user.getString(4));
        weight_txt_update.setText("Weight = "+user.getDouble(5)+" Kg");
        String gender=user.getString(6);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.update_profile(userSingleton.getId(),name_txt_update.getText().toString(),email_txt_update.getText().toString()
                        ,password_txt_update.getText().toString(),Integer.parseInt(age_txt_update.getText().toString()),
                        Double.parseDouble(height_txt_update.getText().toString()),user.getDouble(5),
                        gender);
                Toast.makeText(getApplicationContext(),"Profile info updated",Toast.LENGTH_LONG).show();
            }
        });

    }
}