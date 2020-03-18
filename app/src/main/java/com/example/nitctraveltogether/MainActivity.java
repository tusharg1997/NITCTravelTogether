package com.example.nitctraveltogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
public class  MainActivity extends AppCompatActivity implements  View.OnKeyListener, View.OnClickListener{

    private FirebaseAuth mAuth;
    ProgressDialog pb;
    AutoCompleteTextView emailv,passwordv;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    public boolean onKey(View view, int i, KeyEvent keyEvent){
        if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            login(view);
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.main_activity ){
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = new ProgressDialog(this);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#ff000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {Intent i=new Intent(MainActivity.this, Drawer.class);
        startActivity(i);
        finish();}
        emailv= findViewById(R.id.email);
        passwordv=findViewById(R.id.password);
        passwordv.setOnKeyListener(this);
        ConstraintLayout layout = findViewById(R.id.main_activity);
        layout.setOnClickListener(this);
    }
    //Register Code
    public void register(View view)
    {
        Intent i = new Intent(this, Registration.class );
        startActivity(i);

    }
    boolean validateInput(String email, String pass){
        boolean flag = true;
        if(email.isEmpty() || pass.isEmpty()){
            flag = false;
            Toast.makeText(this, "Enter All the Fields", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }
    //Login Code
    public void login(View view)
    {


        String email=emailv.getText().toString();
        String password=passwordv.getText().toString();
        // check whether both filled are full or not
            if(validateInput(email,password) == false){

                return;
            }
        pb.setMessage("Login .....");
        pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pb.setIndeterminate(true);
        pb.setProgress(0);
        pb.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(MainActivity.this, "Authentication Success.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                String emailid=email.substring(0,email.length()-11);
                                //progressDialog.dismiss();
                                    pb.dismiss();
                                    Intent i = new Intent(MainActivity.this, Drawer.class);
                                    startActivity(i);
                                    finish();



                            }
                            else{
                                pb.dismiss();
                                Toast.makeText(MainActivity.this, "Please verify your email.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            pb.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Wrong Username or Password.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }



}
