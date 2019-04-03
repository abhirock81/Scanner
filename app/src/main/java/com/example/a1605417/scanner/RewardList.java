package com.example.a1605417.scanner;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class RewardList extends AppCompatActivity {
        RecyclerView recyclerView;
    private List<RewardData> mDataList = new ArrayList<>();
    RewardAdapter rewardAdapter;
    LinearLayoutManager mLayoutManager;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername,mEmailId,muId;
    String level,point;
    int p=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_list);
        recyclerView=findViewById(R.id.rewardrecyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        mUsername = firebaseUser.getDisplayName();
        mEmailId=firebaseUser.getEmail();
        muId=firebaseUser.getUid();

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
        onUserQuery();
        Query query=mDatabaseReference.child("rewardlevels");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mDataList.clear();
                    RewardData rewardData3=new RewardData("Crown","Level","Points","Reward","Position");
                    mDataList.add(rewardData3);
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        RewardData rewardData=userSnapshot.getValue(RewardData.class);
                        assert rewardData != null;
                            if(rewardData.getLevel().equalsIgnoreCase(level)){
                                RewardData rewardData1 = new RewardData(rewardData.getCrown(), rewardData.getLevel(), rewardData.getPoints(), rewardData.getReward(),"1");
                                mDataList.add(rewardData1);
                            }
                            else {
                            RewardData rewardData1 = new RewardData(rewardData.getCrown(), rewardData.getLevel(), rewardData.getPoints(), rewardData.getReward(),"0");
                            mDataList.add(rewardData1);}
                        }
                }
                rewardAdapter=new RewardAdapter(getApplicationContext(),mDataList);
                recyclerView.setAdapter(rewardAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onUserQuery() {
        Query query=mDatabaseReference.child("users").orderByChild("uId").equalTo(muId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        point=myData.getPoints();
                        int d=Integer.parseInt(point);
                        if (d >= 50 && d < 100) {
                            level="Level 1";
                            } else if (d >= 100 && d < 250) {
                            level="Level 2";
                             } else if (d >= 250 && d < 500) {
                            level="Level 3";
                             } else if (d >= 500 && d < 750) {
                            level="Level 4";
                             } else if (d >= 750 && d < 1000) {
                            level="Level 5";
                            } else if (d >= 1000 && d < 2000) {
                            level="Level 6";
                            } else if (d >= 2000 && d < 3000) {
                            level="Level 7";
                             } else if (d >= 3000 && d < 5000) {
                            level="Level 8";
                            } else if (d >= 5000 && d < 10000) {
                            level="Level 9";
                            } else if (d > 10000) {
                            level="Level 10";
                             }
                    }

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
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}
