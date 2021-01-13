package com.example.persistentdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {
    TextView output;
    EditText input;
    Button savePref, loadPref,saveFile,loadFile;
    SharedPreferences myPref;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // to add string resource: click the icon next to text (input) column -> add new resources at top right corner
        output=findViewById(R.id.outputText);
        input=findViewById(R.id.inputText);
        savePref=findViewById(R.id.savePref);
        loadPref=findViewById(R.id.loadPref);
        saveFile=findViewById(R.id.saveFile);
        loadFile=findViewById(R.id.loadFile);
        myPref= PreferenceManager.getDefaultSharedPreferences(this);
        myEditor=myPref.edit();

        //Part 1: save in data/data/<package>/shared_prefs->xml file
        savePref.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String inputString=input.getText().toString();
                // @String xml => add com.example.persistentdata.preferenceString as key (ignore the error at xml)
                myEditor.putString(getString(R.string.com_example_persistentdata_preferenceString),inputString); //overwrite in String xml with input text
                myEditor.commit();
                Toast.makeText(MainActivity.this,"Preference saved",Toast.LENGTH_SHORT).show();
            }
        });

        loadPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display=myPref.getString(getString(R.string.com_example_persistentdata_preferenceString), "NULL");
                output.setText(display);
            }
        });

        // Part 2: save for internal file at in data/data/<package>/files ->notes.txt (right click-> synchronize)
        /*saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = input.getText().toString();
                OutputStreamWriter out=null;
                try{
                    out = new OutputStreamWriter(openFileOutput("notes.txt",MODE_APPEND)); //MODE_PRIVATE vs MODE_APPEND
                    out.write(inputString);
                    Toast.makeText(MainActivity.this,"Output saved to Internal File",Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        loadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream is = null;
                try{
                    is=openFileInput("notes.txt");
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);
                    String line;
                    StringBuilder sb=new StringBuilder();
                    while((line=reader.readLine())!=null){
                        sb.append(line+"\n");
                    }
                    output.setText(sb.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    try{
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

        // Part 3: Save External Files at: /storage/emulated/0/Android/data/<package>/files/
        // Part 3 needs to add permission in manifest (between "package and application")(refer to Lecture 7)
        //(optional) at <application -> add android:requestLegacyExternalStorage="true"
        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString=input.getText().toString();
                String path=getExternalFilesDir("notes")+"/notes.txt";
                Log.i("filepath",path);
                FileWriter fw=null;
                try{
                    fw = new FileWriter(path);
                    fw.write(inputString);
                    Toast.makeText(MainActivity.this,"Output saved to External File",Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        loadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path=getExternalFilesDir("notes")+"/notes.txt";
                FileReader fr=null;
                try{
                    fr=new FileReader(path);
                    BufferedReader reader = new BufferedReader(fr);
                    String line;
                    StringBuilder sb=new StringBuilder();
                    while((line=reader.readLine())!=null){
                        sb.append(line+"\n");
                    }
                    output.setText(sb.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    try {
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}
