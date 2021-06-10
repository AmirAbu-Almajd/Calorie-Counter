package com.example.calorie_counter;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Date DateWithoutTime;
    Double Cals;
    Double cals;
    Double fats;
    Double sugar;
    Double carbs;
    Double sodium;
    Double vitamin_c;
    JSONObject Json;
    int calories;
    String label;
    String name;
    String id;
    Database CaloriesDatabase;
    List<String> Items;
    Button add_meal;
    int textViewsIds;
    ArrayAdapter<String> adapter;
    int db_size = 0;
    EditText quantity;
    LineGraphSeries<DataPoint> series1;
    Double y;
    String x;
    Database db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance(String param1, String param2) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        /////////Salma's Start/////////
        LinearLayout mealsLayout = (LinearLayout) rootView.findViewById(R.id.userMealsLayout);
        AutoCompleteTextView auto = (AutoCompleteTextView) rootView.findViewById(R.id.search_txt);
        quantity = (EditText) rootView.findViewById(R.id.Quantity_txt);
        quantity.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        auto.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        fetch_meals(rootView);
//        WriteinDatabase();
        Items = new ArrayList<>();
        CaloriesDatabase = new Database(rootView.getContext());
        Items = CaloriesDatabase.get_items();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, Items);
        add_meal = (Button) rootView.findViewById(R.id.db_btn);
        //ListView responseList = (ListView) rootView.findViewById(R.id.meals_listview);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        auto.setAdapter(adapter);
        //responseList.setAdapter(listAdapter);
        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter.isEmpty()) {
                    OkHttpClient client = new OkHttpClient();
                    String Url = "https://api.spoonacular.com/food/ingredients/autocomplete?apiKey=331336ee4d01495083696ea9ef54d599&query=" + auto.getText() + "&number=10&metaInformation=true&intolerances=bug";
                    Request request = new Request.Builder()
                            .url(Url)
                            .addHeader("Content-Type", "application/json")
                            .get()
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            if (response.isSuccessful()) {
                                final String myResponse = response.body().string();
                                getActivity().runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void run() {

                                        String json = "{\"items\":" + myResponse + "}";
                                        try {
                                            JSONObject obj = new JSONObject(json);
                                            JSONArray items = obj.getJSONArray("items");
                                            for (int i = 0; i < items.length(); i++) {
                                                JSONObject nameObj = items.getJSONObject(i);
                                                name = nameObj.getString("name");
                                                id = nameObj.getString("id");
                                                CaloriesDatabase.insert_food_item(id, name);
                                                //Items=CaloriesDatabase.get_items();
                                                adapter.add(name);
                                                //adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, Items);
                                                adapter.notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                quantity.setHint("how many " + auto.getText() + "s did you eat");
                CaloriesDatabase = new Database(rootView.getContext());
                String id = CaloriesDatabase.get_item_id(auto.getText().toString());
                OkHttpClient client = new OkHttpClient();
                String Url = "https://api.spoonacular.com/food/ingredients/" + id + "/information?apiKey=331336ee4d01495083696ea9ef54d599&query=" + auto.getText().toString() + "&amount=100&unit=grams";
                Request request = new Request.Builder()
                        .url(Url)
                        .addHeader("Content-Type", "application/json")
                        .get()
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();
                            getActivity().runOnUiThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override

                                public void run() {
                                    CaloriesDatabase = new Database(rootView.getContext());
                                    try {
                                        JSONObject rootobj = new JSONObject(myResponse);
                                        String name = rootobj.getString("name");
                                        JSONObject innerobj = rootobj.getJSONObject("nutrition");
                                        JSONArray nutrients = innerobj.getJSONArray("nutrients");
                                        for (int i = 0; i < nutrients.length(); i++) {
                                            JSONObject arrobj = nutrients.getJSONObject(i);
                                            if (arrobj.getString("title").equals("Calories")) {
                                                calories = arrobj.getInt("amount");
                                            }

                                        }
                                        Cals = (double) calories;
                                        listAdapter.add(String.valueOf(Cals));
                                        write_to_file(String.valueOf(Cals));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });


            }

        });
        add_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaloriesDatabase = new Database(rootView.getContext());
                String id = CaloriesDatabase.get_item_id(auto.getText().toString());
                OkHttpClient client = new OkHttpClient();
                String Url = "https://api.spoonacular.com/food/ingredients/" + id + "/information?apiKey=331336ee4d01495083696ea9ef54d599&query=" + auto.getText().toString() + "&amount=100&unit=grams";
                Request request = new Request.Builder()
                        .url(Url)
                        .addHeader("Content-Type", "application/json")
                        .get()
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();
                            getActivity().runOnUiThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override

                                public void run() {
                                    String result = "";
                                    CaloriesDatabase = new Database(rootView.getContext());
                                    try {
                                        JSONObject rootobj = new JSONObject(myResponse);
                                        String name = rootobj.getString("name");
                                        JSONObject innerobj = rootobj.getJSONObject("nutrition");
                                        JSONArray nutrients = innerobj.getJSONArray("nutrients");
                                        for (int i = 0; i < nutrients.length(); i++) {
                                            JSONObject arrobj = nutrients.getJSONObject(i);
                                            if (arrobj.getString("title").equals("Calories")) {
                                                cals = arrobj.getDouble("amount");
                                            }
                                            if (arrobj.getString("title").equals("Fat")) {
                                                fats = arrobj.getDouble("amount");
                                            }
                                            if (arrobj.getString("title").equals("Sodium")) {
                                                sodium = arrobj.getDouble("amount");
                                            }
                                            if (arrobj.getString("title").equals("Net Carbohydrates")) {
                                                carbs = arrobj.getDouble("amount");
                                            }
                                            if (arrobj.getString("title").equals("Sugar")) {
                                                sugar = arrobj.getDouble("amount");
                                            }
                                            if (arrobj.getString("title").equals("Vitamin C")) {
                                                vitamin_c = arrobj.getDouble("amount");
                                            }

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    LocalDate dateNow = LocalDate.now();
                                    double from_file = read_from_file();
                                    double final_calories = from_file * Double.valueOf(quantity.getText().toString());
                                    CaloriesDatabase.add_meal(userSingleton.getId(), auto.getText().toString(), dateNow, Double.valueOf(quantity.getText().toString()), cals, fats, sugar, carbs, vitamin_c, sodium);
                                    fetch_meals(rootView);
//                                    int meals_count=CaloriesDatabase.get_meals_count();
                                    //Toast.makeText(getActivity(),meals_count,Toast.LENGTH_LONG).show();


                                }
                            });
                        }
                    }
                });
            }
        });


        /////////Salma's End/////////
        /////////setting date/////////
        db = new Database(getContext());
        TextView date_txt = rootView.findViewById(R.id.date_txt);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = sdf.format(c);
        LocalDate d = LocalDate.now();
        //date_txt.setText(d.toString());
        ////////calorie equation///////////
        calories_tracking(rootView, d);
        //////////////////graph//////////////////////////////
        draw_weight_graph(rootView, d);
        Button updateWeight = rootView.findViewById(R.id.updateBtnGraph);
        EditText weightTxt = rootView.findViewById(R.id.newWeightTxt);
        updateWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_today_weight_update() == true) {
                    Toast.makeText(getContext(), "you already updated your weight today", Toast.LENGTH_LONG).show();
                } else {
                    db.insert_weight_entry(userSingleton.getId(), Double.parseDouble(weightTxt.getText().toString()));
                    draw_weight_graph(rootView, d);
                }
            }
        });
        /////////////profile transition////////

        /*ImageButton profile_btn = rootView.findViewById(R.id.profile_btn);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(), profile.class);
                startActivity(i);
            }
        });*/


        return rootView;
    }


    public void WriteinDatabase() {
        CaloriesDatabase = new Database(getContext());
        OkHttpClient client = new OkHttpClient();
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String Url = "https://api.spoonacular.com/food/ingredients/autocomplete?apiKey=331336ee4d01495083696ea9ef54d599&query=" + String.valueOf(alphabet) + "&number=99&metaInformation=true&intolerances=egg";
            Request request = new Request.Builder()
                    .url(Url)
                    .addHeader("Content-Type", "application/json")
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void run() {

                                String json = "{\"items\":" + myResponse + "}";
                                try {
                                    JSONObject obj = new JSONObject(json);
                                    JSONArray items = obj.getJSONArray("items");
                                    for (int i = 0; i < items.length(); i++) {
                                        JSONObject nameObj = items.getJSONObject(i);
                                        name = nameObj.getString("name");
                                        id = nameObj.getString("id");
                                        CaloriesDatabase.insert_food_item(id, name);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public void write_to_file(String Cals) {
        FileOutputStream stream = null;
        try {
            stream = getActivity().openFileOutput("calories.txt", 0);
            stream.write(Cals.getBytes());
            //stream.write(System.getProperty("line.separator").getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    public Double read_from_file() {
        FileInputStream fileinputstream = null;
        try {
            fileinputstream = getActivity().openFileInput("calories.txt");
            InputStreamReader stream = new InputStreamReader(fileinputstream);
            BufferedReader br = new BufferedReader(stream);
            StringBuffer buffer = new StringBuffer();
            String lines;
            while ((lines = br.readLine()) != null) {
                buffer.append(lines);
            }
            Double calories = Double.valueOf(buffer.toString());
            return calories;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void calories_tracking(View rootView, LocalDate c) {
        db = new Database(getContext());
        TextView caloriesGoal_txt = rootView.findViewById(R.id.caloriesGoal_txt);
        TextView caloriesRemaining_txt = rootView.findViewById(R.id.caloriesRemaining_txt);
        double daily_calorie_intake = db.get_daily_intake(userSingleton.getId());
        double calories_consumed = db.get_calories_consumed(userSingleton.getId(), c);
        double calories_left = daily_calorie_intake - calories_consumed;
        caloriesGoal_txt.setText(daily_calorie_intake+"");
        caloriesRemaining_txt.setText(calories_left+"");
        //caloriesEqu_txt.setText("       " + calories_consumed + "         +         " + calories_left + "         =         " + String.valueOf(daily_calorie_intake));
        ////////////////////// progress Bar//////////////////
        ProgressBar simpleProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar4);
        TextView text = (TextView) rootView.findViewById(R.id.textView4);
        double resultingPercentage = (calories_consumed / daily_calorie_intake) * 100;
        String resultingPercentageStr = String.format("%.2f", resultingPercentage);
        simpleProgressBar.setProgress((int) resultingPercentage);
//        simpleProgressBar.setProgressTintList(ColorStateList.valueOf(0xFF89cff0));
        Log.e("henaaa",((int) resultingPercentage)+"");
        text.setText(resultingPercentageStr + "%");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fetch_meals(View rootView) {
        LocalDate dateNow = LocalDate.now();
        LinearLayout mealsLayout = (LinearLayout) rootView.findViewById(R.id.userMealsLayout);
        textViewsIds = 0;
        mealsLayout.removeAllViews();
        int id = userSingleton.getId();

        CaloriesDatabase = new Database(getContext());
        Cursor cursor = CaloriesDatabase.getTodayMeals(id, dateNow);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(15, 15, 15, 15);
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            TextView listItems = new TextView(rootView.getContext());
            listItems.setBackgroundColor(0xFF89cff0);
            listItems.setPadding(15, 15, 15, 15);
            listItems.setLayoutParams(params);
            listItems.setId(textViewsIds + 3000);
            listItems.setTextSize(18);
            textViewsIds++;
            String anItem = "Item:   " + cursor.getString(0) +
                    "\nQuantity:   " + cursor.getDouble(1) +
                    "\nCalories:   " + cursor.getDouble(2);
            listItems.setText(anItem);
            mealsLayout.addView(listItems);
            cursor.moveToNext();
        }
        calories_tracking(rootView,dateNow);


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void draw_weight_graph(View rootView, LocalDate sdf) {
        GraphView graphView = (GraphView) rootView.findViewById(R.id.weightGraph);
        Cursor weights = db.get_user_weights(userSingleton.getId());
        weights.moveToFirst();
        DataPoint[] datapoints = new DataPoint[weights.getCount()];
        for (int i = 0; i < weights.getCount(); i++) {
            LocalDate localDate = new LocalDate(weights.getString(1));
            Date date = localDate.toDateTimeAtStartOfDay().toDate();
            datapoints[i] = new DataPoint(date, weights.getDouble(0));
            weights.moveToNext();
        }

        series1 = new LineGraphSeries<>(datapoints);
        graphView.addSeries(series1);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.toString();
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    public boolean check_today_weight_update() {
        LocalDate d = db.get_last_weight_update_date(userSingleton.getId());
        if (LocalDate.now().equals(d))
            return true;
        else
            return false;
    }
}