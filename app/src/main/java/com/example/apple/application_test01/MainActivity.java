package com.example.apple.application_test01;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView) findViewById(R.id.text1);
        //text.setText("hi bou!");

        TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setText("1");

        TextInputEditText input = (TextInputEditText) findViewById(R.id.edit_test1);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button1 = (Button) findViewById(R.id.button1);
    }

    @Override
    public void onClick(View view) {
        Log.i("add", "onClick: adding");//日志信息
        TextInputEditText input = (TextInputEditText) findViewById(R.id.edit_test1);
        Integer i = Integer.valueOf(input.getText().toString()).intValue();
        TextView text2 = (TextView) findViewById(R.id.text2);
        Integer j = Integer.valueOf(text2.getText().toString()).intValue();
        Integer adding = i + j;
        text2.setText(adding);

    }

    public void subtract(View button) {
        Log.i("subtract", "subtract : subtracting!");
        TextInputEditText input = (TextInputEditText) findViewById(R.id.edit_test1);
        Integer i = Integer.valueOf(input.getText().toString()).intValue();
        System.out.print("*****************"+i);
        TextView text2 = (TextView) findViewById(R.id.text2);
        Integer j = Integer.valueOf(text2.getText().toString()).intValue();
        Integer adding = j - i;
        text2.setText(adding);

    }
}
