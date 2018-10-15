package com.example.apple.application_test01;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

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
import java.util.HashMap;
import java.util.List;
import java.util.SimpleTimeZone;

public class RateItemActivity extends ListActivity implements Runnable{

    Handler handler;
    private List<HashMap<String, String>> listItem;
    private SimpleAdapter listItemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread thread = new Thread(this);
        thread.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    listItem = (List<HashMap<String,String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(RateItemActivity.this,listItem,
                        R.layout.activity_ratelist,
                        new String[]{"item_name","item_rate"},
                        new int[]{R.id.item_name,R.id.item_rate});
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

    }


    public void run(){
        List<HashMap<String,String>> rate_list = new ArrayList<HashMap<String, String>>();
        boolean marker = false;
        String html = null;
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
            HashMap<String,String> map = new HashMap<String, String>();
            Element a = row.selectFirst("a");
            Elements A_rates = row.select("td");
            String rate = A_rates.get(2).text();
            String name = a.text();
            Log.i("rate_itemlist", "run: rate ="+rate+name);
            map.put("item_name",""+name);
            map.put("item_rate",""+rate);
            rate_list.add(map);
            marker = true;
        }
        Message msg = handler.obtainMessage(1);
        if(marker){
            msg.arg1=1;
        }else{
            msg.arg1=0;
        }
        msg.obj = rate_list;
        handler.sendMessage(msg);


//        listItemAdapter = new SimpleAdapter(this,listItem,
//                R.layout.activity_ratelist,
//                new String[]{"item_name","item_rate"},
//                new int[]{R.id.item_name,R.id.item_rate});
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
