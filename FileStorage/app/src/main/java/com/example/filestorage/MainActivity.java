package com.example.filestorage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends Activity {

    EditText mEditText;
    Button btn_save;
    Button btn_load;
    Button btn_sd_save;
    Button btn_sd_load;
    static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListener();
    }

    public void initViews(){
        mEditText = (EditText) findViewById(R.id.editText1);
        btn_save = (Button) findViewById(R.id.save_button);
        btn_load = (Button) findViewById(R.id.load_button);
        btn_sd_save = (Button) findViewById(R.id.sds_button);
        btn_sd_load = (Button) findViewById(R.id.sdl_button);
    }

    public void setListener(){
        btn_save.setOnClickListener(myListener);
        btn_load.setOnClickListener(myListener);
        btn_sd_save.setOnClickListener(myListener);
        btn_sd_load.setOnClickListener(myListener);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save_button:
                    String str1 = mEditText.getText().toString();
                    try {
                        FileOutputStream fOut = openFileOutput("textfile.txt", MODE_PRIVATE);
                        OutputStreamWriter osw = new OutputStreamWriter(fOut);
                        // --- Write the string to the file ---
                        osw.write(str1);
                        osw.flush();
                        osw.close();
                        // --- Display file saved message ---
                        Toast.makeText(getBaseContext(), "File saved successfully!",
                                Toast.LENGTH_LONG).show();
                        //--- Clears the EditText ---
                        mEditText.setText("");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case R.id.load_button:
                    try {
                        FileInputStream fIn = openFileInput("textfile.txt");
                        InputStreamReader isr = new InputStreamReader(fIn);
                        char[] inputBuffer = new char[READ_BLOCK_SIZE];
                        String s = "";
                        int charRead;
                        while ((charRead = isr.read(inputBuffer)) > 0) {
                        //--- Convert the chars to a String ---
                            String readString = String
                                    .copyValueOf(inputBuffer, 0, charRead);
                            s += readString;
                            inputBuffer = new char[READ_BLOCK_SIZE];
                        }
                        //--- Set the EditText to the text that has been read ---
                        mEditText.setText(s);
                        Toast.makeText(getBaseContext(), "File loaded successfully!",
                                Toast.LENGTH_LONG).show();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case R.id.sds_button:
                    String str2 = mEditText.getText().toString();
                    try {
                    //--- SD Card Storage ---
                        File sdCard = Environment.getExternalStorageDirectory();
                        File directory = new File (sdCard.getAbsoluteFile() + "/MyFiles");
                        directory.mkdirs();
                        File file = new File(directory, "textfile.txt");
                        FileOutputStream fOut = new FileOutputStream(file);
                        OutputStreamWriter osw = new OutputStreamWriter(fOut);
                    // --- Write the string to the file ---
                        osw.write(str2);
                        osw.flush();
                        osw.close();
                    // --- Display file saved message ---
                        Toast.makeText(getBaseContext(), "File saved successfully!",
                                Toast.LENGTH_LONG).show();
                    //--- Clears the EditText ---
                        mEditText.setText("");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case R.id.sdl_button:
                    try {
                    //--- SD Storage ---
                        File sdCard = Environment.getExternalStorageDirectory();
                        File directory = new File (sdCard.getAbsoluteFile() + "/MyFiles");
                        File file = new File(directory, "textfile.txt");
                        FileInputStream fIn = new FileInputStream(file);
                        InputStreamReader isr = new InputStreamReader(fIn);
                        char[] inputBuffer = new char[READ_BLOCK_SIZE];
                        String s = "";
                        int charRead;
                        while ((charRead = isr.read(inputBuffer)) > 0) {
                        //--- Convert the chars to a String ---
                            String readString = String
                                    .copyValueOf(inputBuffer, 0, charRead);
                            s += readString;
                            inputBuffer = new char[READ_BLOCK_SIZE];
                        }
                        //--- Set the EditText to the text that has been read ---
                        mEditText.setText(s);
                        Toast.makeText(getBaseContext(), "File loaded successfully!",
                                Toast.LENGTH_LONG).show();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
            }
        }
    };
}
