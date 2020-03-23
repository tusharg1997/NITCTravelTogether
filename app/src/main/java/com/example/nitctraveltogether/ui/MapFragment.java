package com.example.nitctraveltogether.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nitctraveltogether.Drawer;
import com.example.nitctraveltogether.Splashscreen;
import com.example.nitctraveltogether.home;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.nitctraveltogether.MainActivity;
import com.example.nitctraveltogether.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private GoogleMap mMap;
    public Criteria criteria;
    public String bestProvider;
    Location lastLocation;
    public double latitude;
    public double longitude;
    LocationManager locationManager;
    LocationListener locationListener;
    LocationRequest locationRequest;
    DatabaseReference databaseuser;
    GeoLocation geoLocation;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        // Inflate the layout for this fragment\
        Intent i = new Intent(getActivity(), home.class);

        View view = inflater.inflate(R.layout.fragment_map, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragmentmap);
        mapFragment.getMapAsync(this);

        return view;
    }
    public static boolean isLocationEnabled(Context context)
    {
        int locationMode = 0;
        String locationProviders;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            try
            {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        else
        {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
////                Toast.makeText(getApplicationContext() ," "+location.getLongitude()+" "+location.getLongitude() , Toast.LENGTH_LONG).show();
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.cur)));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
//
//                String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("userlocation");
//
//                GeoFire geoFire= new GeoFire(ref);
//                geoFire.setLocation(id, new GeoLocation(location.getLatitude(),location.getLongitude()));
//                if(!getuseraroundstarted)
//                    getusersaround();
                //Toast.makeText(getActivity(),"Location changing",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {


               // if (isLocationEnabled(getActivity())) {
                  //  locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                //    locationManager.requestLocationUpdates(bestProvider, 1000, 0, locationListener);
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        lastLocation = lastKnownLocation;
                        if(lastKnownLocation!=null){
                        LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        mMap.clear();

                        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.cur)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        // Toast.makeText(this,"email="+email,Toast.LENGTH_LONG).show();
                        String emailid = email.substring(0, email.length() - 11);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userlocation");

                        GeoFire geoFire = new GeoFire(ref);
                        geoFire.setLocation(emailid, new GeoLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                        geoLocation = new GeoLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                        getusersaround();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Location requesting",Toast.LENGTH_SHORT).show();

                            locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);

                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                    }

              //  }
            }

        }


    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    }

                }

            }

        }

    }
    boolean getuseraroundstarted=false;
    List<Marker> markers = new ArrayList<Marker>();
    private void getusersaround(){
        getuseraroundstarted=true;
        // Toast.makeText(firstpage.this,"Entered in function",Toast.LENGTH_LONG).show();
        DatabaseReference userslocation =FirebaseDatabase.getInstance().getReference().child("userlocation");
        GeoFire geofire= new GeoFire(userslocation);
        GeoQuery geoQuery= geofire.queryAtLocation(new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()),10000);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(location.equals(geoLocation))
                    return;
                try {
                    for(Marker markerIt : markers){
                        if(markerIt.getTag().equals(key))
                            return;
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"Not able to refresh map, you can reload map",Toast.LENGTH_SHORT).show();
                }

                // Toast.makeText(firstpage.this,"before location",Toast.LENGTH_LONG).show();
                // Toast.makeText(firstpage.this,String.valueOf(location.latitude)+" "+String.valueOf(location.longitude),Toast.LENGTH_LONG).show();
                LatLng userLocation = new LatLng(location.latitude, location.longitude);

                Marker muserMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title(key).icon(BitmapDescriptorFactory.fromResource(R.mipmap.rsz_icon)));
                muserMarker.setTag(key);

                markers.add(muserMarker);
            }

            @Override
            public void onKeyExited(String key) {
                try {
                    for (Marker markerIt : markers) {
                        if (markerIt.getTag().equals(key)) {
                            markerIt.remove();
                            markers.remove(markerIt);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"Not able to refresh map, you can reload map",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                try {
                    for (Marker markerIt : markers) {
                        if (markerIt.getTag().equals(key)) {
                            markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"Not able to refresh map, you can reload map",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(getActivity(), "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
//    @Override
//    public void onStop()
//    {
//        super.onStop();
//        String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("userlocation");
//
//        GeoFire geoFire= new GeoFire(ref);
//        geoFire.removeLocation(id);
//    }

}
