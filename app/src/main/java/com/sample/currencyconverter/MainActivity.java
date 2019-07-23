package com.sample.currencyconverter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    CustomPageAdapter mCustomPageAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomPageAdapter = new CustomPageAdapter(getSupportFragmentManager(), this);

        //getPosisi.getItem();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPageAdapter);

        mViewPager.getCurrentItem();
    }

}
