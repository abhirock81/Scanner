package com.example.a1605417.scanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SubCatSearch extends AppCompatActivity implements SearchView.OnQueryTextListener {
Intent intent;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername, mEmailId;
    SubCatSearchAdapter recyclerAdapter;
    RecyclerView recyclerView;
    private List<String> subcatList=new ArrayList<String>();
    private List<MyData> myDataList=new ArrayList<MyData>();
    SearchView searchView;
    ListView searchListView;
    private List<String> everythinglist = new ArrayList<>();
    private List<String> productIdList = new ArrayList<>();
    String placechange;
    ArrayAdapter<String> adapter;
    String s1;
    private List<String> getSubcatList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat_search);
        intent=getIntent();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        recyclerView=findViewById(R.id.subCatRecyclerView);
        searchView=findViewById(R.id.searchView2);
        searchListView=findViewById(R.id.searchlistview);
        searchView.setOnQueryTextListener(this);
        recyclerAdapter=new SubCatSearchAdapter(getApplicationContext(),myDataList);
        RecyclerView.LayoutManager recycler=new GridLayoutManager(SubCatSearch.this,4);
        recyclerView.setLayoutManager(recycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
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
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),ProductReviews.class);
                String result=(String) searchListView.getItemAtPosition(i);
                int pos=everythinglist.indexOf(result);
                intent.putExtra("productId",productIdList.get(pos));
                startActivity(intent);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                everythinglist.clear();
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, everythinglist);
                searchListView.setAdapter(adapter);
                return false;
            }
        });
        if(intent.hasExtra("category"))
            query0();
    }

    private void query0() {

        Query query=mDatabaseReference.child("product").orderByChild("category").equalTo(intent.getStringExtra("category"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    myDataList.clear();
                    subcatList.clear();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(myData.getPlace().equalsIgnoreCase(intent.getStringExtra("place"))){
                            if(!subcatList.contains(myData.getSubCategory())){
                            MyData myData1=new MyData(myData.getCategory(),myData.getSubCategory(),myData.getPlace(),myData.getProductName());
                            myDataList.add(myData1);
                            subcatList.add(myData.getSubCategory());}
                        }
                    }

                }
                recyclerAdapter=new SubCatSearchAdapter(getApplicationContext(),myDataList);
                recyclerView.setAdapter(recyclerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void query1() {

        Query query=mDatabaseReference.child("product").orderByChild("category").equalTo(intent.getStringExtra("category"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    myDataList.clear();
                    subcatList.clear();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if (placechange.equalsIgnoreCase(myData.getPlace())) {
                            if (!subcatList.contains(myData.getSubCategory())) {
                                MyData myData1 = new MyData(myData.getCategory(), myData.getSubCategory(), myData.getPlace(), myData.getProductName());
                                myDataList.add(myData1);
                                subcatList.add(myData.getSubCategory());
                            }
                        }
                    }
                }
                recyclerAdapter=new SubCatSearchAdapter(getApplicationContext(),myDataList);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void onQuery() {
        Query query = mDatabaseReference.child("product");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myDataList.clear();
                getSubcatList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if (myData.getPlace().equalsIgnoreCase(intent.getStringExtra("place")) && myData.getCategory().equalsIgnoreCase(intent.getStringExtra("category"))) {
                            if (myData.getSubCategory().toLowerCase().startsWith(s1.toLowerCase()) || myData.getProductName().toLowerCase().startsWith(s1.toLowerCase())
                                    || myData.getProductDescription().toLowerCase().startsWith(s1.toLowerCase()) || myData.getPlace().toLowerCase().startsWith(s1.toLowerCase())) {
                                if(!getSubcatList.contains(myData.getSubCategory())) {
                                    MyData myData1 = new MyData(myData.getCategory(), myData.getSubCategory(), myData.getPlace(), myData.getProductName());
                                    myDataList.add(myData1);
                                    getSubcatList.add(myData.getSubCategory());
                                    subcatList.add(myData.getSubCategory());
                                }
                            }
                        }
                    }
                }
                if (s1.isEmpty()) {
                    myDataList.clear();
                }
                if (myDataList.isEmpty()) {
                    Toast.makeText(SubCatSearch.this, "no result found", Toast.LENGTH_LONG).show();
                }
                recyclerAdapter = new SubCatSearchAdapter(getApplicationContext(), myDataList);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
        @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s1=s;
        onQuery();
        return false;
    }

}
