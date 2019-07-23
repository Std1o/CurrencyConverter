package com.sample.currencyconverter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sample.currencyconverter.fragments.FragmentCurrencyConverter;
import com.sample.currencyconverter.fragments.FragmentMap;

public class CustomPageAdapter extends FragmentPagerAdapter {


    protected Context mContext;


    public CustomPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {


        Fragment fragment1 = new FragmentCurrencyConverter();
        Fragment fragment2 = new FragmentMap();
        Fragment varFrag = fragment1;


        switch (position) {
            case 0:
                varFrag = fragment1;
                break;
            case 1:
                varFrag = fragment2;
                break;
        }

        return varFrag;

    }


    @Override
    public int getCount() {
        return 3;
    }
}