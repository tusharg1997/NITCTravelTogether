package com.example.nitctraveltogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    EditText email;
    EditText password;
    EditText fname;
    EditText lname;
    EditText phone;
    EditText age;
    RadioButton male,female;

    DatabaseReference databaseuser;
    DatabaseReference databaseuser1;
    public void sendtologin(View view){
        Intent i = new Intent(this, MainActivity.class);
        try {
            FirebaseAuth.getInstance().signOut();
        } catch (Exception e) {
            Toast.makeText(Registration.this,"error in signout",Toast.LENGTH_SHORT).show();
        }
        startActivity(i);
        finish();
    }
    public void saveToDatabase(String remail, String rpassword, String rfname, String rlname, String rphone, String rage, String rgender){
        String id = remail.substring(0,remail.length()-11);
        User user = new User(remail,rpassword, rfname, rlname, rphone, rage, rgender );

        databaseuser.child(id).setValue(user);
        userrating userr=new userrating(0,-1);
        databaseuser1.child(id).setValue(userr);
        Toast.makeText(this, "Registered Successfully, please go to your email and verify your email id.", Toast.LENGTH_LONG).show();

    }

    boolean validate(String remail, String rpassword, String rfname, String rlname, String rphone, String rage ,String rgender)
    {
        boolean flag=true;
        if(remail.isEmpty() || rpassword.isEmpty() || rfname.isEmpty() || rlname.isEmpty() || rphone.isEmpty() || rage.isEmpty()||rgender.isEmpty())
        {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(remail.length()<=12){
            Toast.makeText(this, "Wrong email id", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(rphone.length()!=10){
            Toast.makeText(this, "Enter valid phone no.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else {
            String str = remail.substring(email.length() - 11);
            if (!str.equalsIgnoreCase("@nitc.ac.in")) {
                Toast.makeText(this, "Enter NITC email id", Toast.LENGTH_SHORT).show();
                flag = false;
            }
        }
        return flag;
    }
    public void register(View view){
        email = findViewById(R.id.remail);
        password = findViewById(R.id.rpass);
        fname = findViewById(R.id.rname);
        lname = findViewById(R.id.rlname);
        age = findViewById(R.id.rage);
        phone = findViewById(R.id.rphone);
        male=findViewById(R.id.rgenderm);
        female=findViewById(R.id.rgenderf);
        final String remail=email.getText().toString();
        final String rpassword=password.getText().toString();
        final String rfname=fname.getText().toString();
        final String rlname=lname.getText().toString();
        final String rphone=phone.getText().toString();
        final String rage=age.getText().toString();
        String rgender = "";
        if(male.isChecked())
            rgender=male.getText().toString();
        else if(female.isChecked())
            rgender=female.getText().toString();
        final String frgender=rgender;
        Toast.makeText(Registration.this,"Hello"+frgender,Toast.LENGTH_SHORT);
        if(validate(remail, rpassword, rfname, rlname, rphone, rage,rgender) == false){
            return;
        }
        mAuth.createUserWithEmailAndPassword(remail, rpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        FirebaseUser user = mAuth.getCurrentUser();

//                                        Toast.makeText(Registration.this, "Email Registered, Please check your email for verification. Key="+user.getUid(),
//                                                Toast.LENGTH_SHORT).show();
                                        saveToDatabase(remail, rpassword, rfname, rlname, rphone, rage,frgender);



                                    }else{
                                        Toast.makeText(Registration.this, task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        } else {

                            Toast.makeText(Registration.this, "Authentication failed."+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.remail);
        password = findViewById(R.id.rpass);
        fname = findViewById(R.id.rname);
        lname = findViewById(R.id.rlname);
        age = findViewById(R.id.rage);
        phone = findViewById(R.id.rage);
        male=findViewById(R.id.rgenderm);
        female=findViewById(R.id.rgenderf);
       FirebaseDatabase data =  FirebaseDatabase.getInstance();
       databaseuser = FirebaseDatabase.getInstance().getReference("User");
       databaseuser1 = FirebaseDatabase.getInstance().getReference("rating");


    }
}
