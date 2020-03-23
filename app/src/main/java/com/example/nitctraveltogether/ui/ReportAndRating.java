package com.example.nitctraveltogether.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitctraveltogether.R;
import com.example.nitctraveltogether.userrating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportAndRating#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportAndRating extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportAndRating() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportAndRating.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportAndRating newInstance(String param1, String param2) {
        ReportAndRating fragment = new ReportAndRating();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report_and_rating, container, false);

        //SearchView searchprofile= getActivity().findViewById(R.id.search);

        pb = new ProgressDialog(getActivity());
        name=root.findViewById(R.id.name);
        gender=root.findViewById(R.id.gender);
        age=root.findViewById(R.id.age);
        typedemail=root.findViewById(R.id.editText2);
        email=root.findViewById(R.id.email);
        reportbutton=root.findViewById(R.id.reportuser);
        reportrate=root.findViewById(R.id.ratereport);

        report=root.findViewById(R.id.report);
        rating=root.findViewById(R.id.rating);
        bsearch=root.findViewById(R.id.search);
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
                    Toast.makeText(getActivity(),"Please type some email to search",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(profileemail.length()<=11){
                    pb.dismiss();
                    Toast.makeText(getActivity(), "Please Enter valid Email", Toast.LENGTH_SHORT).show();
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
                reporttext=report.getText().toString();
                feedbackemail=typedemail.getText().toString().trim();
                savereporttodatabase();
            }
        });
        return root;

    }

    public void searchprofilefunction()
    {

      flag = false;
        try{
            String fsearchemail=profileemail.substring(0,profileemail.length()-11);
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
                        Toast.makeText(getActivity(),"Entered email not found",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(),"Entered email not found",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(),"Email id not in database",Toast.LENGTH_SHORT).show();
        }


    }
    public void save(String fsearchemail)
    {
        //String fsearchemail=feedbackemail.substring(0,feedbackemail.length()-11);
        count++;
        rate=(((count-1)*alreadyrated)+rate)/(count);
        userrating userr=new userrating(rate,count);
        databaseuser2 = FirebaseDatabase.getInstance().getReference("rating");
        databaseuser2.child(fsearchemail).child("rate").setValue(rate);
        databaseuser2.child(fsearchemail).child("count").setValue(count);
        Toast.makeText(getActivity(),"Rated successfully",Toast.LENGTH_SHORT).show();

    }

    public void savereporttodatabase()
    {
        String fsearchemail=feedbackemail.substring(0,feedbackemail.length()-11);
        final String reporteremail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String fremail=reporteremail.substring(0,reporteremail.length()-11);
        try {

            databaseuser1 = FirebaseDatabase.getInstance().getReference("report");
            databaseuser1.child(fsearchemail).child(fremail).setValue(reporttext);
            Toast.makeText(getActivity(),"Reported",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(),"Email id not in database",Toast.LENGTH_SHORT).show();
        }
    }
}
