package com.khalti.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> pendingFragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    public void addFrag(Fragment fragment) {
        pendingFragments.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return pendingFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return pendingFragments.size();
    }

    public Fragment getItem(int position) {
        return pendingFragments.get(position);
    }
}