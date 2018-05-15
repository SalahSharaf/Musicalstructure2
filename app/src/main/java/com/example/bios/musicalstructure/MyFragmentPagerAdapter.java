package com.example.bios.musicalstructure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GenresFragment.getInstance();
            case 1:
                return PlayListFragment.getInstance();
            case 2:
                return SongsFragment.getInstance();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
