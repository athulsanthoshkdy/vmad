package com.coduniq.dmad.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.coduniq.dmad.R;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE=2;
    private Uri uri=null;
    private int check=0;

    private ImageView prof;
    private User user;
    TextView id,name,contact,health,blood;
    String n,c,i,b,h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        id=findViewById(R.id.uid);
        name=findViewById(R.id.name);
        contact=findViewById(R.id.contact);
        blood=findViewById(R.id.blood);
        health=findViewById(R.id.health);
        prof=findViewById(R.id.profile);
        n=name.getText().toString();
        c=contact.getText().toString();
        i=id.getText().toString();
        b=blood.getText().toString();
        h=health.getText().toString();
        Button bt=findViewById(R.id.btnloc);
        databaseRef= FirebaseDatabase.getInstance().getReference("users").child("100001");
        if (Common.profileFlag){
            Common.profileFlag=false;
            bt.setVisibility(View.VISIBLE);
            databaseRef= FirebaseDatabase.getInstance().getReference("users").child(Common.id);
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(Common.url));
                startActivity(intent);
            }
        });

        databaseRef.child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Common.us=dataSnapshot.getValue(User.class);
                user=Common.us;
                Toast.makeText(ProfileActivity.this, user.getName(), Toast.LENGTH_SHORT).show();
                if (user!=null){
                    id.setText(i+user.getUid());
                    name.setText(n+user.getName());
                    contact.setText(c+user.getContact());
                    health.setText(h+user.getHealth());
                    blood.setText(b+user.getBlood());

                    if (!user.getImageurl().equals("not-set"))
                        Picasso.with(ProfileActivity.this).load(user.getImageurl()).into(prof);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });


        User u=Common.us;

        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST_CODE);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //image from gallery result
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            check=1;
            uri = data.getData();
            prof.setImageURI(uri);

            ppupload();
        }
    }

    private void ppupload() {
        if (check == 0) {
            Toast.makeText(ProfileActivity.this, "Image not selected for upload", Toast.LENGTH_SHORT).show();
        }
        else{
            // Toast.makeText(PostActivity.this, "posting......", Toast.LENGTH_SHORT).show();
            final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMax(100); // Progress Dialog Max Value
            progressDialog.setMessage("Please wait while uploading"); // Setting Message
            progressDialog.setTitle("Uploading"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            String s = "Display_Pictures";
            final StorageReference filepath = mStorageRef.child(s).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int currentProgress = (int) progress;
                    //  progressBar.setProgress(currentProgress);
                    progressDialog.setProgress(currentProgress);
                }
            });


            filepath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public Task<Uri> then(@NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NotNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        final Uri downUri = task.getResult();
                        final DatabaseReference newPost = databaseRef;
                        if (downUri != null) {
                            newPost.child("dp").setValue(downUri.toString());
//                                        newPost.child("uid").setValue(mCurrentUser.getUid());
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Unsuccesfull image upload", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else                             Toast.makeText(getApplicationContext(), "Unsuccesfull image upload", Toast.LENGTH_SHORT).show();
                }
            });
                            /*filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //getting the post image download url 
                                    @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    Toast.makeText(getApplicationContext(), "Succesfully uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });*/
        }
    }

    private StorageReference mStorageRef;
    private DatabaseReference databaseRef,mDatabaseUsers;


    @Override
    protected void onStart() {
        super.onStart();
        if (!isInternetConnection()){
            //startActivity(new Intent(this,MultipActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isInternetConnection()){
           // startActivity(new Intent(this,MultipActivity.class));
        }
    }

    public boolean isInternetConnection()
    {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}
