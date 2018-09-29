package com.example.apple.application_test01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {

    private final String TAG = "ConfigActivity";

    EditText edit_dollar_rate;
    EditText edit_euro_rate;
    EditText edit_won_rate;

    Button save_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        double dollar2 = intent.getDoubleExtra("dollar_rate_key",0.0d);
        double euro2 = intent.getDoubleExtra("euro_rate_key",0.0d);
        double won2 = intent.getDoubleExtra("won_rate_key", 0.0d);

        Log.i(TAG, "onCreate: dollar2 = "+dollar2);
        Log.i(TAG, "onCreate: euro2 = "+euro2);
        Log.i(TAG, "onCreate: won2 = "+won2);

        edit_dollar_rate = (EditText) findViewById(R.id.dollar_rate);
        edit_euro_rate = (EditText) findViewById(R.id.euro_rate);
        edit_won_rate = (EditText) findViewById(R.id.won_rate);

        edit_dollar_rate.setText(dollar2+"");
        edit_euro_rate.setText(String.valueOf(euro2));
        edit_won_rate.setText(String.valueOf(won2));
    }

    public void SaveRate(View btn){

        Double newdollarrate = Double.parseDouble(edit_dollar_rate.getText().toString());
        Double neweurorate = Double.parseDouble(edit_euro_rate.getText().toString());
        Double newwonrate = Double.parseDouble(edit_won_rate.getText().toString());

        Log.i(TAG, "SaveRate: ");
        Log.i(TAG, "SaveRate: new dollar rate = "+newdollarrate);
        Log.i(TAG, "SaveRate: new euro rate = " + neweurorate);
        Log.i(TAG, "SaveRate: new won rate = " +newwonrate);

        //SAVE THE RATE TO MYRATE.XML
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("dollar_rate",Float.parseFloat(newdollarrate.toString()));
        editor.putFloat("euro_rate",Float.parseFloat(neweurorate.toString()));
        editor.putFloat("won_rate",Float.parseFloat(newwonrate.toString()));
        editor.commit();

        Log.i(TAG, "SaveRate: save data in sharedperferences");

        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putFloat("dollar_rate",Float.parseFloat(newdollarrate.toString()));
        editor1.putFloat("euro_rate",Float.parseFloat(neweurorate.toString()));
        editor1.putFloat("won_rate",Float.parseFloat(newwonrate.toString()));
        editor1.commit();

        Log.i(TAG, "SaveRate: save data in PreferenceManager");

        Intent intent = getIntent();

        Bundle bundle = new Bundle();
        bundle.putDouble("key_dollar",newdollarrate);
        bundle.putDouble("key_euro", neweurorate);
        bundle.putDouble("key_won", newwonrate);

        intent.putExtras(bundle);
        setResult(2,intent);

        finish();

    }
}
