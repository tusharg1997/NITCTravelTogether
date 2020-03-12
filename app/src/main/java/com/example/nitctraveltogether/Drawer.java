package com.example.nitctraveltogether;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class Drawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private Bitmap compressedImageFile;


    DatabaseReference databaseuser;

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
                R.id.nav_home, R.id.needlift, R.id.sharefare, R.id.logout, R.id.map)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


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
        if (currentUser == null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();

        }
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
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
