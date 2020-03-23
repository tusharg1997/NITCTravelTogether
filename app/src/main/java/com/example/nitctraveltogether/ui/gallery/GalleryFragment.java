package com.example.nitctraveltogether.ui.gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
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

import org.w3c.dom.Text;

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

import static android.content.Context.MODE_PRIVATE;
class request{
    public String senderemail;
    public String receiveremail;

    public request(String semail,String remail)
    {
        this.senderemail=semail;
        this.receiveremail=remail;
    }
}
public class GalleryFragment extends Fragment {

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

    public void doremaining(List<Offer> list, int i){
        age.setText(rage);
        gender.setText(rgender);
        aseats =(TextView) mydialog.findViewById(R.id.availableseats);
        destination =(TextView) mydialog.findViewById(R.id.destination);
        tov =(TextView) mydialog.findViewById(R.id.typeofvehicle);
        email.setText("Email: "+ list.get(i).email);
        aseats.setText("No. of Seats "+ list.get(i).availableSeats);
        final String receiveremail=list.get(i).email.substring(0,list.get(i).email.length()-11);
        databaseuser1 = FirebaseDatabase.getInstance().getReference("request").child(receiveremail);
        tov.setText("Type of Vehicle "+ list.get(i).vehicleType);

        name.setText("Name : "+ getName(list.get(i).email));
        destination.setText("Destination "+ list.get(i).destination);
        pb.dismiss();
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();


    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseDatabase data =  FirebaseDatabase.getInstance();
        databaseuser = FirebaseDatabase.getInstance().getReference("OfferLift");
        mydialog = new Dialog(getActivity());
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        int x;
        pb = new ProgressDialog(getActivity());
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        lv  = root.findViewById(R.id.listview);

        pref=getActivity().getSharedPreferences("user",MODE_PRIVATE);
        final String senderemail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String key = senderemail.substring(0,senderemail.length()-11);
        databaseuser1 = FirebaseDatabase.getInstance().getReference("request");
        editor=pref.edit();
        user=pref.getString("email_id",null);
        final List<String> list1 = new ArrayList<>();
        final List<Offer> list = new ArrayList<>();
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
                        Offer offer = new Offer( (String)userData.get("email"), (String) userData.get("destination"),
                                (String) userData.get("availableSeats"), (String) userData.get("vehicleType"),(String) userData.get("time"));
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
                TextView txtclose;
                final Button btnFollow;

                mydialog.setContentView(R.layout.custompopup);
                txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
                name =(TextView) mydialog.findViewById(R.id.name);
                email =(TextView) mydialog.findViewById(R.id.email);
                age = (TextView) mydialog.findViewById(R.id.age);
                gender = (TextView) mydialog.findViewById(R.id.gender);
                // accessing age and gender from user table
                pb.setMessage("Loading .....");
                pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb.setIndeterminate(true);
                pb.setProgress(0);
                pb.show();
                DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("User").
                        child(list.get(i).email.substring(0,list.get(i).email.length()-11));
                userdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            if(data.getKey().toString().equalsIgnoreCase("age"))
                                rage = ("Age : " + data.getValue().toString());
                            if(data.getKey().toString().equalsIgnoreCase("gender"))
                            rgender = ("Gender: "+ data.getValue().toString());
                        }
                    doremaining(list, i);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                txtclose.setText("X");
                btnFollow = (Button) mydialog.findViewById(R.id.btnsendrequest);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });
                 int x;

                final String receiveremail=list.get(i).email.substring(0,list.get(i).email.length()-11);
                final ArrayList<String> ls = new ArrayList<String>();
                btnFollow.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {

                        final String senderemail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
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
                        });

//                        while(token==null){}
//                        sendnotification();
                    }
                });
            }
        });
        return root;
    }

    //Notification Code
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
}
