package com.example.a1605417.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CategoryReviews extends AppCompatActivity implements SearchView.OnQueryTextListener{
    ArrayAdapter<String> adapter;
    String[] items;
    TextView textView15;
    String s1;
    private List<String> everythinglist = new ArrayList<>();
    private List<String> productIdList = new ArrayList<>();
    private List<String> datalist=new ArrayList<String>();
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername, mEmailId;
    Intent intent;
    Button sButton,pButton;
    String Product_Id;
    RecyclerView productRecyclerView,servicesRecyclerView;
    SearchView searchView;
    int k=0;
    ListView  listView;
    ArrayList<String> listviewStringArrayList=new ArrayList<String>();
    boolean flag=false;
    @SuppressLint("UseSparseArrays")
    HashMap<String,Integer> hmap=new HashMap<String, Integer>();
    HashMap<String,Integer> hmap1=new HashMap<String, Integer>();
    ArrayList<MyData> myDataList=new ArrayList<MyData>();
    ArrayList<MyData> myDataList1=new ArrayList<MyData>();
    static  String location;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient mFusedLocationClient;
    PlacesRecyclerAdapter adapter1,adapter2;
    SpannableStringBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_reviews);
       // sButton=findViewById(R.id.servicesButton);
       // pButton=findViewById(R.id.productButton);
        textView15=findViewById(R.id.textView15);
        listView=findViewById(R.id.listView);
        productRecyclerView=findViewById(R.id.productsrecyclerView);
        servicesRecyclerView=findViewById(R.id.servicerecyclerview);
        searchView = findViewById(R.id.searchview);
        myDataList=new ArrayList<MyData>();
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,listviewStringArrayList);
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);
        adapter1=new PlacesRecyclerAdapter(getApplicationContext(),myDataList);
        adapter2=new PlacesRecyclerAdapter(getApplicationContext(),myDataList1);
        RecyclerView.LayoutManager recycler=new GridLayoutManager(CategoryReviews.this,3);
        RecyclerView.LayoutManager recycler1=new GridLayoutManager(CategoryReviews.this,3);
        servicesRecyclerView.setLayoutManager(recycler);
        servicesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        servicesRecyclerView.setAdapter(adapter1);
        productRecyclerView.setLayoutManager(recycler1);
        productRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productRecyclerView.setAdapter(adapter2);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        mUsername = firebaseUser.getDisplayName();
        mEmailId = firebaseUser.getEmail();
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

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                location=(String) listView.getItemAtPosition(i);
                listviewStringArrayList.clear();
                adapter.notifyDataSetChanged();
                String curr_loc="Current Location : ";
                 builder = new SpannableStringBuilder();
                SpannableString cur_locSpannableString= new SpannableString(curr_loc);
                cur_locSpannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, curr_loc.length(), 0);
                builder.append(cur_locSpannableString);
                SpannableString locSpannableString= new SpannableString(location);
                locSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blueish)), 0, location.length(), 0);
                builder.append(locSpannableString);
                textView15.setText(builder, TextView.BufferType.SPANNABLE);
                onquery();
                searchView.onActionViewCollapsed();
            }
        });

        searchView.setOnQueryTextListener(this);
        onGetLocation();
    }

    private void onquery1() {
        Query query=mDatabaseReference.child("reviews").orderByChild("category").equalTo("Products");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    myDataList1.clear();
                    hmap1.clear();
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(myData.getPlace().equalsIgnoreCase(location)&& !myData.getFlag().equals("0"))
                        {
                            Integer c = hmap1.get(myData.getSubCategory());
                            if (hmap1.get(myData.getSubCategory()) == null) {
                                hmap1.put(myData.getSubCategory(), 1);
                            } else {
                                hmap1.put(myData.getSubCategory(), ++c);
                            }
                        }
                    }
                    Set keys=hmap1.keySet();
                    for (Iterator i = keys.iterator(); i.hasNext(); ) {
                        String key = (String) i.next();
                        Integer value = hmap1.get(key);
                        MyData myData=new MyData(key,value,"Products");
                        myDataList1.add(myData);
                        if(myDataList1.size()>8)
                            break;
                    }
                    Collections.sort(myDataList1,MyData.jcomparator);
                    myDataList1.add(new MyData("More >>",0,"Products"));
                }
                adapter2=new PlacesRecyclerAdapter(getApplicationContext(),myDataList1);
                productRecyclerView.setAdapter(adapter2);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onquery() {
        Query query=mDatabaseReference.child("reviews").orderByChild("category").equalTo("Services");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    myDataList.clear();
                    hmap.clear();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if (myData.getPlace().equalsIgnoreCase(location)&& !myData.getFlag().equals("0")) {
                            Integer c = hmap.get(myData.getSubCategory());
                            if (hmap.get(myData.getSubCategory()) == null) {
                                hmap.put(myData.getSubCategory(), 1);
                            } else {
                                hmap.put(myData.getSubCategory(), ++c);
                            }
                        }
                    }
                    Set keys = hmap.keySet();
                    for (Iterator i = keys.iterator(); i.hasNext(); ) {
                        String key = (String) i.next();
                        Integer value = hmap.get(key);
                        MyData myData = new MyData(key, value, "Services");
                        myDataList.add(myData);
                        if (myDataList.size() > 8)
                            break;
                    }
                    Collections.sort(myDataList, MyData.jcomparator);
                    myDataList.add(new MyData("More >>", 0, "Services"));
                }
                adapter1=new PlacesRecyclerAdapter(getApplicationContext(),myDataList);
                servicesRecyclerView.setAdapter(adapter1);
                onquery1();
                   }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.setIconified(true);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s1=s;
        if(s.equalsIgnoreCase("")){
            listView.setVisibility(View.INVISIBLE);}
            else{
            listView.setVisibility(View.VISIBLE);
            listviewStringArrayList.clear();
            adapter.notifyDataSetChanged();
             onPopulateList();}
        return false;
    }

    private void onPopulateList() {
        Query query=mDatabaseReference.child("reviews");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    listviewStringArrayList.clear();
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(myData.getPlace().toLowerCase().startsWith(s1.toLowerCase())){
                            if(!listviewStringArrayList.contains(myData.getPlace()))
                            listviewStringArrayList.add(myData.getPlace());
                        }

                    }
                    if(listviewStringArrayList.isEmpty())
                        Toast.makeText(CategoryReviews.this, "invalid place", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CategoryReviews.this, "no reviews yet", Toast.LENGTH_SHORT).show();
                }
                 adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,listviewStringArrayList);
                 listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void onGetLocation() {
        checkLocationPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
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
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                if(CategoryReviews.location==null){
                                double Latitude=location.getLatitude();
                                double Longitude=location.getLongitude();
                                getAddress(Latitude,Longitude);}
                            }
                        }
                    });
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(CategoryReviews.this,
                                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
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
             location=obj.getLocality();
            String curr_loc="Current Location : ";
            builder = new SpannableStringBuilder();
            SpannableString cur_locSpannableString= new SpannableString(curr_loc);
            cur_locSpannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, curr_loc.length(), 0);
            builder.append(cur_locSpannableString);
            SpannableString locSpannableString= new SpannableString(location);
            locSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blueish)), 0, location.length(), 0);
            builder.append(locSpannableString);
            textView15.setText(builder, TextView.BufferType.SPANNABLE);
            onquery();
            //Toast.makeText(this, ""+location, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}