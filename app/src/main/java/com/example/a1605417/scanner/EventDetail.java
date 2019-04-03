package com.example.a1605417.scanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventDetail extends AppCompatActivity {
    Intent intent;
    String result,productName;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername,mEmailId;
    int k=0;
    ImageView eventimageView;
    TextView headTextView;
    Button reviwButton;
    ProgressBar progressBar;
    String category;
    TextView clubnameTextView;
    String place;
    String subCategory;
    Button give_review,check_review,back;
    RelativeLayout relativeLayout;
    TextView headerTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        eventimageView=findViewById(R.id.eventimageView);
        headTextView=findViewById(R.id.textView);
        progressBar=findViewById(R.id.progressBar);
        reviwButton=findViewById(R.id.reviewButton);
        clubnameTextView=findViewById(R.id.clubname);
        relativeLayout=findViewById(R.id.relativeLayout);
        headTextView.performClick();
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
        if(intent.hasExtra("result")){
            result=intent.getStringExtra("result"); }
        reviwButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getApplicationContext(),Review.class);
                    intent.putExtra("productId",result);
                    intent.putExtra("productName",productName);
                    intent.putExtra("category",category);
                    intent.putExtra("place",place);
                    intent.putExtra("subCategory",subCategory);
                    startActivity(intent);
                }
            });
        onRetrieve();
    }

    public void onRetrieve() {
        Query query = mDatabaseReference.child("product").orderByChild("productId").equalTo(result);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if(getApplicationContext()==null){
                        return;
                    }
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        clubnameTextView.setText(myData.getProductDescription());
                        productName=myData.getProductName();
                        category=myData.getCategory();
                        subCategory=myData.getSubCategory();
                        place=myData.getPlace();
                        headTextView.setText(myData.getProductName());
                        Glide.with(getApplicationContext()).load(myData.getImage()).into(eventimageView);
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    clubnameTextView.setVisibility(View.GONE);
                    headTextView.setText("No Data Found");
                    eventimageView.setVisibility(View.GONE);
                    reviwButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),ScannedBarcodeActivity.class));
    }
}
