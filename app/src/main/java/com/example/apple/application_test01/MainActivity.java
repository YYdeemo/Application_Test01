package com.example.apple.application_test01;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity{

    private EditText height;
    private EditText weight;
    private Button getbmi;
    private TextView bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        getbmi = findViewById(R.id.getbmi);
        getbmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DecimalFormat df = new DecimalFormat("######0.00");
                if(height.getText()==null||weight.getText()==null){
                    Toast.makeText(MainActivity.this,"your input is not access!",Toast.LENGTH_SHORT).show();
                }
                Double h = Double.parseDouble(height.getText().toString());
                Double w = Double.parseDouble(weight.getText().toString());
                if(h<=0 || h>=2.5){
                    Toast.makeText(MainActivity.this,"your height is not access!",Toast.LENGTH_SHORT).show();
                }else if(w<=0 || w>300){
                    Toast.makeText(MainActivity.this,"your weight is not access!",Toast.LENGTH_SHORT).show();
                }else{
                    Double my_bmi = w/h*h;

                    bmi.setText(df.format(my_bmi)+" ");
                }


            }
        });


    }

}
