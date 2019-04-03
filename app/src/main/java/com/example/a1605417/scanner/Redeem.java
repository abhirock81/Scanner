package com.example.a1605417.scanner;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Redeem extends AppCompatActivity {
    EditText nameEditText, numberEditText, placeEditText;
    Button redeemButton;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient mFusedLocationClient;
    Intent intent;
    String points;
    RadioGroup radioGroup;
    TextView moneyTextView,gifttypeTextView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername,mEmailId,mUid;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        nameEditText = findViewById(R.id.nameedittext);
        numberEditText = findViewById(R.id.numberedittext);
        placeEditText = findViewById(R.id.placeedittext);
        redeemButton = findViewById(R.id.redeembutton);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        mUsername = firebaseUser.getDisplayName();
        nameEditText.setText(mUsername);
        mEmailId=firebaseUser.getEmail();
        mUid=firebaseUser.getUid();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    mUsername = firebaseUser.getDisplayName();
                } else {
                    auth.signOut();
                }
            }
        };
        intent=getIntent();
        if(intent.hasExtra("points")){
            points=intent.getStringExtra("points");}

         //onPoints(points);
        onGetLocation();
        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OtpVerification.class);
                intent.putExtra("name", nameEditText.getText().toString().trim());
                intent.putExtra("number", numberEditText.getText().toString().trim());
                intent.putExtra("place", placeEditText.getText().toString());
                startActivity(intent);
            }
        });
        onQuery();
        }

    private void onGetLocation() {
        checkLocationPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }

    private void onQuery() {
        Query query=mDatabaseReference.child("userdetails").orderByChild("uId").equalTo(mUid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                       startActivity(new Intent(getApplicationContext(),RewardsRedeem.class));
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*private void onPoints(String points) {
        if(points)

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                double Latitude=location.getLatitude();
                                double Longitude=location.getLongitude();
                                getAddress(Latitude,Longitude);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                double Latitude=location.getLatitude();
                                double Longitude=location.getLongitude();
                                getAddress(Latitude,Longitude);
                            }
                        }
                    });
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Redeem.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                        mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            double Latitude=location.getLatitude();
                                            double Longitude=location.getLongitude();
                                            getAddress(Latitude,Longitude);
                                        }
                                    }
                                });

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void getAddress(double lat,double lng) {

        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add=obj.getAddressLine(0);
           /* add = add + "\n" + obj.getThoroughfare();
            add = add + "\n" + obj.getSubLocality();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" +obj.getAdminArea();*/
            String address = add;
           placeEditText.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}