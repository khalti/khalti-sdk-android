package com.khalti.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> pendingTitles = new ArrayList<>();
    private List<Fragment> pendingFragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
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