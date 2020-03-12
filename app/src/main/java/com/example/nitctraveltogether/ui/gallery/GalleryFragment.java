package com.example.nitctraveltogether.ui.gallery;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.nitctraveltogether.Offer;
import com.example.nitctraveltogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    Dialog mydialog;
    int count = 0;
    private GalleryViewModel galleryViewModel;
    ListView lv;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference databaseuser;
    DatabaseReference databaseuser1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String user;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseDatabase data =  FirebaseDatabase.getInstance();
        databaseuser = FirebaseDatabase.getInstance().getReference("OfferLift");
        mydialog = new Dialog(getActivity());
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        int x;
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        lv  = root.findViewById(R.id.listview);

        pref=getActivity().getSharedPreferences("user",MODE_PRIVATE);
        String senderemail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String key = senderemail.substring(0,senderemail.length()-11);
        databaseuser1 = FirebaseDatabase.getInstance().getReference("request");
        editor=pref.edit();
        user=pref.getString("email_id",null);
        final List<String> list1 = new ArrayList<>();
        final List<Offer> list = new ArrayList<>();

        databaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                for(String key: dataMap.keySet()){
                    Object data = dataMap.get(key);
                    try{
                        HashMap<String,Object> userData = (HashMap<String,Object>) data;
                        Offer offer = new Offer( (String)userData.get("email"), (String) userData.get("destination"),
                                (String) userData.get("availableSeats"), (String) userData.get("vehicleType"),(String) userData.get("time"));
                        list.add(offer);
                    }
                    catch (Exception e){
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                for(int i=0;i<list.size();i++)
                {
                    list1.add(list.get(i).email);
                    Log.i("abc",list1.get(i));
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, list1);
                lv.setAdapter(arrayAdapter);
              
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                TextView txtclose;
                Button btnFollow;
                TextView name, email, aseats, tov,destination;

                mydialog.setContentView(R.layout.custompopup);
                txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
                name =(TextView) mydialog.findViewById(R.id.name);
                email =(TextView) mydialog.findViewById(R.id.email);
                aseats =(TextView) mydialog.findViewById(R.id.availableseats);
                destination =(TextView) mydialog.findViewById(R.id.destination);
                tov =(TextView) mydialog.findViewById(R.id.typeofvehicle);
                email.setText("Email: "+ list.get(i).email);
                aseats.setText("No. of Seats "+ list.get(i).availableSeats);
                String receiveremail=list.get(i).email.substring(0,list.get(i).email.length()-11);
                databaseuser1 = FirebaseDatabase.getInstance().getReference("request").child(receiveremail);
                tov.setText("Type of Vehicle "+ list.get(i).vehicleType);
                name.setText("Name : kapil Chhipa");
                destination.setText("Destination "+ list.get(i).destination);
                txtclose.setText("X");
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
                btnFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String senderemail=FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                // second listener start

                        databaseuser1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                    int key = Integer.parseInt(ds.getKey().toString());
                                    if(key>count)
                                        count=key;
                                }





                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        databaseuser1.child(String.valueOf(count+1)).setValue(senderemail);
                        Toast.makeText(getActivity(),"Request sent",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return root;
    }
}
