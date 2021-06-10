package com.example.calorie_counter;

import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NutritionsFragment extends Fragment {
    JSONObject Json;
    String Name;
    String unit;
    Double value;
    nutrients nutrition;
    String label;
    String name;
    String id;
    Database CaloriesDatabase;
    List<String> Items;
    Button database_btn;
    ArrayAdapter<String> adapter;
    int db_size=0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NutritionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaloriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NutritionsFragment newInstance(String param1, String param2) {
        NutritionsFragment fragment = new NutritionsFragment();
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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nutritions, container, false);
        CaloriesDatabase = new Database(rootView.getContext());
        AutoCompleteTextView auto = (AutoCompleteTextView) rootView.findViewById(R.id.search_txt);
        TextView itemTitle = (TextView) rootView.findViewById(R.id.itemNameTxt);
        auto.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
        Items= new ArrayList<>();
        Items=CaloriesDatabase.get_items();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, Items);
        ListView responseList = (ListView) rootView.findViewById(R.id.listView1);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        auto.setAdapter(adapter);
        responseList.setAdapter(listAdapter);
        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(adapter.isEmpty())
                {
                    Log.e("here","if condition");
                    OkHttpClient client = new OkHttpClient();
                    String Url = "https://api.spoonacular.com/food/ingredients/autocomplete?apiKey=56f4af11592a48e5b0441f8d1474ac1e&query=" + auto.getText() + "&number=10&metaInformation=true&intolerances=bug";
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
            public void afterTextChanged(Editable editable) {
            }
        });
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                String id =CaloriesDatabase.get_item_id(auto.getText().toString());
                String tempWord = auto.getText().toString();
                String charC = tempWord.substring(0,1);
                charC = charC.toUpperCase();
                String rest = tempWord.substring(1,tempWord.length());
                itemTitle.setText("Nutrition facts of 100 grams of " + charC+rest);

//                itemTitle.setText("auto.getText().toString()");
                OkHttpClient client = new OkHttpClient();
                String Url = "https://api.spoonacular.com/food/ingredients/"+id +"/information?apiKey=56f4af11592a48e5b0441f8d1474ac1e&query="+auto.getText().toString()+"&amount=100&unit=grams";
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
                                    String result="";
                                    CaloriesDatabase = new Database(rootView.getContext());
                                    try {
                                        JSONObject rootobj= new JSONObject(myResponse);
                                        String name= rootobj.getString("name");
                                        JSONObject innerobj= rootobj.getJSONObject("nutrition");
                                        JSONArray nutrients= innerobj.getJSONArray("nutrients");
                                        for(int i=0; i<nutrients.length(); i++)
                                        {
                                            JSONObject arrobj= nutrients.getJSONObject(i);
                                            if(arrobj.getString("title").equals("Calories") ||
                                                    arrobj.getString("title").equals("Fat") ||
                                                    arrobj.getString("title").equals("Sodium") ||
                                                    arrobj.getString("title").equals("Net Carbohydrates") ||
                                                    arrobj.getString("title").equals("Sugar") ||
                                                    arrobj.getString("title").equals("Vitamin C"))
                                            {
                                                Name=arrobj.getString("title");
                                                value=arrobj.getDouble("amount");
                                                unit= arrobj.getString("unit");
                                                result=Name+ "    "+ value+ "    " + unit;
                                                listAdapter.add(result);
                                                listAdapter.notifyDataSetChanged();
                                                auto.getText().clear();
                                                userSingleton.hideSoftKeyboard(getActivity());
                                            }
                                        }
                                        responseList.setAdapter(listAdapter);

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
        return rootView;
    }
}
