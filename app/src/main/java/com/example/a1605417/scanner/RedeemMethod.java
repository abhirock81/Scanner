package com.example.a1605417.scanner;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class RedeemMethod extends AppCompatActivity {
    QRCodeWriter writer;
    ImageView imageView;
    Bitmap bitmap;
    Intent intent;
    String number;
    TextView t1,t2,t3,t4,t5,t6,t7;
    EditText nameEditText,numberEditText,emailEditText,addressEditText,stateEditText,pincodeEditText,cityEditText;
    Button submitbutton;
    DatabaseReference mDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername,mEmailId,mUid,mNumber;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient mFusedLocationClient;
    String reardpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_redeem_method);
       writer=new QRCodeWriter();
       imageView=findViewById(R.id.qrImage);
       nameEditText=findViewById(R.id.nameeditText);
       addressEditText=findViewById(R.id.addresseditText);
       numberEditText=findViewById(R.id.phoneeditText);
       emailEditText=findViewById(R.id.emaileditText);
       stateEditText=findViewById(R.id.stateeditText);
       pincodeEditText=findViewById(R.id.pincodeeditText);
       cityEditText=findViewById(R.id.cityeditText);
       submitbutton=findViewById(R.id.submitbutton);
       t1=findViewById(R.id.textView4);
       t2=findViewById(R.id.textView5);
       t3=findViewById(R.id.textView6);
       t4=findViewById(R.id.textView7);
       t5=findViewById(R.id.textView8);
       t6=findViewById(R.id.textView9);
       t7=findViewById(R.id.textView10);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        mUsername = firebaseUser.getDisplayName();
        mEmailId=firebaseUser.getEmail();
        mUid=firebaseUser.getUid();
        mNumber=firebaseUser.getPhoneNumber();
        intent=getIntent();
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
        if(intent.hasExtra("redeemMethod")){
        if(intent.getStringExtra("redeemMethod").equalsIgnoreCase("OnSpotgift")){
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            t3.setVisibility(View.GONE);
            t4.setVisibility(View.GONE);
            t5.setVisibility(View.GONE);
            t6.setVisibility(View.GONE);
            t7.setVisibility(View.GONE);
            nameEditText.setVisibility(View.GONE);
            addressEditText.setVisibility(View.GONE);
            numberEditText.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            stateEditText.setVisibility(View.GONE);
            pincodeEditText.setVisibility(View.GONE);
            cityEditText.setVisibility(View.GONE);
            submitbutton.setVisibility(View.GONE);
            BitMatrix bitMatrix = null;
            try {
                bitMatrix = writer.encode(mUid+"?"+intent.getStringExtra("gifts"), BarcodeFormat.QR_CODE, 200, 200);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            assert bitMatrix != null;
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
                }
            }

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            imageView.setImageBitmap(bitmap);
        }
        else{
            imageView.setVisibility(View.GONE);
            nameEditText.setVisibility(View.VISIBLE);
            addressEditText.setVisibility(View.VISIBLE);
            numberEditText.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            stateEditText.setVisibility(View.VISIBLE);
            pincodeEditText.setVisibility(View.VISIBLE);
            cityEditText.setVisibility(View.VISIBLE);
            submitbutton.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            t3.setVisibility(View.VISIBLE);
            t4.setVisibility(View.VISIBLE);
            t5.setVisibility(View.VISIBLE);
            t6.setVisibility(View.VISIBLE);
            t7.setVisibility(View.VISIBLE);
        }
        }
        checkLocationPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query=mDatabaseReference.child("redeem").orderByChild("uId").equalTo(mUid);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                MyData myData = userSnapshot.getValue(MyData.class);
                                assert myData != null;

                                if (myData.getGifts().equals(intent.getStringExtra("gifts"))&&myData.getFlag().equals("0")) {
                                    reardpoints=myData.getRedeemPoints();
                                    userSnapshot.getRef().child("pincode").setValue(pincodeEditText.getText().toString().trim());
                                    userSnapshot.getRef().child("state").setValue(stateEditText.getText().toString().trim());
                                    userSnapshot.getRef().child("city").setValue(cityEditText.getText().toString().trim());
                                    userSnapshot.getRef().child("place").setValue(addressEditText.getText().toString().trim());
                                    userSnapshot.getRef().child("personEmail").setValue(emailEditText.getText().toString().trim());
                                    userSnapshot.getRef().child("number").setValue(numberEditText.getText().toString().trim());
                                    userSnapshot.getRef().child("personName").setValue(nameEditText.getText().toString().trim());
                                    userSnapshot.getRef().child("redeemMethod").setValue(intent.getStringExtra("redeemMethod"));
                                    userSnapshot.getRef().child("flag").setValue("1");
                                }
                            }
                            Toast.makeText(RedeemMethod.this, "Your request is recorded", Toast.LENGTH_SHORT).show();
                            onPointUpdated();
                        }
                        else{
                            Toast.makeText(RedeemMethod.this, "Your request for redeeming is denied", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    onRetrieveNumber();
    }

    private void onPointUpdated() {
        Query query=mDatabaseReference.child("users").orderByChild("uId").equalTo(mUid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        int remainingpoints=Integer.parseInt(myData.getPoints())-Integer.parseInt(reardpoints);
                        userSnapshot.getRef().child("points").setValue(""+remainingpoints);
                        int d;
                        d=Integer.parseInt(myData.getPoints());
                        if(d>=50&&d<100){userSnapshot.getRef().child("level").setValue("1");}
                        else if(d>=100&&d<250){userSnapshot.getRef().child("level").setValue("2");}
                        else if(d>=250&&d<500){userSnapshot.getRef().child("level").setValue("3");}
                        else if(d>=500&&d<750){userSnapshot.getRef().child("level").setValue("4");}
                        else if(d>=750&&d<1000){userSnapshot.getRef().child("level").setValue("5");}
                        else if(d>=1000&&d<2000){userSnapshot.getRef().child("level").setValue("6");}
                        else if(d>=2000&&d<3000){userSnapshot.getRef().child("level").setValue("7");}
                        else if(d>=3000&&d<5000){userSnapshot.getRef().child("level").setValue("8");}
                        else if(d>=5000&&d<10000){userSnapshot.getRef().child("level").setValue("9");}
                        else if(d>10000){userSnapshot.getRef().child("level").setValue("10");}

                    }
                }
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   /* public void onClick(View view) throws WriterException {
        switch (view.getId()){

            case R.id.onspotgiftradioButton:
                imageView.setVisibility(View.VISIBLE);
                nameEditText.setVisibility(View.GONE);
                addressEditText.setVisibility(View.GONE);
                numberEditText.setVisibility(View.GONE);
                emailEditText.setVisibility(View.GONE);
                stateEditText.setVisibility(View.GONE);
                pincodeEditText.setVisibility(View.GONE);
                cityEditText.setVisibility(View.GONE);
                submitbutton.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
                t3.setVisibility(View.GONE);
                t4.setVisibility(View.GONE);
                t5.setVisibility(View.GONE);
                t6.setVisibility(View.GONE);
                t7.setVisibility(View.GONE);
                BitMatrix bitMatrix = writer.encode(mUid+"?"+intent.getStringExtra("gifts"), BarcodeFormat.QR_CODE, 200, 200);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                int[] pixels = new int[width * height];
                for (int y = 0; y < height; y++) {
                    int offset = y * width;
                    for (int x = 0; x < width; x++) {
                        pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
                    }
                }

                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                imageView.setImageBitmap(bitmap);
                break;
            case R.id.bypostradioButton :
                imageView.setVisibility(View.GONE);
                nameEditText.setVisibility(View.VISIBLE);
                addressEditText.setVisibility(View.VISIBLE);
                numberEditText.setVisibility(View.VISIBLE);
                emailEditText.setVisibility(View.VISIBLE);
                stateEditText.setVisibility(View.VISIBLE);
                pincodeEditText.setVisibility(View.VISIBLE);
                cityEditText.setVisibility(View.VISIBLE);
                submitbutton.setVisibility(View.VISIBLE);
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t4.setVisibility(View.VISIBLE);
                t5.setVisibility(View.VISIBLE);
                t6.setVisibility(View.VISIBLE);
                t7.setVisibility(View.VISIBLE);
                emailEditText.setText(mEmailId);
                nameEditText.setText(mUsername);
                break;

        }


    }*/

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
                                ActivityCompat.requestPermissions(RedeemMethod.this,
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
            pincodeEditText.setText(obj.getPostalCode());
            cityEditText.setText(obj.getLocality());
            stateEditText.setText(obj.getAdminArea());
            addressEditText.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
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

    private void onRetrieveNumber() {
        Query query=mDatabaseReference.child("userdetails").orderByChild("uId").equalTo(mUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        number=myData.getNumber();
                        numberEditText.setText(myData.getNumber());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
