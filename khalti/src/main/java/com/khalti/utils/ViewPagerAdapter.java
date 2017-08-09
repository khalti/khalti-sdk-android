package com.khalti.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> pendingTitles = new ArrayList<>();
    private List<Fragment> pendingFragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pendingFragments.get(position);
    }

    @Override
    public int getCount() {
        return pendingFragments.size();
    }

    public void addFrag(Fragment fragment, String title) {
        pendingFragments.add(fragment);
        pendingTitles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pendingTitles.get(position);
    }
}