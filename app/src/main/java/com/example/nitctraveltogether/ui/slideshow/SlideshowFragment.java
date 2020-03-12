package com.example.nitctraveltogether.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

public class SlideshowFragment extends Fragment {


    DatabaseReference databaseuser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private SlideshowViewModel slideshowViewModel;
    ListView list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        list=root.findViewById(R.id.requests);
        String currentemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentemail = currentemail.substring(0,currentemail.length()-11);
        databaseuser = FirebaseDatabase.getInstance().getReference("request").child(currentemail);


        //
        final List<String> ls = new ArrayList<>();
        final String finalCurrentemail = currentemail;
        databaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    String user = ds.getValue(String.class);
                    Log.i("user_email",user);
                    count++;
                    ls.add(user);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ls);
                list.setAdapter(arrayAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}
