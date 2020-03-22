package com.example.nitctraveltogether.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitctraveltogether.Api;
import com.example.nitctraveltogether.Offer;
import com.example.nitctraveltogether.R;
import com.example.nitctraveltogether.ui.gallery.GalleryViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShareFareNeedLift#newInstance} factory method to
 * create an instance of this fragment.
 */
 class OfferShare{
    String email, destination, time;
    OfferShare(String e, String d, String t)
    {
        email = e;
        destination = d;
        time = t;
    }
}
public class ShareFareNeedLift extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView name, email, aseats, tov,destination,age,gender;

    String rage="Age :";
    String rgender="Gender : ";
    Dialog mydialog;
    int count = 0;
    private GalleryViewModel galleryViewModel;
    ListView lv;
    ProgressDialog pb;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference databaseuser;
    DatabaseReference databaseuser1;
    DatabaseReference databaseuser2;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String user;
    String token=null;
    String tokenid;
    public ShareFareNeedLift() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareFareNeedLift.
     */
    // TODO: Rename and change types and number of parameters
    public static ShareFareNeedLift newInstance(String param1, String param2) {
        ShareFareNeedLift fragment = new ShareFareNeedLift();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    private void sendnotification(String sender)
    {
        String title="Request for lift from "+sender;
        String body="I want to travel with you";
        Toast.makeText(getActivity(), "Inside send notification, token:"+token, Toast.LENGTH_SHORT).show();
        //Hosting Url-https://nitctraveltogether-a535a.firebaseapp.com
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://nitctraveltogether-a535a.firebaseapp.com/api/").addConverterFactory(GsonConverterFactory.create()).build();

        Api api=retrofit.create(Api.class);
        Call<ResponseBody> call=api.sendNotification(token,title,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(getActivity(),response.body().string(),Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    public void doremainingthing(List<OfferShare> list, int i) {
        TextView txtclose;
        final Button btnFollow;
        TextView name, email, aseats, tov, destination, age, gender;
        mydialog.setContentView(R.layout.custompopup1);
        txtclose = (TextView) mydialog.findViewById(R.id.txtclose);
        name = (TextView) mydialog.findViewById(R.id.name);
        email = (TextView) mydialog.findViewById(R.id.email);
        age = (TextView) mydialog.findViewById(R.id.age);
        gender = (TextView) mydialog.findViewById(R.id.gender);
        destination = (TextView) mydialog.findViewById(R.id.destination);
        email.setText("Email: " + list.get(i).email);
        final String receiveremail = list.get(i).email.substring(0, list.get(i).email.length() - 11);
        databaseuser1 = FirebaseDatabase.getInstance().getReference("request").child(receiveremail);
        name.setText("Name : " + getName(list.get(i).email));
        destination.setText("Destination: " + list.get(i).destination);
        txtclose.setText("X");
        age.setText(rage);
        gender.setText(rgender);
        btnFollow = (Button) mydialog.findViewById(R.id.btnsendrequest);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

        int x;
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
        final ArrayList<String> ls = new ArrayList<String>();

        // send notification
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                final String senderemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                // second listener start

                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                Date now = new Date();
                String time = format.format(now);
                String key = senderemail.substring(0,senderemail.length()-11);
                databaseuser1.child(key).setValue(time);
                Toast.makeText(getActivity(),"Request sent",Toast.LENGTH_SHORT).show();
                databaseuser2 = FirebaseDatabase.getInstance().getReference("tokens");

                databaseuser2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            String key=data.getKey().toString();
                            String value=data.getValue().toString();
                            if(key.equalsIgnoreCase(receiveremail))
                            { token = value;}
                        }
                        sendnotification(senderemail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });// over

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_share_fare_need_lift, container, false);
        databaseuser = FirebaseDatabase.getInstance().getReference("ShareFareOfferLift");
        mydialog = new Dialog(getActivity());
        int x;
        pb = new ProgressDialog(getActivity());
        lv  = root.findViewById(R.id.listview);
        pref=getActivity().getSharedPreferences("user",MODE_PRIVATE);
        final String senderemail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String key = senderemail.substring(0,senderemail.length()-11);
        databaseuser1 = FirebaseDatabase.getInstance().getReference("request");
        editor=pref.edit();
        user=pref.getString("email_id",null);
        final List<String> list1 = new ArrayList<>();
        final List<OfferShare> list = new ArrayList<>();
        pb.setMessage("Loading .....");
        pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pb.setIndeterminate(true);
        pb.setProgress(0);
        pb.show();
        databaseuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                for(String key: dataMap.keySet()){
                    Object data = dataMap.get(key);
                    try{

                        HashMap<String,Object> userData = (HashMap<String,Object>) data;
                        String offertime = userData.get("time").toString();
                        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                        Date d1= format.parse(offertime);
                        Date now = new Date();
                        long duration  = now.getTime() - d1.getTime();
                        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
                        if(diffInHours> 1)
                            continue;
                        OfferShare offer = new OfferShare( (String)userData.get("email"), (String) userData.get("destination"), (String) userData.get("time"));
                        if(!offer.email.equalsIgnoreCase(senderemail))
                            list.add(offer);
                    }
                    catch (Exception e){
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                for(int i=0;i<list.size();i++)
                {
                    String [] arr= list.get(i).time.split(" ",2);
                    list1.add(list.get(i).email + "\n" + list.get(i).destination +"                "+   arr[1]);

                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, list1);
                lv.setAdapter(arrayAdapter);
                pb.dismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                // taking age and gender from user table
                DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("User").
                        child(list.get(i).email.substring(0, list.get(i).email.length() - 11));
                userdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getKey().toString().equalsIgnoreCase("age"))
                                rage = ("Age : " + data.getValue().toString());
                            if (data.getKey().toString().equalsIgnoreCase("gender"))
                                rgender = ("Gender: " + data.getValue().toString());
                        }
                        doremainingthing(list, i);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });//database listener close

            }// onclick close
        });//listener close



        return root;
    }
}
