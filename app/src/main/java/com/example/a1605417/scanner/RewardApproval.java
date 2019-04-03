package com.example.a1605417.scanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RewardApproval extends AppCompatActivity {
    Intent intent;
    String result;
    String userId;
    List<String> giftList=new ArrayList<String>();
    ListView listView;
    TextView usernameTextView,useremailTextView,userdateofredeemTextView;
    Button approveButton;
    DatabaseReference mDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername;
    ArrayAdapter adapter;
    String s;
    String rewardpoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_approval);
        listView=findViewById(R.id.dynamic);
        usernameTextView=findViewById(R.id.username);
        useremailTextView=findViewById(R.id.useremailid);
        userdateofredeemTextView=findViewById(R.id.dateofredeem);
        approveButton=findViewById(R.id.approve);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
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
        result=intent.getStringExtra("result");
        Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();
        giftList.clear();
        onApprovingResult();
    approveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            query();
        }
    });
    }

    private void query() {
        Query query=mDatabaseReference.child("redeem").orderByChild("uId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(myData.getFlag().equals("0")){
                            userSnapshot.getRef().child("flag").setValue("1");
                        }
                    }
                    onPointUpdated(rewardpoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void onApprovingResult() {

        userId = result.substring(0, result.indexOf("?"));
         s = result.substring(userId.length()+1, result.length() - 1);
        String[] arr = s.split("/");
        giftList.addAll(Arrays.asList(arr));
        adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item,giftList);
        listView.setAdapter(adapter);
             onQuery();
      //  for (int i = 0; i < giftList.size(); i++)
          //  Toast.makeText(getApplicationContext(), "" + giftList.get(i), Toast.LENGTH_SHORT).show();



    }

    private void onQuery() {
        Query query=mDatabaseReference.child("redeem").orderByChild("uId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(myData.getGifts().equals(result.substring(userId.length()+1))&& !myData.getFlag().equals("1"))
                        {
                            usernameTextView.setText(myData.getPersonName());
                            useremailTextView.setText(myData.getPersonEmail());
                            userdateofredeemTextView.setText(myData.getDateOfJoin());
                            rewardpoints=myData.getRedeemPoints();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onPointUpdated(final String rewardpoints) {
        Query query=mDatabaseReference.child("users").orderByChild("uId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        int remainingpoints=Integer.parseInt(myData.getPoints())-Integer.parseInt(rewardpoints);
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
                startActivity(new Intent(getApplicationContext(),ServiceScanner.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),ServiceScanner.class));
    }
}
