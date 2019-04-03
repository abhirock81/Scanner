package com.example.a1605417.scanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    TextView codeTextView;
    EditText codeEditText;
    Intent intent;
    String number, name, place;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername, mEmailId, mUID;
    int remaining;
    int redeempoints;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        codeEditText = findViewById(R.id.codeeditText);
        codeTextView = findViewById(R.id.codetextView);
        intent = getIntent();
        if (intent.hasExtra("number")) {
            number = intent.getStringExtra("number");
        }
        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name");
        }
        if (intent.hasExtra("place")) {
            place = intent.getStringExtra("place");
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        mUsername = firebaseUser.getDisplayName();
        mEmailId = firebaseUser.getEmail();
        mUID = firebaseUser.getUid();
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

        sendVerification(number);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }

    private void sendVerification(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                codeEditText.setText(code);
                RedeemData redeemData = new RedeemData(name, number, place, mUID);
                mDatabaseReference.child("userdetails").push().setValue(redeemData);
                startActivity(new Intent(getApplicationContext(),RewardsRedeem.class));
                }
            }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }
    };

    private void onPointsUpdated() {
        Query query = mDatabaseReference.child("users").orderByChild("uId").equalTo(mUID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        int total = Integer.parseInt(myData.getPoints());
                        if(total>=50&&total<100){remaining=total-50;}
                        else if(total>=100&&total<250){remaining=total-100;}
                        else if(total>=250&&total<500){remaining=total-250;}
                        else if(total>=500&&total<750){remaining=total-500;}
                        else if(total>=750&&total<1000){remaining=total-750;}
                        else if(total>=1000&&total<2000){remaining=total-1000;}
                        else if(total>=2000&&total<3000){remaining=total-2000;}
                        else if(total>=3000&&total<5000){remaining=total-3000;}
                        else if(total>=5000&&total<10000){remaining=total-5000;}
                        else if(total>10000){remaining=total-10000;}
                               userSnapshot.getRef().child("points").setValue(""+remaining);
                    }
                    alertDialog = new AlertDialog.Builder(OtpVerification.this)
                            .setTitle("Message")
                            .setMessage("points will be redeemed within 48 hours!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    RedeemData redeemData = new RedeemData(name, number, place, mUID,""+redeempoints);
                                    mDatabaseReference.child("redeem").push().setValue(redeemData);
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

                 }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
}

