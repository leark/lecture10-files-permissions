package edu.uw.filedemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private static final String FILE_NAME = "myFile.txt";

    private static final int REQUEST_WRITE_CODE = 1;
    
    private EditText textEntry; //save reference for quick access
    private RadioButton externalButton; //save reference for quick access

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        externalButton = (RadioButton)findViewById(R.id.radio_external);
        textEntry = (EditText)findViewById(R.id.textEntry); //what we're going to save
    }

    public void saveFile(View v){
        Log.v(TAG, "Saving file...");

        if(externalButton.isChecked()){ //external storage
            if(isExternalStorageWritable()) {
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    saveToExternalFile(); //helper method
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_CODE);
                }
            }
        }
        else { //internal storage


        }
    }

    //actually write to the file
    private void saveToExternalFile(){
        try {
            File dir = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            // File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (!dir.exists()) dir.mkdirs(); // create folder if doesn't exist

            File file = new File(dir, FILE_NAME);
            Log.v(TAG, "Saving to " + file.getAbsolutePath());

            PrintWriter out = new PrintWriter(new FileWriter(file, true));
            out.println(textEntry.getText().toString());

            out.close();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveToExternalFile();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
        // return Environment.MEDIA_MOUNTED.equals(state);
    }


    public void loadFile(View v){
        Log.v(TAG, "Loading file...");
        TextView textDisplay = (TextView)findViewById(R.id.txtDisplay); //what we're going to save
        textDisplay.setText(""); //clear initially

        if(externalButton.isChecked()){ //external storage
            if(isExternalStorageWritable()) {
                try {
                    File dir = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    File file = new File(dir, FILE_NAME);
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder text = new StringBuilder();

                    String line = reader.readLine();
                    while (line != null) {
                        text.append(line + "\n");
                        line = reader.readLine();
                    }
                    textDisplay.setText(text.toString());
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }
        }
        else { //internal storage
            File dir = getFilesDir();

            // FileInputStream fos = openFileInput(FILE_NAME, MODE_PRIVATE);

            getCacheDir();
            getExternalCacheDir();
        }
    }


    public void shareFile(View v) {
        Log.v(TAG, "Sharing file...");

        Uri fileUri = null;
        if(externalButton.isChecked()){ //external storage

        }
        else { //internal storage

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_photo:
                startActivity(new Intent(MainActivity.this, PhotoActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
