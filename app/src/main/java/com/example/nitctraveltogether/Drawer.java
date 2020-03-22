package com.example.nitctraveltogether;

import android.app.SearchManager;
import android.content.Context;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class Drawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private Bitmap compressedImageFile;


    DatabaseReference databaseuser;
    DatabaseReference databaseuser1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#ff000000"));
//        actionBar.setBackgroundDrawable(colorDrawable);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.needlift, R.id.sharefareneedlift,R.id.sharefareneedlift, R.id.logout, R.id.map, R.id.reportandrating)
                .setDrawerLayout(drawer)
                .build();
        View hView = navigationView.getHeaderView(0);
        TextView email = (TextView) hView.findViewById(R.id.email);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    

        //Notification Code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Tushar";
            String description = "Testing";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("tushar_m180499ca", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        //Notification Code


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                    //Toast.makeText(Drawer.this,"Token is:"+token,Toast.LENGTH_SHORT).show();
                    savetoken(token);
                } else {
                    Toast.makeText(Drawer.this, "error generating the token " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Saving unique token to database
    public void savetoken(String s)
    {
        String temail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String email=temail.substring(0,temail.length()-11);
        databaseuser1= FirebaseDatabase.getInstance().getReference("tokens");
        databaseuser1.child(email).setValue(s);
    }
    public void shareFareOfferLift(View view){
        EditText destination = findViewById(R.id.offerlift);
        String desti = destination.getText().toString();
        if(desti.isEmpty()){
            Toast.makeText(this, "Please Enter Your destination", Toast.LENGTH_SHORT).show();
            return;
        }
        else
            Toast.makeText(this, desti, Toast.LENGTH_SHORT).show();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String remail = currentUser.getEmail();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String id1 = remail.substring(0,remail.length()-11);
        DatabaseReference databaseuser = FirebaseDatabase.getInstance().getReference("ShareFareOfferLift");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("destination", desti);
        hashMap.put("time", time);
        hashMap.put("email", remail);
        databaseuser.child(id1).setValue(hashMap);
        Toast.makeText(this, "Lift offered Successfully", Toast.LENGTH_SHORT).show();


    }
    public void offerLift(View view) {
        EditText email = findViewById(R.id.email);
        EditText destination = findViewById(R.id.destination);
        EditText seats = findViewById(R.id.seat);
        EditText vehicle = findViewById(R.id.vehicle);
       // final String remail = email.getText().toString();
        final String rdestination = destination.getText().toString();
        final String rseats = seats.getText().toString();
        final String rvehicle = vehicle.getText().toString();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        String remail = currentUser.getEmail();
        if (validate(remail, rdestination, rseats, rvehicle) == false)
            return;
        //Toast.makeText(this, "email is" + id, Toast.LENGTH_SHORT).show();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
            String time = dateFormat.format(date);
        Offer offer = new Offer(remail, rdestination, rseats, rvehicle, time);
        String id1 = remail.substring(0,remail.length()-11);
        databaseuser.child(id1).setValue(offer);
        Toast.makeText(Drawer.this, "Successfully offered", Toast.LENGTH_SHORT).show();
    }

    boolean validate(String remail, String rdest, String rseats, String rvehicle) {

        boolean flag = true;
        if (remail.isEmpty() || rdest.isEmpty() || rseats.isEmpty() || rvehicle.isEmpty()) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        databaseuser = FirebaseDatabase.getInstance().getReference("OfferLift");
        mAuth = FirebaseAuth.getInstance();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       /* SearchView searchView = (SearchView) menu.findItem(R.id.).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}
