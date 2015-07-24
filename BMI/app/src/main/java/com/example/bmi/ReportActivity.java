package com.example.bmi;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by EE4990 on 7/13/2015.
 */
public class ReportActivity extends Activity {

    private Bundle bundle;
    private double height;
    private int feet, inch;
    private double weight;
    private double bmi;
    private TextView textVal;
    private TextView textCom;
    private ImageView img;
    private Button btn_back;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initViews();
        setListener();
        calcBMI();
    }

    public void initViews(){
        bundle = getIntent().getExtras();
        //height = Double.parseDouble(bundle.getString("height"))/100;
        feet = bundle.getInt("feet");
        inch = bundle.getInt("inch");
        height = (double)(feet*12 + inch) * 0.0254;
        weight = Double.parseDouble(bundle.getString("weight"))*0.45359;
        img = (ImageView) findViewById(R.id.img);
        textVal = (TextView) findViewById(R.id.text_value);
        textCom = (TextView) findViewById(R.id.text_comment);
        btn_back = (Button) findViewById(R.id.btn_back);
    }

    public void calcBMI(){
        bmi = weight / (height * height);

        DecimalFormat nf = new DecimalFormat("0.00");
        textVal.setText(getString(R.string.bmi_result) + nf.format(bmi));
        if(bmi > 25){
            img.setImageResource(R.drawable.bot_fat);
            textCom.setText(R.string.bmi_heavy);
        }
        else if(bmi <20){
            img.setImageResource(R.drawable.bot_thin);
            textCom.setText(R.string.bmi_thin);
        }
        else{
            img.setImageResource(R.drawable.bot_fit);
            textCom.setText(R.string.bmi_avg);
        }
    }

    public void setListener(){
        btn_back.setOnClickListener(myListener);
    }

    public View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ReportActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
