package com.nitc.nitctraveltogether.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitc.nitctraveltogether.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link about#newInstance} factory method to
 * create an instance of this fragment.
 */
public class about extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView howtouse;
    Dialog mydialog;
    public about() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment about.
     */
    // TODO: Rename and change types and number of parameters
    public static about newInstance(String param1, String param2) {
        about fragment = new about();
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
        View root= inflater.inflate(R.layout.fragment_about, container, false);
        ImageView kapil,tushar,soumya,akshita,abhilasha,profilepic,linkedin,facebook;
        TextView name,email;
        howtouse = root.findViewById(R.id.use);
        name=root.findViewById(R.id.name);
        email=root.findViewById(R.id.email);
        kapil=root.findViewById(R.id.kapil);
        tushar=root.findViewById(R.id.tushar);
        akshita=root.findViewById(R.id.akshita);
        abhilasha=root.findViewById(R.id.abhilasha);
        soumya=root.findViewById(R.id.soumya);
        profilepic=root.findViewById(R.id.profilepic);
        facebook=root.findViewById(R.id.facebook);
        mydialog = new Dialog(getActivity());

        linkedin=root.findViewById(R.id.linkedin);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tusharg1997/NITCTravelTogether"));
                startActivity(browserIntent);
            }
        });
        howtouse.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mydialog.setContentView(R.layout.howtouseinfo);
                //TextView txtclose = (TextView) mydialog.findViewById(R.id.txtclose);
                TextView tt = mydialog.findViewById(R.id.howtouse);
               // Button ok = mydialog.findViewById(R.id.ok);
                String s="NITC Travel Together is an android application developed using Android Studio, Java, XML, and Firebase."+"\n"+"This app is made exclusively for NITC students so that they can travel together and share the fare as well as each other's company while traveling by offering lift to others or by requesting others for lift."+"\n"+" This app has Google Map API integrated which not only shows the current position of the logged-in user but also the position of other users which are in the radius of 5km, also the location marker shows the name of that user." +"\n"+ "A user can offer a lift by entering his destination, vehicle type and number of available seats while other users who do not want to offer lift but only want to travel with others can select the lift from the list of people who offered the lift and can send the request, the person who offered the lift gets a notification with the name and phone number of the person who has requested a lift from him, he can either accept the request which sends the notification to another person with his phone number or he can reject the request which will send the reject notification to the other person. The notification thing is developed using Rest API and firebase." + "\n"+ "Users can also rate each other by searching their profile using email id and also users can report a person if he/she thinks that the person is intolerable and must be reported.\n\n";
                tt.setText(s);
                /*txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });*/
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mydialog.show();
            }
        });

        kapil.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                name.setText("Name:Kapil Chhipa");
                email.setTextColor(Color.BLACK);
                email.setOnClickListener(null);
                email.setText("Email:kapil_m180265ca@nitc.ac.in");
                profilepic.setImageResource(R.drawable.kapil);
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/kapilchhipa4"));
                           startActivity(browserIntent);
                    }
                });
                linkedin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/kapil-chhipa/"));
                        startActivity(browserIntent);
                    }
                });
                facebook.setVisibility(View.VISIBLE);
                linkedin.setVisibility(View.VISIBLE);
            }
        });
        tushar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("Name:Tushar Gupta");
                email.setText("Email:tushar_m180499ca@nitc.ac.in");
                email.setTextColor(Color.BLACK);
                email.setOnClickListener(null);
                profilepic.setImageResource(R.drawable.tushar);
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tusharg1997"));
                        startActivity(browserIntent);
                    }
                });
                linkedin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/tushar-gupta-b7232a67/"));
                        startActivity(browserIntent);
                    }
                });
                facebook.setVisibility(View.VISIBLE);
                linkedin.setVisibility(View.VISIBLE);
            }
        });
        abhilasha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name.setText("Name:Abhilasha Sharma");
                email.setText("Email:abhilasha_m180275ca@nitc.ac.in");
                email.setTextColor(Color.BLACK);
                email.setOnClickListener(null);
                profilepic.setImageResource(R.drawable.abhilasha);
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/AbhilashaSharma2109"));
                        startActivity(browserIntent);
                    }
                });
                linkedin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/abhilasha-sharma-83141a171/"));
                        startActivity(browserIntent);
                    }
                });
                facebook.setVisibility(View.VISIBLE);
                linkedin.setVisibility(View.VISIBLE);
            }
        });
        akshita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("Name:Akshita Priyadarshi");
                email.setOnClickListener(null);
                email.setText("Email:akshita_m180251ca@nitc.ac.in");
                email.setTextColor(Color.BLACK);
                profilepic.setImageResource(R.drawable.akshita);
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/akshita.priyadarshi.1"));
                        startActivity(browserIntent);
                    }
                });
                linkedin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/akshita-priydarshini-41b7b5185/"));
                        startActivity(browserIntent);
                    }
                });
                facebook.setVisibility(View.VISIBLE);
                linkedin.setVisibility(View.VISIBLE);
            }
        });
        soumya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setOnClickListener(null);
                name.setText("Name:Soumya Parashar");
                email.setText("Email:soumya_m180260ca@nitc.ac.in");
                email.setTextColor(Color.BLACK);
                profilepic.setImageResource(R.drawable.soumya);
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/soumya.parashar.96"));
                        startActivity(browserIntent);
                    }
                });
                linkedin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/soumya-parashar-33b119174/"));
                        startActivity(browserIntent);
                    }
                });
                facebook.setVisibility(View.VISIBLE);
                linkedin.setVisibility(View.VISIBLE);

            }
        });
        mydialog.setContentView(R.layout.howtouseinfo);
        //TextView txtclose = (TextView) mydialog.findViewById(R.id.txtclose);
        TextView tt = mydialog.findViewById(R.id.howtouse);
        // Button ok = mydialog.findViewById(R.id.ok);
        String s="NITC Travel Together is an android application developed using Android Studio, Java, XML, and Firebase."+"\n"+"This app is made exclusively for NITC students so that they can travel together and share the fare as well as each other's company while traveling by offering lift to others or by requesting others for lift."+"\n"+" This app has Google Map API integrated which not only shows the current position of the logged-in user but also the position of other users which are in the radius of 5km, also the location marker shows the name of that user." +"\n"+ "A user can offer a lift by entering his destination, vehicle type and number of available seats while other users who do not want to offer lift but only want to travel with others can select the lift from the list of people who offered the lift and can send the request, the person who offered the lift gets a notification with the name and phone number of the person who has requested a lift from him, he can either accept the request which sends the notification to another person with his phone number or he can reject the request which will send the reject notification to the other person. The notification thing is developed using Rest API and firebase." + "\n"+ "Users can also rate each other by searching their profile using email id and also users can report a person if he/she thinks that the person is intolerable and must be reported.\n\n";
        tt.setText(s);
                /*txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });*/
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
        return root;
    }
}
