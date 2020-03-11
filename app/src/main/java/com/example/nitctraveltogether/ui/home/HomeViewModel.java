package com.example.nitctraveltogether.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference databaseuser;
    /*boolean validate(String remail, String rdest, String rseats, String rvehicle)
    {

        boolean flag=true;
        if(remail.isEmpty() || rdest.isEmpty() || rseats.isEmpty() || rvehicle.isEmpty() )
        {
            Toast.makeText(getActivity(), "Enter all fields", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }*/
    /*public void offerLift(View view)
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
        Toast.makeText(HomeViewModel.this, "email is"+id, Toast.LENGTH_SHORT).show();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        Offer offer = new Offer(remail,rdestination,rseats,rvehicle,time,id);
        String id1 = databaseuser.push().getKey();
        databaseuser.child(id1).setValue(offer);
    }*/
    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is home fragment");

        // ya call hota hai
        FirebaseDatabase data =  FirebaseDatabase.getInstance();
        databaseuser = FirebaseDatabase.getInstance().getReference("OfferLift");
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }
}