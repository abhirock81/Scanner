package com.example.a1605417.scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import extra.HideIt;

public class Review extends AppCompatActivity {
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername,mEmailId,mUID;
    RadioGroup radioGroup;
    EditText personnameEditText,personEmailEditText,enterreviewEditText;
    TextView givereviewTextView;
    Button submit;
    RadioButton radioButton;
    int k=0;
    int a;
    String reviewtext;
    int wordCount;
    String btntext;
    int mpoints=0;
    String dor;
    String Product_Id,Product_Name;
    Intent intent;
    boolean flag=false;
    RadioButton excellentRadioButton,verygoodRadioButton,goodRadioButton,averageRadioButton,badRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        personnameEditText=findViewById(R.id.personename);
        givereviewTextView=findViewById(R.id.givereviewtext);
        personEmailEditText=findViewById(R.id.personemail);
        submit=findViewById(R.id.submitbutton);
        radioGroup=findViewById(R.id.radiogroup);
        enterreviewEditText=findViewById(R.id.enter_review);
        excellentRadioButton=findViewById(R.id.excellent);
        verygoodRadioButton=findViewById(R.id.verygood);
        goodRadioButton=findViewById(R.id.good);
        averageRadioButton=findViewById(R.id.average);
        badRadioButton=findViewById(R.id.bad);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        dor = df.format(c);
        intent=getIntent();
        if(intent.hasExtra("productId"))
        Product_Id=intent.getStringExtra("productId");
        if(intent.hasExtra("productName"))
        Product_Name=intent.getStringExtra("productName");
        int rid=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(rid);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        mUsername = firebaseUser.getDisplayName();
        mEmailId=firebaseUser.getEmail();
        mUID=firebaseUser.getUid();
        personnameEditText.setText(mUsername);
        personEmailEditText.setText(mEmailId);
        personEmailEditText.setEnabled(false);
        personnameEditText.setEnabled(false);

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
        if(!intent.hasExtra("edit")){
        Query query=mDatabaseReference.child("reviews").orderByChild("personEmail").equalTo(mEmailId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(myData.getProductId().equals(Product_Id)){
                  Intent intent=new Intent(getApplicationContext(),ReviewList.class);
                  intent.putExtra("productId",Product_Id);
                  startActivity(intent);
                        break;}
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}
        else{
            if(badRadioButton.getText().toString().equals(intent.getStringExtra("rating")))
                badRadioButton.setChecked(true);
            else if(averageRadioButton.getText().toString().equals(intent.getStringExtra("rating")))
                averageRadioButton.setChecked(true);
            else if(goodRadioButton.getText().toString().equals(intent.getStringExtra("rating")))
                goodRadioButton.setChecked(true);
            else if(verygoodRadioButton.getText().toString().equals(intent.getStringExtra("rating")))
                verygoodRadioButton.setChecked(true);
            else if(excellentRadioButton.getText().toString().equals(intent.getStringExtra("rating")))
                excellentRadioButton.setChecked(true);
            enterreviewEditText.setText(intent.getStringExtra("enteredreviews"));
            }



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                function();
                }

            private void function() {
                int rid = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(rid);
                btntext=radioButton.getText().toString().trim();
                reviewtext=enterreviewEditText.getText().toString();
                if (TextUtils.isEmpty(radioButton.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Choose one Review", Toast.LENGTH_SHORT).show();
                    return;
                }
                query1();
            }

            private void query1() {
                Query query = mDatabaseReference.child("reviews").orderByChild("personEmail").equalTo(mEmailId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reviewtext=enterreviewEditText.getText().toString();
                        if(dataSnapshot.exists()){
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                                MyData myData=userSnapshot.getValue(MyData.class);
                                assert myData != null;
                                if(myData.getProductId().equals(Product_Id)){
                                    userSnapshot.getRef().child("rating").setValue(btntext);
                                    userSnapshot.getRef().child("enteredReview").setValue(reviewtext);
                                    userSnapshot.getRef().child("flag").setValue("2");
                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                    flag=true;
                                    }
                            }
                            if(!flag){
                                MyData myData = new MyData(mEmailId, mUsername,btntext,reviewtext,dor,Product_Id,Product_Name,0,"0",intent.getStringExtra("category"),intent.getStringExtra("place"),intent.getStringExtra("subCategory"));
                                mDatabaseReference.child("reviews").push().setValue(myData);
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            }

                        }
                        else{
                            MyData myData = new MyData(mEmailId, mUsername,btntext,reviewtext,dor,Product_Id,Product_Name,0,"0",intent.getStringExtra("category"),intent.getStringExtra("place"),intent.getStringExtra("subCategory"));
                            mDatabaseReference.child("reviews").push().setValue(myData);
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

           /* private void onPointUpdated() {
                Query query = mDatabaseReference.child("users").orderByChild("uId").equalTo(mUID);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                MyData myData = userSnapshot.getValue(MyData.class);
                                assert myData != null;
                                String points = myData.getPoints();
                                int d = Integer.parseInt(points);
                                mpoints = mpoints + d;
                                userSnapshot.getRef().child("points").setValue("" + mpoints);
                            }
                        }
                        MyData myData = new MyData(mEmailId, mUsername,btntext,reviewtext,dor,Product_Id,Product_Name,0);
                        mDatabaseReference.child("reviews").push().setValue(myData);
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }*/


        });


    }
    
}