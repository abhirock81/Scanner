package com.example.a1605417.scanner;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewList extends AppCompatActivity {
     int k=0;
    RecyclerView recyclerView;
    private List<MyData> mDataList = new ArrayList<>();
    ReviewAdapter reviewAdapter;
    LinearLayoutManager mLayoutManager;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername,mEmailId;
    Intent intent;
    String Product_Id;
    static  String productid,myreviewstag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        recyclerView=findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
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
        intent=getIntent();
        Product_Id=intent.getStringExtra("productId");
        productid=intent.getStringExtra("productId");
        if(intent.hasExtra("myreviews")){query2();}
        else if(intent.hasExtra("productId"))
        query1();
    }


    private void query2() {

        Query query=mDatabaseReference.child("reviews").orderByChild("personEmail").equalTo(mEmailId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDataList.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(!myData.getFlag().equals("0")){
                        MyData myData1=new MyData(myData.getProductName(),myData.getRating(),myData.getEnteredReview(),1,0,myData.getProductId());
                        mDataList.add(myData1);}
                    }
                }
                else{
                  AlertDialog  alertDialog = new AlertDialog.Builder(ReviewList.this)
                            .setTitle("Message")
                            .setMessage("No Reviews Exist!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                }
                            }).show();
                }
                reviewAdapter = new ReviewAdapter(getApplicationContext(), mDataList);
                recyclerView.setAdapter(reviewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void query1() {
        Query query=mDatabaseReference.child("reviews").orderByChild("productId").equalTo(Product_Id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(!myData.getFlag().equals("0")) {
                            if (myData.getPersonEmail().equals(mEmailId)) {
                                MyData myData1 = new MyData(myData.getProductName(), myData.getRating(), myData.getEnteredReview(), 1, 0, myData.getProductId());
                                mDataList.add(myData1);
                            } else {
                                MyData myData1 = new MyData(myData.getProductName(), myData.getRating(), myData.getEnteredReview(), 0, 0, myData.getProductId());
                                mDataList.add(myData1);
                            }
                        }

                    }
                    reviewAdapter = new ReviewAdapter(getApplicationContext(), mDataList);
                    recyclerView.setAdapter(reviewAdapter);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
