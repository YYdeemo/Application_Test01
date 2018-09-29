package com.example.apple.application_test01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RMBActivity extends AppCompatActivity{

    private final String TAG = "RMBActivity";

    private EditText rmb;
    private Button dollar;
    private Button euro;
    private Button won;
    private TextView result;

    private Double dollarrate = 5.5;
    private Double eurorate = 6.6;
    private Double wonrate = 11.11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmb);

        rmb = (EditText) findViewById(R.id.rmb);
        dollar = (Button) findViewById(R.id.dollar);
        euro = (Button) findViewById(R.id.euro);
        won = (Button) findViewById(R.id.won);
        result = (TextView) findViewById(R.id.result);
    }

    public void onclick(View but){
        if(rmb.getText()==null){
            Toast.makeText(RMBActivity.this,"is illegal",Toast.LENGTH_SHORT).show();
        }
        Double r = Double.parseDouble(rmb.getText().toString());
        double res = 0;
        if(r<0){
            Toast.makeText(RMBActivity.this,"your input is not access!",Toast.LENGTH_SHORT).show();
        }
        if(but.getId()==R.id.dollar){
            res = r*dollarrate;
            result.setText(res+"");
        }
        if (but.getId()==R.id.euro){
            res = r*eurorate;
            result.setText(res+"");
        }
    }

    public void getWon(View but){
        double res = 0;
        if(rmb.getText()==null){
            Toast.makeText(RMBActivity.this,"is illegal",Toast.LENGTH_SHORT).show();
        }
        Double r = Double.parseDouble(rmb.getText().toString());
        if(r<0){
            Toast.makeText(RMBActivity.this,"your input is not access!",Toast.LENGTH_SHORT).show();
        }
        res = r*wonrate;
        result.setText(res+"");

    }

    public void openOne(View btn){
        Intent intent = new Intent(this,SecondActivity.class);

        intent.putExtra("dollar_rate_key",dollarrate);
        intent.putExtra("euro_rate_key",eurorate);
        intent.putExtra("won_rate_key",wonrate);

        Log.i(TAG, "openOne: dollar_rate = "+dollarrate);
        Log.i(TAG, "openOne: euro_rate = "+eurorate);
        Log.i(TAG, "openOne: won_rate = "+wonrate);

        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1 && resultCode==2) {
            Bundle bundle = data.getExtras();
            dollarrate = bundle.getDouble("key_dollar", 0.0d);
            eurorate = bundle.getDouble("key_euro", 0.0d);
            wonrate = bundle.getDouble("key_won", 0.0d);

            Log.i(TAG, "onActivityResult: dollar_rate = "+dollarrate);
            Log.i(TAG, "onActivityResult: euro_rate = "+eurorate);
            Log.i(TAG, "onActivityResult: won_rate = "+wonrate);
        }

        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_config){
            Intent intent = new Intent(this,SecondActivity.class);

            intent.putExtra("dollar_rate_key",dollarrate);
            intent.putExtra("euro_rate_key",eurorate);
            intent.putExtra("won_rate_key",wonrate);

            Log.i(TAG, "openOne: dollar_rate = "+dollarrate);
            Log.i(TAG, "openOne: euro_rate = "+eurorate);
            Log.i(TAG, "openOne: won_rate = "+wonrate);

            startActivityForResult(intent,1);

        }
        return super.onOptionsItemSelected(item);
    }


}
