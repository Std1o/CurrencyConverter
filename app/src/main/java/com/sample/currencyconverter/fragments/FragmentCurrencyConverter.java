package com.sample.currencyconverter.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sample.currencyconverter.R;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentCurrencyConverter extends Fragment {

    Boolean isStarted = false;
    Boolean isVisible = false;
    EditText etValue;
    TextView tvFirstCountry, tvSecondCountry, tvResult;
    String originalCurrency = "RUB";
    String convertToCurrency = "CHF";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SaveInstanceState){
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        return rootView;
    }

    private void initViews() {
        etValue = getActivity().findViewById(R.id.editText);
        tvFirstCountry = getActivity().findViewById(R.id.firstCountry);
        tvSecondCountry = getActivity().findViewById(R.id.secondCountry);
        tvResult = getActivity().findViewById(R.id.tvResult);
        setTextViewClickListeners();
    }

    private void setTextViewClickListeners() {
        tvFirstCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });

        tvSecondCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
    }

    public void swap(View view) {
        originalCurrency = tvSecondCountry.getText().toString();
        convertToCurrency = tvFirstCountry.getText().toString();
        tvFirstCountry.setText(originalCurrency);
        tvSecondCountry.setText(convertToCurrency);
        getActivity().findViewById(R.id.button).callOnClick();
    }

    protected void showDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] mCurrency = {"RUB", "CHF", "EUR", "GBP", "JPY", "UAH", "KZT", "BYN", "TRY", "CNY", "AUD", "CAD", "PLN"};

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите валюту"); // заголовок для диалога

        builder.setItems(mCurrency, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (id == 0) {
                    originalCurrency = mCurrency[item];
                    tvFirstCountry.setText(mCurrency[item]);
                }
                else {
                    convertToCurrency = mCurrency[item];
                    tvSecondCountry.setText(mCurrency[item]);
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void convert(View view) {
        final double value = Double.parseDouble(etValue.getText().toString());
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://api.exchangerate-api.com/v4/latest/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+originalCurrency,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;

                        try {
                            obj = new JSONObject(response);
                            JSONObject firstItem = obj.getJSONObject("rates");
                            double currencyRate = Double.parseDouble(firstItem.getString(convertToCurrency));
                            double converted = value * currencyRate;
                            tvResult.setText(String.valueOf(converted));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
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
