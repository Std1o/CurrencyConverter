package com.sample.currencyconverter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.sample.currencyconverter.R;


/**
 * Created by ovix on 10/10/17.
 */

public class FragmentMap extends Fragment {

    Boolean isStarted = false;
    Boolean isVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SaveInstanceState){
        View rootView = inflater.inflate(R.layout.fragment2, container, false);

        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible && isStarted){

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isStarted && isVisible) {

        }
    }
}
