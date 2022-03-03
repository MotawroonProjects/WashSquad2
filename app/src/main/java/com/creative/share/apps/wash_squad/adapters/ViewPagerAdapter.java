package com.creative.share.apps.wash_squad.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titles;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();


    }

    public void addFragment(List<Fragment> fragments) {
        this.fragmentList.addAll(fragments);
    }

    public void addTitles(List<String> titles) {
        this.titles.addAll(titles);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
