package com.example.calorie_counter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.joda.time.DateTimeComparator;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        db = new Database(getContext());

        /////////setting date/////////
        TextView date_txt = rootView.findViewById(R.id.date_txt);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = sdf.format(c);
        date_txt.setText(currentDate);
        ////////calorie equation///////////
        calories_tracking(rootView,c);
        ///////////////////////uncomment to insert trail weights///////////////////////////
        /*Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -5);
        Date c_tomorrow = cal.getTime();
        db.insert_weight_entry_temp(userSingleton.getId(),55,c_tomorrow);
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        c_tomorrow = cal.getTime();
        db.insert_weight_entry_temp(userSingleton.getId(),50,c_tomorrow);*/
        //////////////////graph//////////////////////////////
        draw_weight_graph( rootView, sdf);
        Button updateWeight = rootView.findViewById(R.id.updateBtnGraph);
        EditText weightTxt = rootView.findViewById(R.id.newWeightTxt);
        updateWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_today_weight_update()==true)
                {
                    Toast.makeText(getContext(),"you already updated your weight today",Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.insert_weight_entry(userSingleton.getId(),Double.parseDouble(weightTxt.getText().toString()));
                    draw_weight_graph( rootView, sdf);
                }
            }
        });
        /*
        Cursor weights = db.get_user_weights(userSingleton.getId());
                series1 = new LineGraphSeries<>();
                weights.moveToFirst();
                while (!weights.isAfterLast()) {
                    x = weights.getString(1);
                    y = weights.getDouble(0);
                    try {
                        series1.appendData(new DataPoint(new SimpleDateFormat("dd/MM/yyyy").parse(x), y), true, 100);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    weights.moveToNext();
                }
                graphView.addSeries(series1);
        while (!weights.isAfterLast()) {
            x = weights.getString(1);
            y = weights.getDouble(0);
            try {
                series1.appendData(new DataPoint(new SimpleDateFormat("dd/MM/yyyy").parse(x), y), true, 100);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            weights.moveToNext();
        }*/

        /////////////profile transition////////
        ImageButton profile_btn = rootView.findViewById(R.id.profile_btn);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(), profile.class);
                startActivity(i);
            }
        });
        return rootView;
    }

    public void calories_tracking(View rootView,Date c)
    {
        TextView caloriesEqu_txt = rootView.findViewById(R.id.caloriesEqu_txt);
        int daily_calorie_intake = db.get_daily_intake(userSingleton.getId());
        int calories_consumed = db.get_calories_consumed(userSingleton.getId(), c);
        int calories_left = daily_calorie_intake - calories_consumed;
        caloriesEqu_txt.setText("       " + calories_consumed + "         +         " + calories_left + "         =         " + String.valueOf(daily_calorie_intake));
        // ListView meals_list = rootView.findViewById(R.id.meals_listview);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        //meals_list.setAdapter(adapter);
        //db.add_meal(userSingleton.getId(), "oranges", c, 1, 50);
        //db.add_meal(userSingleton.getId(), "banana", c, 1, 100);
        /*Cursor c_meals = db.getTodayMeals(userSingleton.getId(), c);
        while (!c_meals.isAfterLast()) {
            adapter.add(c_meals.getString(0));
            //adapter.add(c_meals.getString(1));
            //adapter.add(c_meals.getString(2));
            c_meals.moveToNext();
        }*/
    }

    public void draw_weight_graph(View rootView,SimpleDateFormat sdf)
    {
        GraphView graphView = (GraphView) rootView.findViewById(R.id.weightGraph);
        Cursor weights = db.get_user_weights(userSingleton.getId());
        weights.moveToFirst();
        DataPoint[] datapoints=new DataPoint[weights.getCount()];
        for(int i=0;i<weights.getCount();i++)
        {
            datapoints[i]=new DataPoint(new Date(weights.getString(1)),weights.getDouble(0));
            weights.moveToNext();
        }

        series1 = new LineGraphSeries<>(datapoints);
        graphView.addSeries(series1);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    public boolean check_today_weight_update()
    {
        /*Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        Date c_tomorrow = cal.getTime();*/
        //LocalDate today=LocalDate.now();
        Date today = Calendar.getInstance().getTime();
        Date d=db.get_last_weight_update_date(userSingleton.getId());
        if(DateTimeComparator.getDateOnlyInstance().compare(today,d)==0)
            return true;
        else
            return false;
    }
}