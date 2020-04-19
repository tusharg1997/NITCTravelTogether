package com.nitc.nitctraveltogether.ui.slideshow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nitc.nitctraveltogether.AdapterIncomingRequest;
import com.nitc.nitctraveltogether.Api;
import com.nitc.nitctraveltogether.ModelClassIncomingRequest;
import com.nitc.nitctraveltogether.R;
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
    private RecyclerView recyclerView;
    ProgressDialog pb1;
    String token=null;
    TextView msg;
    Dialog mydialog;
    DatabaseReference databaseuser;
    DatabaseReference databaseuser2;
    DatabaseReference databaseuser1;
    DatabaseReference databaseuseracceptor;
    DatabaseReference databaseuserremove;
    DatabaseReference databaseuserted;
    AdapterIncomingRequest adapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private SlideshowViewModel slideshowViewModel;
    ArrayList<ModelClassIncomingRequest>  modelClassList;
    ListView list;
    String AcceptorEmail = "";
    String AcceptorContact="";
    ArrayAdapter<String> arrayAdapter;
    //Sending Notification

    private void sendacceptnotification(String contactNo,String tokenemail, int i,String name)
    {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String title="Request accepted";

        String body="Your request to travel\nhas been accepted From: " + email+ "\n The Acceptor Contact Number is " + contactNo;
       // Toast.makeText(getActivity(), "Inside send notification, token:"+token, Toast.LENGTH_SHORT);

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
                    modelClassList.remove(i);
                    adapter.notifyItemRemoved(i);
                    if(modelClassList.size()==0)
                        msg.setVisibility(View.VISIBLE);
                    mydialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Request Accepted").setMessage("You have successfully accepted the request of "+name+".\nPlease go to current active option to see your co-traveller details.")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
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

    private void sendrejectnotification(String tokenemail, int i)
    {
        String title="Request rejected";
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        String body="Your request to travel has been rejected from\n" + email;
      //  Toast.makeText(getActivity(), "Inside send notification, token:"+token, Toast.LENGTH_SHORT);
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
                    modelClassList.remove(i);
                    if(modelClassList.size()==0)
                        msg.setVisibility(View.VISIBLE);
                    adapter.notifyItemRemoved(i);
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

    public void doRemainingWork( int i,String rage, String remail, String rfirstname, String rgender, String rlastname, String rphone){
        TextView txtclose;
        //Toast.makeText(getActivity(), "clicked + " + rage+" " + remail, Toast.LENGTH_SHORT).show();

        final Button btnFollow,accept,reject;
        TextView name, email,age,gender,contactt;

        mydialog.setContentView(R.layout.popuprequest);
        txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
        name =(TextView) mydialog.findViewById(R.id.name);
        email =(TextView) mydialog.findViewById(R.id.email);
        age = (TextView) mydialog.findViewById(R.id.age);
        gender = (TextView) mydialog.findViewById(R.id.gender);
        //contactt = (TextView) mydialog.findViewById(R.id.number);
        age.setText(rage);
        gender.setText(rgender);
        email.setText("Email: "+ remail);
        txtclose.setText("X");
        rfirstname=rfirstname.substring(0, 1).toUpperCase() + rfirstname.substring(1);
        rlastname=rlastname.substring(0, 1).toUpperCase() + rlastname.substring(1);
        name.setText("Name : "+ rfirstname+" "+rlastname);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });
        String temail=modelClassList.get(i).getEmail();
        String tokenemail=temail.substring(7,temail.length()-11);
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
        String finalRlastname = rlastname;
        String finalRfirstname = rfirstname;
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb1.setMessage("Accepting the request.....\nDo not press back button");
                pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb1.setIndeterminate(true);
                pb1.setProgress(0);
                pb1.show();


               // pb.dismiss();
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
                       // contactt.setText("Contact " + rphone);
                        //Toast.makeText(getActivity(), "Go to Current Active to see Requester Details and contact number", Toast.LENGTH_LONG).show();
                        sendacceptnotification(AcceptorContact,tokenemail, i, finalRfirstname +" "+ finalRlastname);

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
                pb1.setMessage("Rejecting the request.....\nDo not press back button");
                pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb1.setIndeterminate(true);
                pb1.setProgress(0);
                pb1.show();



               // pb.dismiss();
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
                        sendrejectnotification(tokenemail,i);
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
        recyclerView=(RecyclerView) root.findViewById(R.id.requests);
        msg = root.findViewById(R.id.msg);
        String currentemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentemail = currentemail.substring(0,currentemail.length()-11);
        databaseuser = FirebaseDatabase.getInstance().getReference("request").child(currentemail);
        mydialog = new Dialog(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        pb = new ProgressDialog(getActivity());
        pb1 = new ProgressDialog(getActivity());
        pb.setMessage("Loading .....");
        pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pb.setIndeterminate(true);
        pb.setProgress(0);
        pb.show();
        modelClassList = new ArrayList<>();
      

        final String finalCurrentemail = currentemail;
        databaseuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    pb.dismiss();
                    msg.setVisibility(View.VISIBLE);
                }
               else if (dataSnapshot.exists()) {
                    pb.dismiss();
                    if ((int) dataSnapshot.getChildrenCount() == 0) {
                        pb.dismiss();
                         adapter = new AdapterIncomingRequest(modelClassList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setHasFixedSize(true);
                        adapter.notifyDataSetChanged();
                        msg.setVisibility(View.VISIBLE);
                    } else {
                        int f=0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            try {
                                String email = ds.getKey();
                                String time = ds.getValue().toString();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date d1 = format.parse(time);
                                Date now = new Date();
                                long duration = now.getTime() - d1.getTime();
                                long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
                                if (diffInHours >= 1)
                                    continue;
                                f=1;
                               // Toast.makeText(getActivity(), ds.getKey(), Toast.LENGTH_SHORT).show();
                                String times[] = time.split(" ", 2);
                                modelClassList.add(new ModelClassIncomingRequest("Email: " + email + "@nitc.ac.in", "Time: " + times[1]));

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter = new AdapterIncomingRequest(modelClassList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setHasFixedSize(true);
                        adapter.notifyDataSetChanged();
                        msg.setVisibility(View.INVISIBLE);
                        if(f==0)
                            msg.setVisibility(View.VISIBLE);
                        pb.dismiss();
                        adapter.setOnItemClickListener(new AdapterIncomingRequest.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                pb.setMessage("Loading .....");
                                pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                pb.setIndeterminate(true);
                                pb.setProgress(0);
                                pb.show();
                                String email = modelClassList.get(position).getEmail();
                                email = email.substring(7, email.length() - 11);
                                DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("User").
                                        child(email);
                                userdata.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String age = "", email = "", firstname = "", gender = "", lastname = "", phone = "";
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            if (data.getKey().toString().equalsIgnoreCase("age"))
                                                age = ("Age : " + data.getValue().toString());
                                            if (data.getKey().toString().equalsIgnoreCase("gender"))
                                                gender = ("Gender: " + data.getValue().toString());
                                            if (data.getKey().toString().equalsIgnoreCase("firstName"))
                                                firstname = (data.getValue().toString());
                                            if (data.getKey().toString().equalsIgnoreCase("lastName"))
                                                lastname = (data.getValue().toString());
                                            if (data.getKey().toString().equalsIgnoreCase("phone"))
                                                phone = (data.getValue().toString());
                                            if (data.getKey().toString().equalsIgnoreCase("email_id"))
                                                email = (data.getValue().toString());
                                        }
                                        // now call a function
                                        doRemainingWork( position, age, email, firstname, gender, lastname, phone);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}
