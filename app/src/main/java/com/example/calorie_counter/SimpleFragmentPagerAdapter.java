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
            case 2:
                return new ShoppingListFragment();
            case 1:
                return new waterFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Home";
            case 2:
                return "ShoppingLists";
            case 1:
                return "Water";
        }
        return super.getPageTitle(position);
    }

}
