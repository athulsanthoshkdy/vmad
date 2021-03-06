package com.coduniq.dmad.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coduniq.dmad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckCertainty extends Activity implements LocationListener {
    private LocationManager lm;
    public double latitude, longitude;
    public String No1, No2;
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_certainty);

        FirebaseDatabase.getInstance().getReference("users").child("100001").child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common.us=dataSnapshot.getValue(User.class);
                user=Common.us;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try {
            File myFile = new File("/sdcard/.emergencyNumbers.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            No1 = myReader.readLine();
            No2 = myReader.readLine();
            myReader.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        final SmsManager sms = SmsManager.getDefault();
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(),No1, Toast.LENGTH_LONG).show();
                sms.sendTextMessage(No1, null, "Help! I've met with an accident at http://maps.google.com/?q="+ String.valueOf(latitude)+","+ String.valueOf(longitude), null, null);
                sms.sendTextMessage(No1, null, "Nearby Hospitals http://maps.google.com/maps?q=hospital&mrt=yp&sll="+ String.valueOf(latitude)+","+ String.valueOf(longitude)+"&output=kml", null, null);
                sms.sendTextMessage(No2, null, "Help! I've met with an accident at http://maps.google.com/?q="+ String.valueOf(latitude)+","+ String.valueOf(longitude), null, null);
                sms.sendTextMessage(No2, null, "Nearby Hospitals http://maps.google.com/maps?q=hospital&mrt=yp&sll="+ String.valueOf(latitude)+","+ String.valueOf(longitude)+"&output=kml", null, null);
                System.exit(1);
            }
        }, 15000);

        Button dismiss = (Button) findViewById(R.id.dismissB);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users").child("100001").child("accidentpi").child("d").setValue("0");
                System.exit(1);
            }
        });


    }
    int size=0;
    @Override
    public void onLocationChanged(final Location location){
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        FirebaseDatabase.getInstance().getReference("accident").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                size =(int)dataSnapshot.getChildrenCount();
                FirebaseDatabase.getInstance().getReference("accident").child(user.getUid()).child("userid").setValue(user.getUid());
                FirebaseDatabase.getInstance().getReference("accident").child(user.getUid()).child("lat").setValue(latitude);
                FirebaseDatabase.getInstance().getReference("accident").child(user.getUid()).child("long").setValue(longitude);
                FirebaseDatabase.getInstance().getReference("accident").child(user.getUid()).child("time").setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));

                FirebaseDatabase.getInstance().getReference("accidentstat").child(user.getUid()).child("title").setValue(user.getUid());
                FirebaseDatabase.getInstance().getReference("accidentstat").child(user.getUid()).child("desc").setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                FirebaseDatabase.getInstance().getReference("accidentstat").child(user.getUid()).child("imageUrl").setValue("http://maps.google.com/maps?q=loc:"+latitude+","+longitude);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(getApplicationContext(),"Lat and Long extracted"+latitude+","+longitude, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProviderDisabled(String provider){
    }
    @Override
    public void onProviderEnabled(String provider){
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}