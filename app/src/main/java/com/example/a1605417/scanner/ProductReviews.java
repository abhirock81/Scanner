package com.example.a1605417.scanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductReviews extends AppCompatActivity {
    RecyclerView productreviewRecyclerView;
    ImageView  productImageView;
    TextView   productnameTextView,reviewgradepointsTextView,totalExcellentReviewTextView,totalVerygoodReviewTextView,categoryTextView,descTextView,addressTextView;
    Intent intent;
    String productId;
    int count=0,j=0,count1;
    double reviewgradepoint;
    String image;
   ProductReviewAdapter productReviewAdapter;
    LinearLayoutManager mLayoutManager;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    RatingBar ratingBar;
    String mUsername,mEmailId;
    TextView middleTextView;
    ImageView addImageView;
    String subCategory;
    private List<MyData> mDataList = new ArrayList<>();
    private List<MyData> mDataList1 = new ArrayList<>();
    private List<String> uidDataList = new ArrayList<>();
    private List<String> ratingDataList = new ArrayList<>();
    private List<String> reviewDataList = new ArrayList<>();
    private List<String> productIdList = new ArrayList<>();
    private List<String> personList = new ArrayList<>();
    private List<String> iconList = new ArrayList<>();
    int excellentpoint=0,verygoodpoint=0,goodpoint=0,averagepoint=0,badpoint=0;
    private List<Integer> mratingList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_reviews);
        productImageView=findViewById(R.id.productimage);
        ratingBar=findViewById(R.id.ratingBar);
        middleTextView=findViewById(R.id.middletextView);
        addImageView=findViewById(R.id.addImageView);
        categoryTextView=findViewById(R.id.categorytextView);
        descTextView=findViewById(R.id.descTextView);
        addressTextView=findViewById(R.id.addressTextView);
        productnameTextView=findViewById(R.id.productnametextview);
        productreviewRecyclerView=findViewById(R.id.productreviewsrecyclerview);
        reviewgradepointsTextView=findViewById(R.id.reviewgradepoints);
        totalExcellentReviewTextView=findViewById(R.id.totalexcellentrevie);
        totalVerygoodReviewTextView=findViewById(R.id.totalverygoodreview);
        mLayoutManager = new LinearLayoutManager(this);
        productreviewRecyclerView.setLayoutManager(mLayoutManager);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        mUsername = firebaseUser.getDisplayName();
        mEmailId=firebaseUser.getEmail();
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
        middleTextView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        intent=getIntent();
        if(intent.hasExtra("productId"))
         productId=intent.getStringExtra("productId");
        query1();


          productImageView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                      Dialog builder = new Dialog(ProductReviews.this,android.R.style.Theme_Light);
                      builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                      builder.getWindow().setBackgroundDrawable(
                              new ColorDrawable(android.graphics.Color.TRANSPARENT));
                      builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                          @Override
                          public void onDismiss(DialogInterface dialogInterface) {
                              //nothing;
                          }
                      });

                      ImageView imageView = new ImageView(ProductReviews.this);
                      Glide.with(getApplicationContext()).load(image).into(imageView);
                      builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                              ViewGroup.LayoutParams.MATCH_PARENT,
                              ViewGroup.LayoutParams.MATCH_PARENT));
                      builder.show();
              }
          });
          addImageView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new Intent(getApplicationContext(),Review.class);
                  intent.putExtra("productId",productId);
                  intent.putExtra("productName",productnameTextView.getText().toString());
                  intent.putExtra("category",categoryTextView.getText().toString());
                  intent.putExtra("place",addressTextView.getText().toString());
                  intent.putExtra("subCategory",subCategory);
                  startActivity(intent);
              }
          });
            }

    private void onShowReviews() {

        mDataList1.clear();
        Collections.sort(mDataList,MyData.lcomparator);
        for(count=0;count<mDataList.size()&&count<3;count++)
        {
            mDataList1.add(mDataList.get(count));
        }
        for(int ncount=mDataList.size()-1;ncount>=count&&ncount>=mDataList.size()-3;ncount--)
        {mDataList1.add(mDataList.get(ncount));
         }
        productReviewAdapter = new ProductReviewAdapter(getApplicationContext(), mDataList1);
        productreviewRecyclerView.setAdapter(productReviewAdapter);
        }


    private void query1() {
        Query query=mDatabaseReference.child("product").orderByChild("productId").equalTo(productId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;

                        productnameTextView.setText(myData.getProductName());
                        image=myData.getImage();
                         subCategory=myData.getSubCategory();
                        categoryTextView.setText(myData.getCategory());
                        descTextView.setText(myData.getProductDescription());
                        addressTextView.setText(myData.getPlace());
                        Glide.with(getApplicationContext()).load(myData.getImage()).into(productImageView);
                    }
                }
                query2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void query3() {

                Query query = mDatabaseReference.child("userPhoto");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (count1 = 0; count1 < uidDataList.size(); count1++) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    MyData myData = userSnapshot.getValue(MyData.class);
                                    assert myData != null;
                                    if(uidDataList.get(count1).equalsIgnoreCase(myData.getPersonEmail()))
                                    iconList.add(myData.getPhotoUrl());
                                }
                            }
                        }

                                addingInList();
                        }




                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


      //  Toast.makeText(ProductReviews.this, ""+iconList.size(), Toast.LENGTH_SHORT).show();

    }

    private void addingInList() {
        for( j=0;j<ratingDataList.size();j++){
            if(j<iconList.size()){
            MyData myData=new MyData(personList.get(j),ratingDataList.get(j),reviewDataList.get(j),0,
                    mratingList.get(j),productIdList.get(j),iconList.get(j));
            mDataList.add(myData);}
            else{
                MyData myData=new MyData(personList.get(j),ratingDataList.get(j),reviewDataList.get(j),0,
                        mratingList.get(j),productIdList.get(j),null);
                mDataList.add(myData);
            }
        }
       onShowReviews();
    }
    //to check the the outstanding reviews-positive and negative
    private void query2() {
        Query query=mDatabaseReference.child("reviews").orderByChild("productId").equalTo(productId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDataList.clear();
                uidDataList.clear();
                ratingDataList.clear();
                reviewDataList.clear();
                iconList.clear();
                personList.clear();
                if(dataSnapshot.exists()) {
                    excellentpoint=0;verygoodpoint=0;goodpoint=0;averagepoint=0;badpoint=0;
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(!myData.getFlag().equals("0")){
                            uidDataList.add(myData.getPersonEmail());
                            ratingDataList.add(myData.getRating());
                            reviewDataList.add(myData.getEnteredReview());
                            personList.add(myData.getPersonName());
                            productIdList.add(myData.getProductId());

                        if (myData.getRating().equals("Excellent")) {
                            mratingList.add(5);
                            excellentpoint++;
                        } else if (myData.getRating().equals("Very Good")) {
                            mratingList.add(4);
                            verygoodpoint++;
                        } else if (myData.getRating().equals("Good")) {
                            mratingList.add(3);
                            goodpoint++;
                        } else if (myData.getRating().equals("Average")) {
                            mratingList.add(2);
                            averagepoint++;
                        } else {
                            mratingList.add(1);
                            badpoint++;
                        }
                    }
                    }
                    }
                    else{
                         middleTextView.setVisibility(View.VISIBLE);
                         addImageView.setVisibility(View.VISIBLE);
                     }
                    if((excellentpoint+verygoodpoint+goodpoint+averagepoint+badpoint)!=0) {
                        reviewgradepoint=(5*excellentpoint+4*verygoodpoint+3*goodpoint+2*averagepoint+badpoint)/(excellentpoint+verygoodpoint+goodpoint+averagepoint+badpoint);
                    }
                    else{
                    reviewgradepoint=0;
                    }
                ratingBar.setRating(Float.parseFloat(""+reviewgradepoint));
                reviewgradepointsTextView.setText(""+ ((int) reviewgradepoint)+"/5");
                
                totalExcellentReviewTextView.setText(""+excellentpoint+" Excellent Reviews");
                totalVerygoodReviewTextView.setText(""+verygoodpoint+" VeryGood Reviews");
                
                query3();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),CategoryReviews.class));
    }
}
