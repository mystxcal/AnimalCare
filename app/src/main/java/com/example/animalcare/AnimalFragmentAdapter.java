package com.example.animalcare;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AnimalFragmentAdapter extends FragmentPagerAdapter {

    public AnimalFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new FeedSectionFragment();
        else
            return new HelpSectionFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
         super.getPageTitle(position);

         if(position == 0)
             return "Feed";
         else
             return "Animal Help";

    }
}
