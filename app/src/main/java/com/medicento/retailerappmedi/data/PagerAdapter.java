package com.medicento.retailerappmedi.data;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter{
    ArrayList<Fragment> frames = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragements (Fragment fragment, String tabTitles) {
        this.frames.add(fragment);
        this.tabTitles.add(tabTitles);
    }


    @Override
    public Fragment getItem(int position) {
        return frames.get(position);
    }

    @Override
    public int getCount() {
        return frames.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
