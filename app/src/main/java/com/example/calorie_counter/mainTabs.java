package com.example.calorie_counter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;

import com.google.android.material.tabs.TabLayout;

public class mainTabs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);
//        Bundle extras = getIntent().getExtras();
//        boolean openF2 = false;
//        if(extras!=null && extras.containsKey("openF2")){
//            openF2 = extras.getBoolean("openF2");
//        }
//        if(openF2){
//           viewPager = findViewById(R.id.)
//        }
        int defaultValue = 0;
        int page = getIntent().getIntExtra("One", defaultValue);
        ViewPager viewPager=findViewById(R.id.ViewPager1);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(page);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.TabLayout1);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.item1:
            {
                Intent i = new Intent(getApplicationContext(), profile.class);
                startActivity(i);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}