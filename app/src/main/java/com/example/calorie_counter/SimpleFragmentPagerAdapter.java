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
                return new ShoppingListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "MainMenu";
            case 1:
                return "ShoppingLists";
        }
        return super.getPageTitle(position);
    }

}
