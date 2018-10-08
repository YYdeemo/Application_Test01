package com.example.apple.application_test01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RMBActivity extends AppCompatActivity implements Runnable{

    private final String TAG = "RMBActivity";

    private EditText rmb;
    private Button dollar;
    private Button euro;
    private Button won;
    private TextView result;

    private Double dollarrate = 5.5;
    private Double eurorate = 6.6;
    private Double wonrate = 11.11;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmb);

        rmb = (EditText) findViewById(R.id.rmb);
        dollar = (Button) findViewById(R.id.dollar);
        euro = (Button) findViewById(R.id.euro);
        won = (Button) findViewById(R.id.won);
        result = (TextView) findViewById(R.id.result);

        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        Float fd = sharedPreferences.getFloat("dollar_rate",0.0f);
        Float fe = sharedPreferences.getFloat("euro_rate",0.0f);
        Float fw = sharedPreferences.getFloat("won_rate",0.0f);

        dollarrate = Double.parseDouble(fd.toString());
        eurorate = Double.parseDouble(fe.toString());
        wonrate = Double.parseDouble(fw.toString());

        Log.i(TAG, "onCreate: sp dollar_rate = "+dollarrate);
        Log.i(TAG, "onCreate: sp euro_rate = "+eurorate);
        Log.i(TAG, "onCreate: sp won_rate = "+wonrate);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getmessages = " + str);
                    String[] rates = str.split(";");
                    dollarrate = Double.parseDouble(rates[0]);
                    eurorate = Double.parseDouble(rates[1]);
                    wonrate = Double.parseDouble(rates[2]);
                }
                super.handleMessage(msg);
            }
        };
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
            res = r*100/dollarrate;
            BigDecimal bg = new BigDecimal(res);
            double res2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            result.setText(res2+"");
        }
        if (but.getId()==R.id.euro){
            res = r*100/eurorate;
            BigDecimal bg = new BigDecimal(res);
            double res2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            result.setText(res2+"");
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
        res = r*100/wonrate;
        BigDecimal bg = new BigDecimal(res);
        double res2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        result.setText(res2+"");

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

    public void run(){

        Log.i(TAG, "run: run()......");
        for(int i = 1; i < 3; i++){
            Log.i(TAG, "run: i = "+i);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        String rates = null;
        //获得message对象用于返回主线程
        Message msg = handler.obtainMessage(5);
        //

        URL url = null;
        try{
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection ht  = (HttpURLConnection) url.openConnection();
            InputStream htm = ht.getInputStream();
            String html = inputStream2String(htm);
            //Log.i(TAG, "run: html = "+html);

            //get rates
            rates = GetRateFromInternet(html);
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        msg.obj = rates;
        handler.sendMessage(msg);
    }

    private String GetRateFromInternet(String html){
        String rates = null;
        Document doc = Jsoup.parse(html);
        Element table = doc.selectFirst("table");
        Elements rows = table.select("tr");
        for(Element row : rows) {
            if(row == rows.first())
                continue;

            Element a = row.selectFirst("a");
            Log.i(TAG, "GetRateFromInternet: has a , a text is "+a.text());
            if(a.text().contains("美元")){
                Elements A_rates = row.select("td");
                String America_rate = A_rates.get(2).text();
                Log.i(TAG, "GetRateFromInternet: get the America rate is :"+America_rate);
                rates = America_rate+";";
            }
            if(a.text().contains("欧元")){
                Elements E_rates = row.select("td");
                String Euro_rate = E_rates.get(2).text();
                Log.i(TAG, "GetRateFromInternet: get the Euro rate is :"+Euro_rate);
                rates = rates+Euro_rate+";";
            }
            if(a.text().contains("韩国元")){
                Elements W_rates = row.select("td");
                String Won_rate = W_rates.get(2).text();
                Log.i(TAG, "GetRateFromInternet: get the Won rate is :"+ Won_rate);
                rates = rates+Won_rate;
            }
        }
        Log.i(TAG, "GetRateFromInternet: get all rates:"+rates);
        return rates;
    }


    private String inputStream2String(InputStream inputStream) {
        final int bufferSize = 1024;
        final char[]  buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            Reader in = new InputStreamReader(inputStream, "gb2312");
            while (true) {
                int index = in.read(buffer, 0, buffer.length);
                if (index < 0)
                    break;
                out.append(buffer,0,index);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return out.toString();
    }


}
