package com.example.a1605417.scanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView productsRecyclerView;
    DatabaseReference mDataRef;
    ProductSearchRecyclerAdapter adapter;
    Intent intent;
    private List<MyData> myDataList;
    float rating;
    int sum=0;
    int count=0;
    long childrencount=0;
    MyData myData;
    int i;
    String s1;
    SearchView searchView;
   ArrayList<String> idList=new ArrayList<String>();
    ArrayList<String> imageList=new ArrayList<String>();
    ArrayList<String> descriptionList=new ArrayList<String>();
    ArrayList<String> nameList=new ArrayList<String>();
    ArrayList<Float> ratingList=new ArrayList<Float>();
    ArrayList<Integer> totalreviewsList=new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_search);
        productsRecyclerView=findViewById(R.id.productSearchRecyclerView);
        searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        intent=getIntent();
        myDataList=new ArrayList<MyData>();
        mDataRef= FirebaseDatabase.getInstance().getReference();
        adapter=new ProductSearchRecyclerAdapter(getApplicationContext(),myDataList);
        RecyclerView.LayoutManager recycler=new LinearLayoutManager(ProductsSearchActivity.this);
        productsRecyclerView.setLayoutManager(recycler);
        productsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productsRecyclerView.setAdapter(adapter);

        if(intent.getStringExtra("category").equals("Services")){
            Toast.makeText(this, ""+intent.getStringExtra("place"), Toast.LENGTH_SHORT).show();
            query0(); }
        else if(intent.getStringExtra("category").equals("Products")){
          query1();
        }
    }

    private void query1() {
        Query query=mDataRef.child("product").orderByChild("subCategory").equalTo(intent.getStringExtra("subCategory"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    idList.clear();
                    nameList.clear();
                    descriptionList.clear();
                    imageList.clear();
                    ratingList.clear();
                    totalreviewsList.clear();
                    childrencount=dataSnapshot.getChildrenCount();
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                         idList.add(myData.getProductId());
                         nameList.add(myData.getProductName());
                         descriptionList.add(myData.getProductDescription());
                         imageList.add(myData.getImage());
                    }
                }
                getRating();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void query0() {
        Query query=mDataRef.child("product").orderByChild("subCategory").equalTo(intent.getStringExtra("subCategory"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    idList.clear();
                    nameList.clear();
                    descriptionList.clear();
                    childrencount= dataSnapshot.getChildrenCount();
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(myData.getPlace().equalsIgnoreCase(intent.getStringExtra("place"))) {
                            idList.add(myData.getProductId());
                            nameList.add(myData.getProductName());
                            descriptionList.add(myData.getProductDescription());
                            imageList.add(myData.getImage());
                        }}
                }
                getRating();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void  getRating() {
            Query query = mDataRef.child("reviews");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count = 0;
                    sum = 0;
                    if (dataSnapshot.exists()) {
                        for(i=0;i<idList.size();i++) {
                            count=0;
                            sum=0;
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                MyData myData = userSnapshot.getValue(MyData.class);
                                assert myData != null;

                                if (myData.getProductId().equals(idList.get(i))&& !myData.getFlag().equals("0")){
                                    if (myData.getRating().equalsIgnoreCase("Bad")) {
                                        sum = sum + 1;
                                        count++;
                                    } else if (myData.getRating().equalsIgnoreCase("Good")) {
                                        sum = sum + 3;
                                        count++;
                                    } else if (myData.getRating().equalsIgnoreCase("Very Good")) {
                                        sum = sum + 4;
                                        count++;
                                    } else if (myData.getRating().equalsIgnoreCase("Excellent")) {
                                        sum = sum + 5;
                                        count++;
                                    } else if (myData.getRating().equalsIgnoreCase("Average")) {
                                        sum = sum + 3;
                                        count++;
                                    }

                            }
                            }
                            if(count!=0)
                            rating = sum / count;
                            else
                              rating=0;
                            ratingList.add(rating);
                            totalreviewsList.add(count);
                        }
                    } else {
                        rating = 0;
                        ratingList.add(rating);
                        totalreviewsList.add(count);
                    }
                      if(ratingList.size()==idList.size()){
                      finalStep();
                     }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }

    private void finalStep() {
        for(int i=0;i<idList.size();i++)
        {
            MyData myData=new MyData(nameList.get(i),descriptionList.get(i),idList.get(i),imageList.get(i),0,ratingList.get(i),totalreviewsList.get(i));
            myDataList.add(myData);

        }
        Toast.makeText(this, ""+myDataList.size(), Toast.LENGTH_SHORT).show();
        adapter=new ProductSearchRecyclerAdapter(getApplicationContext(),myDataList);
        productsRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s1=s;
        onSearchProduct();
        return false;
    }

    private void onSearchProduct() {

        Query query = mDataRef.child("product");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                nameList.clear();
                descriptionList.clear();
                imageList.clear();
                ratingList.clear();
                totalreviewsList.clear();
                myDataList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if (myData.getPlace().equalsIgnoreCase(intent.getStringExtra("place")) && myData.getCategory().equalsIgnoreCase(intent.getStringExtra("category"))) {
                            if (myData.getSubCategory().toLowerCase().startsWith(s1.toLowerCase()) || myData.getProductName().toLowerCase().startsWith(s1.toLowerCase())
                                    || myData.getProductDescription().toLowerCase().startsWith(s1.toLowerCase()) || myData.getPlace().toLowerCase().startsWith(s1.toLowerCase())) {
                                idList.add(myData.getProductId());
                                nameList.add(myData.getProductName());
                                descriptionList.add(myData.getProductDescription());
                                imageList.add(myData.getImage());
                            }
                        }
                    }
                }
                if (s1.isEmpty()) {
                    idList.clear();
                    nameList.clear();
                    descriptionList.clear();
                }
                if (idList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "no result found", Toast.LENGTH_LONG).show();
                }
               getRating();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
