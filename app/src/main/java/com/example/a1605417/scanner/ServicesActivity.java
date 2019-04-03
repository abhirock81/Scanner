package com.example.a1605417.scanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity {
        RecyclerView servicesRecyclerView;
        DatabaseReference mDatabaseRef;
        PlacesRecyclerAdapter recyclerAdapter;
        Intent intent;
        List<String> placeList=new ArrayList<String>();
        private List<MyData> myDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        servicesRecyclerView=findViewById(R.id.placeRecyclerView);

        myDataList=new ArrayList<MyData>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference();

        intent=getIntent();
       recyclerAdapter=new PlacesRecyclerAdapter(getApplicationContext(),myDataList);
       RecyclerView.LayoutManager recycler=new GridLayoutManager(ServicesActivity.this,2);
       servicesRecyclerView.setLayoutManager(recycler);
       servicesRecyclerView.setItemAnimator(new DefaultItemAnimator());
       servicesRecyclerView.setAdapter(recyclerAdapter);

        onQuery();

    }

    private void onQuery() {
        Query query=mDatabaseRef.child("product").orderByChild("category").equalTo(intent.getStringExtra("category"));
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if(dataSnapshot.exists()) {
                         myDataList.clear();
                         placeList.clear();
                         for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                             MyData value = dataSnapshot1.getValue(MyData.class);
                             MyData myData;
                             assert value != null;
                             if (!placeList.contains(value.getPlace()))
                             {
                                 myData = new MyData(intent.getStringExtra("category"), value.getSubCategory(), value.getPlace(), value.getProductName());
                                 myDataList.add(myData);
                                 placeList.add(value.getPlace());}
                         }
                     }
                     recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
