package com.example.calorie_counter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_main_menu, container, false);
        Database db = new Database(getContext());
        ListView meals_list=rootView.findViewById(R.id.meals_listview);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        meals_list.setAdapter(adapter);
        TextView caloriesEqu_txt=rootView.findViewById(R.id.caloriesEqu_txt);
        TextView date_txt=rootView.findViewById(R.id.date_txt);
        int daily_calorie_intake=db.get_daily_intake(userSingleton.getId());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = sdf.format(c);
        date_txt.setText(currentDate);
        db.add_meal(userSingleton.getId(),"oranges", c, 1, 50);
        db.add_meal(userSingleton.getId(),"banana", c, 1, 100);
        int calories_consumed= db.get_calories_consumed(userSingleton.getId(),c);
        int calories_left= daily_calorie_intake-calories_consumed;
        caloriesEqu_txt.setText("       "+calories_consumed+"         +         "+calories_left+"         =         "+String.valueOf(daily_calorie_intake));

        Cursor c_meals=db.getTodayMeals(userSingleton.getId(),c);
        while (!c_meals.isAfterLast())
        {
            adapter.add(c_meals.getString(0));
            //adapter.add(c_meals.getString(1));
            //adapter.add(c_meals.getString(2));
            c_meals.moveToNext();
        }

        ImageButton profile_btn=rootView.findViewById(R.id.profile_btn);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(),profile.class);
                startActivity(i);
            }
        });
        return rootView;
    }
}