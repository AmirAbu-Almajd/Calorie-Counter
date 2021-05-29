package com.example.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ShoppingItems extends AppCompatActivity {
    int image[] = {
            R.mipmap.blueberry_icon_foreground,
            R.mipmap.apple_icon_foreground,
            R.mipmap.artichoke_icon_foreground,
            R.mipmap.asparagus_icon_foreground,
            R.mipmap.avocado_icon_foreground,
            R.mipmap.beetroot_icon_foreground,
            R.mipmap.broccoli_icon_foreground,
            R.mipmap.cabbage_icon_foreground,
            R.mipmap.carrot_icon_foreground,
            R.mipmap.cauliflower_icon_foreground,
            R.mipmap.celery_icon_foreground,
            R.mipmap.cherry_icon_foreground,
            R.mipmap.chives_icon_foreground,
            R.mipmap.corn_icon_foreground,};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_items);
        GridView itemsGrid = (GridView) findViewById(R.id.itemsGrid);
        int color = 0xFFFFFF;
        ArrayList<imageModel> arrayList = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            imageModel imageModel = new imageModel();
            imageModel.setmThumbIds(image[i]);
            arrayList.add(imageModel);
        }
        imageAdapter adapter = new imageAdapter(getApplicationContext(), arrayList);
        itemsGrid.setAdapter(adapter);
        GroceryList newList = new GroceryList();
        itemsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsList itemsList = new ItemsList();
                String text = itemsList.items.get(position);
                newList.add(text);
//                newList.show();
//                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton addListBtn = (FloatingActionButton)findViewById(R.id.addListBtn);
        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingListFragment.lists.add(newList);
                Intent intent = new Intent(ShoppingItems.this, mainTabs.class);
                int page = 2;
                intent .putExtra("One",page);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });
    }
}