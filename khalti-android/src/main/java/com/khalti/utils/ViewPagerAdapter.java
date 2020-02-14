package com.khalti.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<String> pendingTitles = new ArrayList<>();
    private List<Fragment> pendingFragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    public void addFrag(Fragment fragment, String title) {
        pendingFragments.add(fragment);
        pendingTitles.add(title);
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