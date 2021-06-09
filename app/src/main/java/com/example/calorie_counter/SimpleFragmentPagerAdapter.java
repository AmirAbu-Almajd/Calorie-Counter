package com.example.calorie_counter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {


    public SimpleFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new MainMenuFragment();
            case 1:
                return new NutritionsFragment();
            case 3:
                return new ShoppingListFragment();
            case 2:
                return new waterFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Home";
            case 1:
                return "Nutrition Facts";
            case 3:
                return "Shopping Lists";
            case 2:
                return "Water";
        }
        return super.getPageTitle(position);
    }

}
