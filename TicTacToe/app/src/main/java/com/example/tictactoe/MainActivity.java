package com.example.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class MainActivity extends Activity {

    private Button start;
    private Button exit;
    private RadioButton hard;
    private RadioButton easy;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListener();
    }

    public void initViews(){
        start = (Button) findViewById(R.id.btn_start);
        exit = (Button) findViewById(R.id.btn_exit);
        hard = (RadioButton) findViewById(R.id.btn_hard);
        easy = (RadioButton) findViewById(R.id.btn_easy);
        level = "easy";
    }

    public void setListener(){
        start.setOnClickListener(myListener);
        exit.setOnClickListener(myListener);
        hard.setOnClickListener(myListener);
        easy.setOnClickListener(myListener);
        easy.setChecked(true);
    }


private View.OnClickListener myListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_LEVEL", level);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn_hard:
                level = "hard";
                break;
            case R.id.btn_easy:
                level = "easy";
                break;
        }
    }
};


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
