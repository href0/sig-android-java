package com.project.fishingspots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity {

    //initialize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    DatabaseReference databaseReference;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        actionBar = getSupportActionBar();
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setTitle("Maps");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("toko");

        // assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        // initialize fuse location
        client = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }

    private void getCurrentLocation() {
        // initalize task locatio

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           Task<Location> task = client.getLastLocation();
           task.addOnSuccessListener(new OnSuccessListener<Location>() {
               @Override
               public void onSuccess(@NonNull Location location) {
                    //when success
                   Bundle bundle = getIntent().getExtras();
                   if(location != null){
                       // Sync map
                       supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                           @Override
                           public void onMapReady(@NonNull GoogleMap googleMap) {
                               // Initialize lat lng
                               LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                               // create marker option
                               MarkerOptions options = new MarkerOptions().position(latLng)
                                       .title(("I'm here"));

                               // zoom map
                               googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                               // add marker on map
                               googleMap.addMarker(options);
                           }
                       });
                   }

                   if(bundle != null){
                       supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                           @Override
                           public void onMapReady(@NonNull GoogleMap googleMap) {
                               // Initialize lat lng
                               LatLng latLng = new LatLng(bundle.getDouble("latitude"),bundle.getDouble("longitude"));
                               // create marker option
                               MarkerOptions options = new MarkerOptions().position(latLng)
                                       .title((bundle.getString("namaToko")));

                               // zoom map
                               googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                               // add marker on map
                               googleMap.addMarker(options);
                           }
                       });
                   }else{
                       databaseReference.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                   Toko tokos = dataSnapshot.getValue(Toko.class);

                                   supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                       @Override
                                       public void onMapReady(@NonNull GoogleMap googleMap) {
                                           LatLng latLng = new LatLng(tokos.getLatitude(), tokos.getLongitude());
                                           MarkerOptions options = new MarkerOptions().position(latLng)
                                                   .title((tokos.getNamaToko()));

                                           // zoom map
                                           googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                                           // add marker on map
                                           googleMap.addMarker(options);


                                       }
                                   });

                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
                   }



               }
           });
        }else {
            // wheren permission denied
            // req permission
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // where permission granted
                // call method
                getCurrentLocation();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}