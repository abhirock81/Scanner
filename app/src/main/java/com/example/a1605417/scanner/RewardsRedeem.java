package com.example.a1605417.scanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;

public class RewardsRedeem extends AppCompatActivity {
   TextView levelTextView,pointTextView;
    DatabaseReference mDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername,mEmailId,mUid;
    RecyclerView recyclerView;
    RedeemData redeemData;
    RewardShowcaseAdapter rewardShowcaseAdapter;
    List<RedeemData> redeemDataList=new ArrayList<RedeemData>();
    Button redeemButton;
    String mNumber;
     Intent intent;
    boolean flag=false;
    String number;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_redeem);
        levelTextView=findViewById(R.id.leveldisplaytextView);
        pointTextView=findViewById(R.id.pointdisplaytextView);
        recyclerView=findViewById(R.id.rewarditemrecyclerview);
        redeemButton=findViewById(R.id.redeembutton);
        radioGroup=findViewById(R.id.radioGroup3);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       assert firebaseUser != null;
       mUsername = firebaseUser.getDisplayName();
       mEmailId=firebaseUser.getEmail();
       mUid=firebaseUser.getUid();
       mNumber=firebaseUser.getPhoneNumber();
        //checkImeiPermission();
        onRetrieveNumber();
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
       onQuery();
        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(RewardShowcaseAdapter.points>0&&RewardShowcaseAdapter.points<=Integer.parseInt(pointTextView.getText().toString().trim())){
                  intent=new Intent(getApplicationContext(),RedeemMethod.class);
                    intent.putExtra("gifts",RewardShowcaseAdapter.gift);
                    intent.putExtra("redeemMethod",radioButton.getText().toString());
                    Query query=mDatabaseReference.child("redeem").orderByChild("uId").equalTo(mUid);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                                    MyData myData=userSnapshot.getValue(MyData.class);
                                    assert myData != null;
                                    if(myData.getFlag().equals("0")){
                                        flag=true;
                                        break;
                                    }
                                }
                            }
                            else
                            { addData();
                            flag=true;}

                            if(!flag){
                                addData();
                            }
                            else{
                                Toast.makeText(RewardsRedeem.this, "Your Last request is pending", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
   }

    private void onRetrieveNumber() {
    Query query=mDatabaseReference.child("userdetails").orderByChild("uId").equalTo(mUid);
    query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                 MyData myData=userSnapshot.getValue(MyData.class);
                    assert myData != null;
                    number=myData.getNumber();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

    private void addData() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String doa = df.format(c);
        Date currentTime = Calendar.getInstance().getTime();
        HashMap<String,String> dataMap=new HashMap<>();
        dataMap.put("uId",mUid);
        dataMap.put("personName",mUsername);
        dataMap.put("personEmail",mEmailId);
        dataMap.put("number",number);
        dataMap.put("points",pointTextView.getText().toString().trim());
        dataMap.put("level",levelTextView.getText().toString().trim());
        dataMap.put("gifts",RewardShowcaseAdapter.gift);
        dataMap.put("redeemPoints",""+RewardShowcaseAdapter.points);
        dataMap.put("dateOfJoin",doa);
        dataMap.put("currentTime",currentTime.toString());
        dataMap.put("flag","0");
        dataMap.put("redeemMethod",radioButton.getText().toString());
        mDatabaseReference.child("redeem").push().setValue(dataMap);
        RewardShowcaseAdapter.points=0;
        RewardShowcaseAdapter.gift="";
        startActivity(intent);
    }

    private void checkImeiPermission() {

    }

    private void onPrepareList() {
        if(radioButton.getText().toString().equalsIgnoreCase("OnSpotGift")){
            redeemDataList.clear();
            redeemData=new RedeemData("KeyChain",getResources().getDrawable(R.drawable.keychain),pointTextView.getText().toString().trim(),50);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Pen",getResources().getDrawable(R.drawable.pen),pointTextView.getText().toString().trim(),100);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Cap",getResources().getDrawable(R.drawable.cap),pointTextView.getText().toString().trim(),750);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Wallet",getResources().getDrawable(R.drawable.wallet),pointTextView.getText().toString().trim(),2000);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Tshirt",getResources().getDrawable(R.drawable.tshirt),pointTextView.getText().toString().trim(),3000);
            redeemDataList.add(redeemData);
        }
        else   if(radioButton.getText().toString().equalsIgnoreCase("Others")){
            redeemDataList.clear();
            redeemData=new RedeemData("Rs 10",getResources().getDrawable(R.drawable.rupee),pointTextView.getText().toString().trim(),50);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("KeyChain",getResources().getDrawable(R.drawable.keychain),pointTextView.getText().toString().trim(),50);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Rs 20",getResources().getDrawable(R.drawable.rupee),pointTextView.getText().toString().trim(),100);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Pen",getResources().getDrawable(R.drawable.pen),pointTextView.getText().toString().trim(),100);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Rs 30",getResources().getDrawable(R.drawable.rupee),pointTextView.getText().toString().trim(),250);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Rs 40",getResources().getDrawable(R.drawable.rupee),pointTextView.getText().toString().trim(),500);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Cap",getResources().getDrawable(R.drawable.cap),pointTextView.getText().toString().trim(),750);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Rs 50",getResources().getDrawable(R.drawable.rupee),pointTextView.getText().toString().trim(),750);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Mobile Cover",getResources().getDrawable(R.drawable.mobilecover),pointTextView.getText().toString().trim(),1000);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Wallet",getResources().getDrawable(R.drawable.wallet),pointTextView.getText().toString().trim(),2000);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Tshirt",getResources().getDrawable(R.drawable.tshirt),pointTextView.getText().toString().trim(),3000);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Movie Ticket",getResources().getDrawable(R.drawable.movieticket),pointTextView.getText().toString().trim(),5000);
            redeemDataList.add(redeemData);
            redeemData=new RedeemData("Lunch @BBQ Nation",getResources().getDrawable(R.drawable.bbqnation),pointTextView.getText().toString().trim(),10000);
            redeemDataList.add(redeemData);
        }
        rewardShowcaseAdapter=new RewardShowcaseAdapter(getApplicationContext(),redeemDataList);
        recyclerView.setAdapter(rewardShowcaseAdapter);
    }

    private void onQuery() {
        Query query=mDatabaseReference.child("users").orderByChild("uId").equalTo(mUid);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        pointTextView.setText(myData.getPoints());
                        int d=Integer.parseInt(myData.getPoints());
                        if(d>=50&&d<100){levelTextView.setText("LEVEL 1");
                         }
                        else if(d>=100&&d<250){levelTextView.setText("LEVEL 2");}
                        else if(d>=250&&d<500){levelTextView.setText("LEVEL 3");}
                        else if(d>=500&&d<750){levelTextView.setText("LEVEL 4");}
                        else if(d>=750&&d<1000){levelTextView.setText("LEVEL 5");}
                        else if(d>=1000&&d<2000){levelTextView.setText("LEVEL 6");}
                        else if(d>=2000&&d<3000){levelTextView.setText("LEVEL 7");}
                        else if(d>=3000&&d<5000){levelTextView.setText("LEVEL 8");}
                        else if(d>=5000&&d<10000){levelTextView.setText("LEVEL 9");}
                        else if(d>10000){levelTextView.setText("LEVEL 10");}
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onCheck(View view) {
    /*   switch (view.getId()){
           case R.id.tenrupeetextView :
               if(tenRadioButton.isChecked())
                   tenRadioButton.setChecked(false);
               else
                 tenRadioButton.setChecked(true);
               break;
           case R.id.keychaintextView :
               if(keyRadioButton.isChecked())
                   keyRadioButton.setChecked(false);
               else
                   keyRadioButton.setChecked(true);
               break;
           case R.id.twentyrupeetextView :
               if(twentyRadioButton.isChecked())
                   twentyRadioButton.setChecked(false);
               else
                   twentyRadioButton.setChecked(true);
               break;
           case R.id.pentextView :
               if(penRadioButton.isChecked())
                   penRadioButton.setChecked(false);
               else
                   penRadioButton.setChecked(true);
               break;
           case R.id.thirtyrupeetextView :
               if(thirtyRadioButton.isChecked())
                   thirtyRadioButton.setChecked(false);
               else
                   thirtyRadioButton.setChecked(true);
               break;
           case R.id.fortytextView :
               if(fortyRadioButton.isChecked())
                   fortyRadioButton.setChecked(false);
               else
                   fortyRadioButton.setChecked(true);
               break;
           case R.id.captextView :
               if(capRadioButton.isChecked())
                   capRadioButton.setChecked(false);
               else
                   capRadioButton.setChecked(true);
               break;
           case R.id.fiftytextView :
               if(fiftyRadioButton.isChecked())
                   fiftyRadioButton.setChecked(false);
               else
                   fiftyRadioButton.setChecked(true);
               break;
           case R.id.wallettextView :
               if(walletRadioButton.isChecked())
                   walletRadioButton.setChecked(false);
               else
                   walletRadioButton.setChecked(true);
               break;
           case R.id.tshirttextView :
               if(tshirtRadioButton.isChecked())
                   tshirtRadioButton.setChecked(false);
               else
                   tshirtRadioButton.setChecked(true);
               break;
           case R.id.tickettextView :
               if(ticketRadioButton.isChecked())
                   ticketRadioButton.setChecked(false);
               else
                   ticketRadioButton.setChecked(true);
               break;
           case R.id.bbqtextView :
               if(bbqRadioButton.isChecked())
                   bbqRadioButton.setChecked(false);
               else
                   bbqRadioButton.setChecked(true);
               break;
           case R.id.covertextView :
               if(coverRadioButton.isChecked())
                   coverRadioButton.setChecked(false);
               else
                   coverRadioButton.setChecked(true);
               break;
       }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }

    public void selectGiftType(View view) {
        switch(view.getId()){
            case R.id.onspotgift :
                int rid=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(rid);
                onPrepareList();
                break;
            case R.id.other :
                int rid1=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(rid1);
                onPrepareList();
                break;
        }
    }
}
