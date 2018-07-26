package com.jch.test2;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.List;

public class MyPagerAdapter extends MyFragmentPagerAdapter {
        private List<Fragment> fs;

        public MyPagerAdapter(FragmentManager fm,List<Fragment> fs) {
            super(fm);
            this.fs = fs;
        }

        @Override
        public Fragment getItem(int position) {
            return fs.get(position);
        }

        @Override
        public int getCount() {
            return fs.size();
        }
    }