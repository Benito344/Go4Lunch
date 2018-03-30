package com.behague.benjamin.go_4_lunch.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.behague.benjamin.go_4_lunch.controllers.fragments.ListView;
import com.behague.benjamin.go_4_lunch.controllers.fragments.MapViewFragment;
import com.behague.benjamin.go_4_lunch.controllers.fragments.Workmates;

/**
 * Created by Benjamin BEHAGUE on 28/03/2018.
 */

public class PageAdapter extends FragmentPagerAdapter {

    // 2 - Default Constructor
    public PageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return (3); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return MapViewFragment.newInstance();

            case 1 :
                return ListView.newInstance();

            case 2 :
                return Workmates.newInstance();

            default:
                return null;
        }
    }
}