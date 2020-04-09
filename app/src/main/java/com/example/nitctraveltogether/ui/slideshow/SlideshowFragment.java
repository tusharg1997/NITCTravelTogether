package com.example.nitctraveltogether.ui.slideshow;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nitctraveltogether.Api;
import com.example.nitctraveltogether.Offer;
import com.example.nitctraveltogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SlideshowFragment extends Fragment {


    String getName(String name)
    {
        String temp="";
        for(int i=0;i<name.length();i++)
        {
            if(name.charAt(i)=='_')
                break;
            temp = temp + name.charAt(i);
        }
        return temp;
    }
    int f=0;
    ProgressDialog pb;
    ProgressDialog pb1;
    String rage="Age :";
    String rgender="Gender : ";
    String contact="";
    String token=null;
    TextView msg;
    Dialog mydialog;
    DatabaseReference databaseuser;
    DatabaseReference databaseuser2;
    DatabaseReference databaseuser1;
    DatabaseReference databaseuseracceptor;
    DatabaseReference databaseuserremove;
    DatabaseReference databaseuserted;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private SlideshowViewModel slideshowViewModel;
    ListView list;
    String AcceptorEmail = "";
    String AcceptorContact="";
    ArrayAdapter<String> arrayAdapter;
    //Sending Notification

    private void sendacceptnotification(String contactNo,String tokenemail)
    {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String title="Request accepted";
        String body="Your request to travel\nhas been accepted From: " + email+ "\n The Acceptor Contact Number is " + contactNo;
        Toast.makeText(getActivity(), "Inside send notification, token:"+token, Toast.LENGTH_SHORT);
        //Hosting Url-https://nitctraveltogether-a535a.firebaseapp.com
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://nitctraveltogether-a535a.firebaseapp.com/api/").addConverterFactory(GsonConverterFactory.create()).build();

        Api api=retrofit.create(Api.class);
        Call<ResponseBody> call=api.sendNotification(token,title,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(getActivity(),response.body().string(),Toast.LENGTH_SHORT);
                    pb1.dismiss();
                    Toast.makeText(getActivity(),"Accept Notification sent successfully",Toast.LENGTH_SHORT).show();
                   // mydialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //Current active
        try {


                databaseuseracceptor = FirebaseDatabase.getInstance().getReference("currentactive").child(email.substring(0, email.length() - 11));
                databaseuseracceptor.child(tokenemail).setValue(tokenemail+"@nitc.ac.in");
            FirebaseDatabase.getInstance().getReference("request").child(email.substring(0, email.length() - 11)).child(tokenemail).removeValue();
            databaseuserremove = FirebaseDatabase.getInstance().getReference("currentactive").child(tokenemail);
            databaseuserremove.child(email.substring(0, email.length() - 11)).setValue(email);
        } catch (Exception e) {
            Toast.makeText(getActivity(),"Error in current active", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendrejectnotification(String tokenemail)
    {
        String title="Request rejected";
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String body="Your request to travel has been rejected from\n" + email;
        Toast.makeText(getActivity(), "Inside send notification, token:"+token, Toast.LENGTH_SHORT);
        //Hosting Url-https://nitctraveltogether-a535a.firebaseapp.com
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://nitctraveltogether-a535a.firebaseapp.com/api/").addConverterFactory(GsonConverterFactory.create()).build();

        Api api=retrofit.create(Api.class);
        Call<ResponseBody> call=api.sendNotification(token,title,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(getActivity(),response.body().string(),Toast.LENGTH_SHORT);
                    Toast.makeText(getActivity(),"Reject Notification sent successfully",Toast.LENGTH_SHORT).show();
                    pb1.dismiss();
                    mydialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        FirebaseDatabase.getInstance().getReference("request").child(email.substring(0, email.length() - 11)).child(tokenemail).removeValue();
    }

    public void doRemainingWork(List<String> ls, int i){
        TextView txtclose;
        final Button btnFollow,accept,reject;
        TextView name, email,age,gender,contactt;
        mydialog.setContentView(R.layout.popuprequest);
        txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
        name =(TextView) mydialog.findViewById(R.id.name);
        email =(TextView) mydialog.findViewById(R.id.email);
        age = (TextView) mydialog.findViewById(R.id.age);
        gender = (TextView) mydialog.findViewById(R.id.gender);
        contactt = (TextView) mydialog.findViewById(R.id.number);
        age.setText(rage);
        gender.setText(rgender);
        email.setText("Email: "+ ls.get(i)+"@nitc.ac.in");
        txtclose.setText("X");
        name.setText("Name : "+ getName(ls.get(i)));
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });
        String temail=ls.get(i)+"@nitc.ac.in";
        String tokenemail=temail.substring(0,temail.length()-11);
        accept=mydialog.findViewById(R.id.accept);
        reject=mydialog.findViewById(R.id.reject);

        //When user accepts the request
        AcceptorEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String key = AcceptorEmail.substring(0, AcceptorEmail.length()-11);
        AcceptorContact="";
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User").child(key);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){

                    if(data.getKey().toString().equalsIgnoreCase("phone"))
                    {
                        AcceptorContact = data.getValue().toString();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb1.setMessage("Do not press back button...\nSending Notification....");
                pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb1.setIndeterminate(true);
                pb1.setProgress(0);
                pb1.show();

                databaseuser2 = FirebaseDatabase.getInstance().getReference("tokens");
                databaseuser2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            String key=data.getKey().toString();
                            String value=data.getValue().toString();
                            if(key.equalsIgnoreCase(tokenemail))
                            { token = value;}
                        }
                        contactt.setText("Contact " + contact);
                        sendacceptnotification(AcceptorContact,tokenemail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb1.setMessage("Do not press back button...\nSending Notification....");
                pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb1.setIndeterminate(true);
                pb1.setProgress(0);
                pb1.show();
                databaseuser1 = FirebaseDatabase.getInstance().getReference("tokens");

                databaseuser1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            String key=data.getKey().toString();
                            String value=data.getValue().toString();
                            if(key.equalsIgnoreCase(tokenemail))
                            { token = value;}
                        }
                        sendrejectnotification(tokenemail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pb.dismiss();
        mydialog.show();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        list=root.findViewById(R.id.requests);
        msg = root.findViewById(R.id.msg);
        String currentemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentemail = currentemail.substring(0,currentemail.length()-11);
        databaseuser = FirebaseDatabase.getInstance().getReference("request").child(currentemail);
        mydialog = new Dialog(getActivity());
        pb = new ProgressDialog(getActivity());
        pb1 = new ProgressDialog(getActivity());
        pb.setMessage("Loading .....");
        pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pb.setIndeterminate(true);
        pb.setProgress(0);
        pb.show();
        //
        final List<String> ls = new ArrayList<>();
        final List<String> lsemail = new ArrayList<>();
        final String finalCurrentemail = currentemail;
        databaseuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {

                        String email = ds.getKey().toString();
                        String time = ds.getValue().toString();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date d1 = format.parse(time);
                        Date now = new Date();
                        long duration = now.getTime() - d1.getTime();
                        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
                        if (diffInHours >= 1)
                            continue;
                        String times[] = time.split(" ", 2);
                        lsemail.add(email);
                        ls.add(email + "@nitc.ac.in" + "  " + times[1]);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

               arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ls);

                list.setAdapter(arrayAdapter);
                pb.dismiss();
                if(ls.size()==0) {
                    msg.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), "No Request Within One Hour", Toast.LENGTH_LONG).show();
                }
                else{
                    msg.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                pb.setMessage("Loading .....");
                pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb.setIndeterminate(true);
                pb.setProgress(0);
                pb.show();

                DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("User").
                        child(lsemail.get(i));
                userdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            if(data.getKey().toString().equalsIgnoreCase("age"))
                                rage = ("Age : " + data.getValue().toString());
                            if(data.getKey().toString().equalsIgnoreCase("gender"))
                                rgender = ("Gender: "+ data.getValue().toString());
                            if(data.getKey().toString().equalsIgnoreCase("phone"))
                                contact = data.getValue().toString();
                        }
                        // now call a function
                        doRemainingWork(lsemail,i);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                }
                // on item click close
                });
        return root;
    }
}
