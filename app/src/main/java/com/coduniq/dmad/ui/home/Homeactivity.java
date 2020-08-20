package com.coduniq.dmad.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coduniq.dmad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

public class Homeactivity extends AppCompatActivity {
    private DatabaseReference ref;
    String d;
    private LinearLayout l;
    public int flag;

    private ImageView alert,loc,prof,dr,ac;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        Common.profileFlag=false;
        ref= FirebaseDatabase.getInstance().getReference("users");
        l=findViewById(R.id.layout);
        alert=findViewById(R.id.alert);
        loc=findViewById(R.id.location);
        prof=findViewById(R.id.profile);
        dr=findViewById(R.id.drowsiness);
        ac=findViewById(R.id.accident);
        flag=1;
        final Button serviceB=(Button)findViewById(R.id.service);
        serviceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    Toast.makeText(Homeactivity.this, "ACTIVATED!", Toast.LENGTH_LONG).show();
                    serviceB.setText("Press to de-activate driving mode");
                    startService(new Intent(getApplicationContext(), ShakeService.class));
                    flag = 0;
                } else {
                    Toast.makeText(Homeactivity.this, "DEACTIVATED!", Toast.LENGTH_LONG).show();
                    stopService(new Intent(getApplicationContext(), ShakeService.class));
                    serviceB.setText("Press to activate driving mode");
                    flag = 1;
                }

            }
        });
        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homeactivity.this,ProfileActivity.class));
            }
        });
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homeactivity.this,LocationActivity.class));
            }
        });
        dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homeactivity.this,DrowsinessActivity.class));
            }
        });
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homeactivity.this,CrashActivity.class));
            }
        });
        ref.child("100001").child("drowsiness").child("d").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common.alert= dataSnapshot.getValue(String.class);
                d=Common.alert;
                Toast.makeText(Homeactivity.this,d,Toast.LENGTH_SHORT).show();
                if (Common.alert.equals("1"))
                {
                    l.setVisibility(View.INVISIBLE);
                    alert.setVisibility(View.VISIBLE);
                }
                else{
                    l.setVisibility(View.VISIBLE);
                    alert.setVisibility(View.INVISIBLE);
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        ref.child("100001").child("accidentpi").child("d").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common.alert= dataSnapshot.getValue(String.class);
                d=Common.alert;
                Toast.makeText(Homeactivity.this,d,Toast.LENGTH_SHORT).show();
                if (Common.alert.equals("1"))
                {
                    Intent i = new Intent();
                    i.setClass(Homeactivity.this, CheckCertainty.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });


        
    }
    static String s="";
    Boolean back_flag=false;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!back_flag) {
            l.setVisibility(View.VISIBLE);
            alert.setVisibility(View.INVISIBLE);
            back_flag=true;
        }
        else{
            finish();
        }
    }
}
