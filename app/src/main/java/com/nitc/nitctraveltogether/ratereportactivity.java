package com.nitc.nitctraveltogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ratereportactivity extends AppCompatActivity {

    TextView name,email,gender,age;
    Button reportrate,bsearch,reportbutton;
    private FirebaseAuth mAuth;
    DatabaseReference databaseuser;
    DatabaseReference databaseuserforrating;
    DatabaseReference databaseuser1;
    DatabaseReference databaseuser2;
    String profileage,profilefname,profilelname,profilegender,profileemail,reporttext;
    EditText typedemail;
    RatingBar rating;
    EditText report;
    String currating;
    float rate,alreadyrated;
    int count=-1,fc;
    String feedback,feedbackemail;
    int f;
    boolean flag;
    ProgressDialog pb;
    int ratecount=0;
    int reportcount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratereportactivity);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#ff000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        String intentemail = getIntent().getExtras().getString("email");

        pb = new ProgressDialog(this);
        name=findViewById(R.id.name);
        gender=findViewById(R.id.gender);
        age=findViewById(R.id.age);
        typedemail=findViewById(R.id.editText2);
        email=findViewById(R.id.email);
        reportbutton=findViewById(R.id.reportuser);
        reportrate=findViewById(R.id.ratereport);

        report=findViewById(R.id.report);
        rating=findViewById(R.id.rating);
        bsearch=findViewById(R.id.search);
        typedemail.setText(intentemail);
        typedemail.setEnabled(false);
        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setMessage("Loading .....");
                pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb.setIndeterminate(true);
                pb.setProgress(0);
                pb.show();
                profileemail=typedemail.getText().toString().trim();
                if(profileemail.isEmpty() )
                {
                    pb.dismiss();
                    Toast.makeText(ratereportactivity.this,"Please type some email to search",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(profileemail.length()<=11){
                    pb.dismiss();
                    Toast.makeText(ratereportactivity.this, "Please Enter valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fsearchemail=profileemail.substring(0,profileemail.length()-11);
                databaseuserforrating=FirebaseDatabase.getInstance().getReference("rating").child(fsearchemail);
                databaseuserforrating.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getKey().toString().equalsIgnoreCase("rate"))
                                currating = (data.getValue().toString());
                        }
                        searchprofilefunction();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        reportrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratecount>0)
                {
                    Toast.makeText(ratereportactivity.this,"You cannot rate more than once",Toast.LENGTH_SHORT).show();
                    return;
                }
                ratecount++;
                rate=rating.getRating();
                //Toast.makeText(getActivity(),String.valueOf(rate),Toast.LENGTH_SHORT).show();
                // feedback=report.getText().toString();
                feedbackemail=typedemail.getText().toString().trim();

                savetodatabase();

            }
        });

        reportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportcount>0)
                {
                    Toast.makeText(ratereportactivity.this,"You cannot report more than once",Toast.LENGTH_SHORT).show();
                    return;
                }
                reportcount++;
                reporttext=report.getText().toString();
                feedbackemail=typedemail.getText().toString().trim();
                savereporttodatabase();
            }
        });
        bsearch.performClick();
    }
    public void searchprofilefunction()
    {

        flag = false;
        try{
            String fsearchemail=profileemail.substring(0,profileemail.length()-11);
            String ownemail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String finalownemail=ownemail.substring(0,ownemail.length()-11);
            if(finalownemail.equalsIgnoreCase(fsearchemail))
            {
                Toast.makeText(ratereportactivity.this,"Cannot rate or report your own Email",Toast.LENGTH_SHORT).show();
                age.setText("Cannot rate or report your own Email");
                pb.dismiss();
                return;
            }
            databaseuser = FirebaseDatabase.getInstance().getReference("User").child(fsearchemail);

            databaseuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.getKey().toString().equalsIgnoreCase("age"))
                            profileage = ("Age: " + data.getValue().toString());
                        if (data.getKey().toString().equalsIgnoreCase("gender"))
                            profilegender = (data.getValue().toString());
                        if (data.getKey().toString().equalsIgnoreCase("firstname"))
                            profilefname = data.getValue().toString();
                        if (data.getKey().toString().equalsIgnoreCase("lastname"))
                            profilelname = data.getValue().toString();
                        flag = true;
                    }
                    // now call a function
                    if(flag == false){
                        pb.dismiss();
                        Toast.makeText(ratereportactivity.this,"Entered email not found",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                        setfields();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            pb.dismiss();
            Toast.makeText(ratereportactivity.this,"Entered email not found",Toast.LENGTH_SHORT).show();
        }
    }

    public void setfields(){
        pb.dismiss();
        profilefname=profilefname.substring(0, 1).toUpperCase() + profilefname.substring(1);
        profilelname=profilelname.substring(0, 1).toUpperCase() + profilelname.substring(1);
        profilegender=profilegender.substring(0, 1).toUpperCase() + profilegender.substring(1);
        name.setText("Name: "+profilefname+" "+profilelname);
        email.setText("Email: "+profileemail);
        rating.setRating(Float.parseFloat(currating));
        gender.setText("Gender: "+profilegender);
        if(currating.length()>3)
            currating=currating.substring(0,3);
        age.setText(profileage+"         Rating: "+currating);
        reportrate.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);
        rating.setVisibility(View.VISIBLE);
        reportbutton.setVisibility(View.VISIBLE);
    }

    public void savetodatabase()
    {
        String fsearchemail=feedbackemail.substring(0,feedbackemail.length()-11);

        try {

            databaseuser1 = FirebaseDatabase.getInstance().getReference("rating").child(fsearchemail);
            databaseuser1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.getKey().toString().equalsIgnoreCase("count"))
                            count = Integer.parseInt(data.getValue().toString());
                        if (data.getKey().toString().equalsIgnoreCase("rate"))
                            alreadyrated = Float.parseFloat(data.getValue().toString());

                    }
                    // now call a function

                    save(fsearchemail);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });
        } catch (Exception e) {
            Toast.makeText(ratereportactivity.this,"Email id not in database",Toast.LENGTH_SHORT).show();
        }


    }
    public void save(String fsearchemail)
    {
        //String fsearchemail=feedbackemail.substring(0,feedbackemail.length()-11);
        count++;
        if(count>0)
            rate=(((count-1)*alreadyrated)+rate)/(count);
        userrating userr=new userrating(rate,count);
        databaseuser2 = FirebaseDatabase.getInstance().getReference("rating");
        databaseuser2.child(fsearchemail).child("rate").setValue(rate);
        if(count==0)
            count=1;
        databaseuser2.child(fsearchemail).child("count").setValue(count);
        Toast.makeText(ratereportactivity.this,"Rated successfully",Toast.LENGTH_SHORT).show();

    }

    public void savereporttodatabase()
    {
        String fsearchemail=feedbackemail.substring(0,feedbackemail.length()-11);
        final String reporteremail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String fremail=reporteremail.substring(0,reporteremail.length()-11);
        try {

            databaseuser1 = FirebaseDatabase.getInstance().getReference("report");
            databaseuser1.child(fsearchemail).child(fremail).setValue(reporttext);
            Toast.makeText(ratereportactivity.this,"Reported",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ratereportactivity.this,"Email id not in database",Toast.LENGTH_SHORT).show();
        }
    }

}
