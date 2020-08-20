package com.coduniq.dmad.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coduniq.dmad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrowsinessActivity extends AppCompatActivity {

    private DatabaseReference ref;
    String d;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drowsiness);

        b=findViewById(R.id.detect);
        ref= FirebaseDatabase.getInstance().getReference("users");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b.getText().equals("enable drowsiness detection")){
                    b.setText("disable drowsiness detection");
                    ref.child("100001").child("drowsinessEnable").child("detect").setValue("1");

                }
                else{
                    b.setText("enable drowsiness detection");
                    ref.child("100001").child("drowsinessEnable").child("detect").setValue("0");

                }
            }
        });


        ref.child("100001").child("drowsinessEnable").child("detect").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common.detect= dataSnapshot.getValue(String.class);
                d=Common.detect;
                Toast.makeText(DrowsinessActivity.this,d,Toast.LENGTH_SHORT).show();
                if (Common.detect.equals("0")){
                    b.setText("enable drowsiness detection");
                }
                else{
                    b.setText("disable drowsiness detection");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

    }
}
