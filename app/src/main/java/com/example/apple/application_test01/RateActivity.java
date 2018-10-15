package com.example.apple.application_test01;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RateActivity extends ListActivity implements Runnable{

    Handler handler;
    String TAG = "rate list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread thread = new Thread(this);
        thread.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 6) {
                    List<String> ratelist = (List<String>)msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RateActivity.this,android.R.layout.simple_expandable_list_item_1,ratelist);
                    setListAdapter(adapter);

                }
                super.handleMessage(msg);
            }
        };

        //ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, list_data);
        //setListAdapter(adapter);
    }


    public void run() {
            Log.i(TAG, "run: run()......");
            for (int i = 1; i < 3; i++) {
                Log.i(TAG, "run: i = " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            List<String> ratelist = new ArrayList<String>();

            String html = null;
            //获得message对象用于返回主线程
            Message msg = handler.obtainMessage(6);
            //

            URL url = null;
            try {
                url = new URL("http://www.usd-cny.com/icbc.htm");
                HttpURLConnection ht = (HttpURLConnection) url.openConnection();
                InputStream htm = ht.getInputStream();
                html = inputStream2String(htm);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document doc = Jsoup.parse(html);
            Element table = doc.selectFirst("table");
            Elements rows = table.select("tr");
            for (Element row : rows) {
                if (row == rows.first())
                    continue;

                Element a = row.selectFirst("a");
                Elements A_rates = row.select("td");
                String rate = A_rates.get(2).text();
                String name = a.text();
                ratelist.add(name + "=>" + rate);

                Log.i(TAG, "run: rate ="+rate+name);

            }

            msg.obj = ratelist;
            handler.sendMessage(msg);
        }

        public String inputStream2String (InputStream inputStream){
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            try {
                Reader in = new InputStreamReader(inputStream, "gb2312");
                while (true) {
                    int index = in.read(buffer, 0, buffer.length);
                    if (index < 0)
                        break;
                    out.append(buffer, 0, index);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return out.toString();
        }

    }



