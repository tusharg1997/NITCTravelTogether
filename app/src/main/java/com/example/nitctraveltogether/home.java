package com.example.nitctraveltogether;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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


public class home extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Location lastLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    LocationRequest locationRequest;
    DatabaseReference databaseuser;
    GeoLocation geoLocation;

    private static final int MY_PERMISSION_REQUEST_READ_FINE_LOCATION = 100;

    public void travel(View view)
    {
        Toast.makeText(this,"Going to drawer",Toast.LENGTH_SHORT).show();
       Intent i=new Intent(home.this, Drawer.class);

       startActivity(i);
       finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#ff000000"));
        actionBar.setBackgroundDrawable(colorDrawable);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Toast.makeText(this,"email="+email,Toast.LENGTH_SHORT);
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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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
                Toast.makeText(home.this,"Location changing",Toast.LENGTH_SHORT).show();
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

            if (ActivityCompat.checkSelfPermission(home.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lastLocation=lastKnownLocation;
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.cur)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
               // Toast.makeText(this,"email="+email,Toast.LENGTH_LONG).show();
                String emailid=email.substring(0,email.length()-11);
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("userlocation");

                GeoFire geoFire= new GeoFire(ref);
                geoFire.setLocation(emailid, new GeoLocation(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
                geoLocation=new GeoLocation(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());

                    getusersaround();
            }


        }


    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                for(Marker markerIt : markers){
                    if(markerIt.getTag().equals(key))
                        return;
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

                for(Marker markerIt : markers){
                    if(markerIt.getTag().equals(key)){
                        markerIt.remove();
                        markers.remove(markerIt);
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

                for(Marker markerIt : markers){
                    if(markerIt.getTag().equals(key)){
                        markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                    }
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
