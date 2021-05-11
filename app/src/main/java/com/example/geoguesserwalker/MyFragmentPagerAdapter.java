package com.example.geoguesserwalker;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private ObjectiveTracker objectiveTracker;

    public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;
        mFragmentTags = new HashMap<Integer, String>();
        objectiveTracker = new ObjectiveTracker();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return objectiveTracker.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return ObjectiveFragment.newInstance(objectiveTracker.get(position));

    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return objectiveTracker.get(position).getName();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        String tag = mFragmentTags.get(position);
        if (tag != null) {
            fragment = mFragmentManager.findFragmentByTag(tag);
        }
        return fragment;
    }
}

