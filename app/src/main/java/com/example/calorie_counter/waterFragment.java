package com.example.calorie_counter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.joda.time.LocalDate;

import org.joda.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link waterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class waterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Database db;
    float current_cups;
    int progress=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public waterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment waterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static waterFragment newInstance(String param1, String param2) {
        waterFragment fragment = new waterFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_water, container, false);
        db = new Database(getContext());

        TextView cupsOfWater_txt = rootView.findViewById(R.id.cupsOfWater_txt);
        current_cups=db.get_waterCups(userSingleton.getId(),LocalDate.now());
        cupsOfWater_txt.setText(String.valueOf((int)current_cups));
        cupsOfWater_txt.append(" cups");
        TextView water_goal_txt=rootView.findViewById(R.id.water_target_txt);
        float water_target=db.get_water_goal(userSingleton.getId(),LocalDate.now());
        water_goal_txt.setText(String.valueOf((int)water_target));
        water_goal_txt.append(" cups");
        int temp=(int) Math.ceil((current_cups/water_target)*100);
        progress= temp;
        ProgressBar bar=rootView.findViewById(R.id.progress_bar_water);
        bar.setProgress(progress);
        Button add_cups_of_water=rootView.findViewById(R.id.addWaterBtn);
        add_cups_of_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_cups+=1;
                cupsOfWater_txt.setText(String.valueOf((int)current_cups));
                cupsOfWater_txt.append(" cups");
                db.update_water_intake(userSingleton.getId(),(int)current_cups,LocalDate.now());
                if(progress<=90)
                {
                    int temp=(int) Math.ceil((current_cups/water_target)*100);
                    progress= temp;
                    bar.setProgress(progress);
                }

            }
        });
        return rootView;
    }
}