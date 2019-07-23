package com.sample.currencyconverter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etValue;
    TextView tvFirstCountry, tvSecondCountry, tvResult;
    String originalCurrency = "RUB";
    String convertToCurrency = "CHF";
    int iHateFuckingGit = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        etValue = findViewById(R.id.editText);
        tvFirstCountry = findViewById(R.id.firstCountry);
        tvSecondCountry = findViewById(R.id.secondCountry);
        tvResult = findViewById(R.id.tvResult);
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
        findViewById(R.id.button).callOnClick();
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String[] mCurrency = {"RUB", "CHF", "EUR", "GBP", "JPY", "UAH", "KZT", "BYN", "TRY", "CNY", "AUD", "CAD", "PLN"};

        builder = new AlertDialog.Builder(this);
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
        return builder.create();
    }

    public void convert(View view) {
        final double value = Double.parseDouble(etValue.getText().toString());
        RequestQueue queue = Volley.newRequestQueue(this);
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
}
