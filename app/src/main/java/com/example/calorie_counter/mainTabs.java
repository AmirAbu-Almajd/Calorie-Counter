package com.example.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class mainTabs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);

        ViewPager viewPager=findViewById(R.id.ViewPager1);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout=(TabLayout)findViewById(R.id.TabLayout1);
        tabLayout.setupWithViewPager(viewPager);
    }
}