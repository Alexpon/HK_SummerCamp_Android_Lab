package com.example.bmi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

//    private EditText input_height;
    private EditText input_weight;
    private TextView inputH;
    private TextView inputW;
    private Button btn_submit;
    private Button btn_about;
    private Spinner spinner_feet;
    private Spinner spinner_inch;
    private String[] feetArray;
    private String[] inchArray;
    private int feet, inch;
    private String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListener();
//        registerForContextMenu(input_height);
        registerForContextMenu(input_weight);

        ArrayAdapter<String> adapterFeet = new ArrayAdapter<String>(this, R.layout.dropdown_item, feetArray);
        spinner_feet.setAdapter(adapterFeet);
        ArrayAdapter<String> adapterInch = new ArrayAdapter<String>(this, R.layout.dropdown_item, inchArray);
        spinner_inch.setAdapter(adapterInch);
    }

    protected void initViews(){
//        input_height = (EditText) findViewById(R.id.input_hight);
        input_weight = (EditText) findViewById(R.id.input_weight);
        inputH = (TextView) findViewById(R.id.textView);
        inputW = (TextView) findViewById(R.id.textView2);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_about = (Button) findViewById(R.id.btn_about);
        spinner_feet = (Spinner) findViewById(R.id.spinner_feet);
        spinner_inch = (Spinner) findViewById(R.id.spinner_inch);
        feetArray = getResources().getStringArray(R.array.feet);
        inchArray = getResources().getStringArray(R.array.inch);
    }

    protected void setListener(){
        btn_submit.setOnClickListener(myListener);
        btn_about.setOnClickListener(myListener);
        spinner_feet.setOnItemSelectedListener(mySelecter1);
        spinner_inch.setOnItemSelectedListener(mySelecter2);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
//                    String height = input_height.getText().toString();
                    weight = input_weight.getText().toString();

/*                    if(height.equals("")){
                        inputH.setTextColor(android.graphics.Color.RED);
                        inputW.setTextColor(Color.BLACK);
                        Toast.makeText(MainActivity.this, R.string.err_Hspace, Toast.LENGTH_LONG).show();
                    }
                    else */if(weight.equals("")){
                        inputW.setTextColor(android.graphics.Color.RED);
                        inputH.setTextColor(Color.BLACK);
                        Toast.makeText(MainActivity.this, R.string.err_Wspace, Toast.LENGTH_LONG).show();
                    }/*
                    else if(height.equals("0")){
                        inputH.setTextColor(android.graphics.Color.RED);
                        inputW.setTextColor(Color.BLACK);
                        Toast.makeText(MainActivity.this, R.string.err_Hzero, Toast.LENGTH_LONG).show();
                    }*/
                    else if(weight.equals("0")){
                        inputW.setTextColor(android.graphics.Color.RED);
                        inputH.setTextColor(Color.BLACK);
                        Toast.makeText(MainActivity.this, R.string.err_Wzero, Toast.LENGTH_LONG).show();
                    }
                    else {
                        savePreferences(feet-1, inch, weight);
                        inputH.setTextColor(Color.BLACK);
                        inputW.setTextColor(Color.BLACK);
                        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                        Bundle bundle = new Bundle();
                        //bundle.putString("height", height);
                        bundle.putInt("feet", feet);
                        bundle.putInt("inch", inch);
                        bundle.putString("weight", weight);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                    break;

                case R.id.btn_about:
                    openOptionsDialog();
                    break;

                default:
                    break;
            }
        }
    };

    private AdapterView.OnItemSelectedListener mySelecter1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            feet = position+1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private AdapterView.OnItemSelectedListener mySelecter2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            inch = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        loadPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        weight = input_weight.getText().toString();
        savePreferences(feet-1, inch, weight);
    }

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
        switch (item.getItemId()) {
            case R.id.menu_about:
                openOptionsDialog();
                return true;
            case R.id.menu_wiki:
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://en.wikipedia.org/wiki/Body_mass_index"));
                startActivity(intent);
                return true;
            case R.id.menu_exit:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_about:
                openOptionsDialog();
                return true;
            case R.id.menu_wiki:
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://en.wikipedia.org/wiki/Body_mass_index"));
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void openOptionsDialog(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_msg)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .show();
    }

    public void savePreferences(int f, int i, String w) {
        SharedPreferences pref = getSharedPreferences("BMI", MODE_PRIVATE);
        pref.edit().putInt("feet", f).commit();
        pref.edit().putInt("inches", i).commit();
        pref.edit().putString("weight", w).commit();
    }
    public void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("BMI", MODE_PRIVATE);
        spinner_feet.setSelection(pref.getInt("feet", 0));
        spinner_inch.setSelection(pref.getInt("inches", 0));
        input_weight.setText(pref.getString("weight", "0"));
    }

}
