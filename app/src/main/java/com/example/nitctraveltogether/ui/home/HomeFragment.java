package com.example.nitctraveltogether.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.nitctraveltogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference databaseuser;
    EditText email;
    EditText destination ;
    EditText seats ;
    EditText vehicle;
    private HomeViewModel homeViewModel;
    boolean validate(String remail, String rdest, String rseats, String rvehicle)
    {

        boolean flag=true;
        if(remail.isEmpty() || rdest.isEmpty() || rseats.isEmpty() || rvehicle.isEmpty() )
        {
            Toast.makeText(getActivity(), "Enter all fields", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }
  /*  public void offerLift(View view)
    {

        EditText email = findViewById(R.id.email);
        EditText destination = findViewById(R.id.destination);
        EditText seats = findViewById(R.id.seat);
        EditText vehicle = findViewById(R.id.vehicle);
        final String remail=email.getText().toString();
        final String rdestination=destination.getText().toString();
        final String rseats=seats.getText().toString();
        final String rvehicle=vehicle.getText().toString();
        if(validate(remail, rdestination, rseats, rvehicle)==false)
            return;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Intent i  = new Intent(getActivity() , MainActivity.class);
            getActivity().finish();;
        }
        String id = currentUser.getEmail();
        Toast.makeText(getActivity(), "email is"+id, Toast.LENGTH_SHORT).show();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        Offer offer = new Offer(remail,rdestination,rseats,rvehicle,time,id);
        String id1 = databaseuser.push().getKey();
        databaseuser.child(id1).setValue(offer);
    }*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        FirebaseDatabase data =  FirebaseDatabase.getInstance();
        databaseuser = FirebaseDatabase.getInstance().getReference("OfferLift");
        mAuth = FirebaseAuth.getInstance();

        return root;
    }
}
