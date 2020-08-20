package com.coduniq.dmad.ui.home;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.coduniq.dmad.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CrashActivity extends AppCompatActivity {
    public static String firstN,secondN;
    public EditText edT1;
    public EditText edT2;
    private String No1;
    private String No2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hjh
                Intent i = new Intent();
                i.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
                i.setAction("android.intent.action.MAIN");
                i.addCategory("android.intent.category.LAUNCHER");
                i.addCategory("android.intent.category.DEFAULT");
                Toast.makeText(getApplicationContext(),"Copy the particular contact's number", Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });

        edT1 = (EditText) findViewById(R.id.firstNumber);
        edT2 = (EditText) findViewById(R.id.secondNumber);
        try {
            File myFile = new File("/sdcard/.emergencyNumbers.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            No1 = myReader.readLine();
            No2 = myReader.readLine();
            myReader.close();
            edT1.setText(No1);
            edT2.setText(No2);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        Button doneB=(Button)findViewById(R.id.doneButton);
        if (doneB != null) {
            doneB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(edT1.getText()!=null)
                        firstN=edT1.getText().toString();
                    if(edT2.getText()!=null)
                        secondN=edT2.getText().toString();
                    try {
                        File myFile = new File("/sdcard/.emergencyNumbers.txt");
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        myOutWriter.append(firstN);
                        myOutWriter.append("\n");
                        myOutWriter.append(secondN);
                        myOutWriter.close();
                        fOut.close();
                        Toast.makeText(getApplicationContext(),
                                "The emergency contact numbers have been saved.",
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    Log.d(getPackageName(), "Done! button pressed.");
                }
            });
        }


    }


}
