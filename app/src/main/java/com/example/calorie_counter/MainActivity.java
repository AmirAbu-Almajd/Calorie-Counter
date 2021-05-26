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

public class MainActivity extends AppCompatActivity {
    String tempTxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText nametxt = (EditText) findViewById(R.id.nametxt);
        EditText emailtxt = (EditText) findViewById(R.id.emailtxt);
        EditText passwordtxt = (EditText) findViewById(R.id.passwordtxt);
        EditText agetxt = (EditText) findViewById(R.id.agetxt);
        EditText heighttxt = (EditText) findViewById(R.id.heighttxt);
        EditText weighttxt = (EditText) findViewById(R.id.weighttxt);
        EditText gendertxt = (EditText) findViewById(R.id.gendertxt);
        EditText daily_intaketxt = (EditText) findViewById(R.id.daily_intaketxt);
        ArrayAdapter<String> autoComplete = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        AutoCompleteTextView apiText = (AutoCompleteTextView) findViewById(R.id.apiTxt);
        apiText.setAdapter(autoComplete);
        Button submitbtn = (Button) findViewById(R.id.submitBtn);
        Button showUsersbtn = (Button) findViewById(R.id.showUsers);
        Button showMealsForm = (Button) findViewById(R.id.mealsIntentBtn);
        apiText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean count = false;
                List<String> added;
                char h = '\"';
                String text = "";
                hatWkhod(apiText.getText().toString());

                if (tempTxt.length() > 4) {

                    for (int i = 0; i < tempTxt.length(); i++) {
                        if (i - 14 < tempTxt.length()) {

                            if (tempTxt.startsWith("shrt_desc", i)) {
                                i += 12;
                                count = true;
                            }
                        }
                        if (count && tempTxt.charAt(i) ==  h) {
                            autoComplete.add(text);
                            autoComplete.notifyDataSetChanged();
                            Log.e("Item", text);
                            text = "";
                            count = false;
                        }
                        if (count) {
                            text += tempTxt.charAt(i);
                        }

                    }
                }
                return false;
            }
        });
        final Database calorieDB = new Database(this);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieDB.createUser(nametxt.getText().toString(), emailtxt.getText().toString(), passwordtxt.getText().toString(),
                        Integer.parseInt(agetxt.getText().toString()), Double.parseDouble(heighttxt.getText().toString()),
                        Double.parseDouble(weighttxt.getText().toString()), gendertxt.getText().toString(),
                        Integer.parseInt(daily_intaketxt.getText().toString()));
                Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_LONG).show();
            }
        });
        showUsersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, show_users.class);
                startActivity(i);
            }
        });

        showMealsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = calorieDB.signIn(emailtxt.getText().toString(), passwordtxt.getText().toString());
                Intent i = new Intent(MainActivity.this, meal.class);
                i.putExtra("email", emailtxt.getText().toString());
                i.putExtra("userId", userId + "");
                startActivity(i);

            }
        });


    }

    public void hatWkhod(String chunk) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://food-calorie-data-search.p.rapidapi.com/api/search?keyword=" + chunk)
                .get()
                .addHeader("x-rapidapi-key", "fb4929fb92msh28c036ea7054815p18fef4jsn203032cd0433")
                .addHeader("x-rapidapi-host", "food-calorie-data-search.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tempTxt = myResponse;
                        }
                    });
                }
            }
        });
    }

}