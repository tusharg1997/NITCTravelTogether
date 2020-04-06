package com.example.nitctraveltogether.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitctraveltogether.Adapter;
import com.example.nitctraveltogether.ModelClass;
import com.example.nitctraveltogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentlyActive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentlyActive extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private TextView message;
    ArrayList<ModelClass>  modelClassList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String getName(String email) {
        String s = "";
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '_')
                break;
            s = s + email.charAt(i);
        }


        return s;
    }
    public CurrentlyActive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentlyActive.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentlyActive newInstance(String param1, String param2) {
        CurrentlyActive fragment = new CurrentlyActive();
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
        View root= inflater.inflate(R.layout.fragment_currently_active, container, false);

        recyclerView = root.findViewById(R.id.rv);
        message = root.findViewById(R.id.msg);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        modelClassList = new ArrayList<>();
        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("currentactive").child(currentEmail.substring(0, currentEmail.length() - 11));

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if ((int) dataSnapshot.getChildrenCount() == 0) {
                        Adapter adapter = new Adapter(modelClassList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        message.setVisibility(View.VISIBLE);
                    } else {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            String value=data.getValue().toString();
                            modelClassList.add(new ModelClass("Name: "+getName(value), "Email: "+value));
                        }
                        Adapter adapter = new Adapter(modelClassList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setHasFixedSize(true);
                        adapter.notifyDataSetChanged();
                        message.setVisibility(View.INVISIBLE);
                        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Toast.makeText(getActivity(), "clicked at position "+ String.valueOf(position), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDeleteClick(int position) {
                                try {
                                    ModelClass obj = modelClassList.get(position);
                                    String email = obj.getEmail();
                                    email = email.substring(7, email.length() - 11);

                                    String myemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    myemail = myemail.substring(0, myemail.length() - 11);
                                    FirebaseDatabase.getInstance().getReference("currentactive").child(myemail).child(email).removeValue();
                                    modelClassList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    if (modelClassList.size() == 0)
                                        message.setVisibility(View.VISIBLE);
                                }
                                catch(Exception e)
                                {
                                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
                else
                {
                    Adapter adapter = new Adapter(modelClassList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    message.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return root;
    }
}
