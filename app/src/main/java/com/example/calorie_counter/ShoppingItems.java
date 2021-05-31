package com.example.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ShoppingItems extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {
    String tempText = "";
    int id;
    Database db = new Database(this);
    GroceryList newList = new GroceryList();
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
            R.mipmap.corn_icon_foreground,
            R.mipmap.cucumber_icon_foreground,
            R.mipmap.eggplant_icon_foreground,
            R.mipmap.garlic_icon_foreground,
            R.mipmap.green_beans_icon_foreground,
            R.mipmap.jelapeno_icon_foreground,
            R.mipmap.lettuce_icon_foreground,
            R.mipmap.mushroom_icon_foreground,
            R.mipmap.onion_icon_foreground,
            R.mipmap.orange_icon_foreground,
            R.mipmap.pepper_icon_foreground,
            R.mipmap.pumpkin_icon_foreground,
            R.mipmap.tomato_icon_foreground,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_items);
        id = userSingleton.getId();
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

        itemsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsList itemsList = new ItemsList();
                String text = itemsList.items.get(position);
                Double currentAmount = newList.add(text);
                Toast.makeText(getApplicationContext(),text +" "+ currentAmount.toString() + " KGs",Toast.LENGTH_LONG).show();
            }
        });
        itemsGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsList itemsList = new ItemsList();
                String text = itemsList.items.get(position);
                tempText=text;
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(),"BottomSheet");
                return false;
            }
        });
        FloatingActionButton addListBtn = (FloatingActionButton)findViewById(R.id.addListBtn);
        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.add_grocery_list(id,newList.items,newList.quantities);
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

    @Override
    public void onButtonClicked(String text) {

        Double currentAmount = newList.set(tempText,Double.parseDouble(text));
        Toast.makeText(getApplicationContext(),tempText +" "+ currentAmount.toString() + " KGs",Toast.LENGTH_LONG).show();

    }
}