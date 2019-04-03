package com.example.a1605417.scanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int RC_SIGN_IN = 1;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String mUsername, mEmail;
    Button scanQrButton,viewReviewsButton;
    TextView nameTextView;
    ImageView imageView;
    String sharedpreferencevalue = "false";
    TextView logoutTextView;
    TextView levelTextView;
    SharedPreferences prefs;
    String DeviceNo;
    String level;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    TextView  pointsTextView,reviewTextView,reviewLink,levelLink;
    String points="0";
    String mUserId,photoUrl;
    Intent intent;
    ImageView levelImageView;
    Button redeemTextView;
    ProgressBar progressBar;
    int chpoints=0;
    String flag="0";
    String call;
    Button check_review,give_review,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        logoutTextView = findViewById(R.id.logout);
        scanQrButton = findViewById(R.id.scanQR);
        nameTextView = findViewById(R.id.headertext);
        reviewTextView=findViewById(R.id.reviewtextview);
        reviewLink=findViewById(R.id.reviewlink);
        levelLink=findViewById(R.id.levellink);

        imageView = findViewById(R.id.imageView);
        pointsTextView=findViewById(R.id.points);
        levelImageView=findViewById(R.id.levelImageview);
        redeemTextView=findViewById(R.id.redeemtextview);
        levelTextView=findViewById(R.id.level_textView);
        progressBar=findViewById(R.id.progressBar2);
        viewReviewsButton=findViewById(R.id.viewreviews);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        // prefs = getApplicationContext().getSharedPreferences("testcase",0);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    onSignedInIntialize(firebaseUser.getDisplayName(),firebaseUser.getEmail(),firebaseUser.getPhotoUrl(),firebaseUser.getUid());

                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build()
                                    ))
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        /*logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getApplicationContext());
                points="0";
            }
        });*/
        reviewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ReviewList.class);
                intent.putExtra("myreviews","myreviews");
                startActivity(intent);
            }
        });
        levelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,RewardList.class));
            }
        });

        scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                final View view1 = layoutInflater.inflate(R.layout.after_scan_popup, null);
                final PopupWindow popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                give_review = view1.findViewById(R.id.givereviews);
                check_review = view1.findViewById(R.id.viewreviews);
                back = view1.findViewById(R.id.back);
                popupWindow.setAnimationStyle(R.style.popup_window_animation_phone);
                popupWindow.setElevation(120);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                popupWindow.update();

                give_review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),ScannedBarcodeActivity.class);
                        intent.putExtra("flag","give_review");
                        startActivity(intent);
                    }
                });
                check_review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),ScannedBarcodeActivity.class);
                        intent.putExtra("flag","check_review");
                        startActivity(intent);
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
        redeemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query=mDatabaseReference.child("userdetails").orderByChild("uId").equalTo(mUserId);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            startActivity(new Intent(getApplicationContext(),RewardsRedeem.class));
                        }
                        else{
                            Intent intent=new Intent(getApplicationContext(),Redeem.class);
                            intent.putExtra("points",points);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }});
        viewReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CategoryReviews.class));
            }
        });
        }

    private void onSignedOutCleanup() {
        mUsername="ANONYMOUS";
        AuthUI.getInstance().signOut(this);

    }
    private void onSignedInIntialize(String displayName, String email, final Uri photourl, String uid) {

        mUsername = displayName;
        mEmail=email;
        mUserId=uid;
        photoUrl=photourl.toString();
        nameTextView.setText(mUsername);
        Glide.with(getApplicationContext()).load(photourl.toString()).apply(RequestOptions.circleCropTransform()).into(imageView);
        DeviceNo = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        Query query = mDatabaseReference.child("deviceId").orderByChild("deviceNo").equalTo(DeviceNo);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        pointsTextView.setText("0");

                    }
                } else {
                    points="50";
                    pointsTextView.setText(points);
                    MyData myData=new MyData(DeviceNo,points);
                    mDatabaseReference.child("deviceId").push().setValue(myData);

                }
                onQuery0();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    onPhotoQuery();
    onCountReviews();
    }

    private void onCountReviews() {
        Query query=mDatabaseReference.child("reviews").orderByChild("personEmail").equalTo(mEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int i=0;
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        if(myData.getFlag().equals("2"))
                          i++;
                    }
                   reviewTextView.setText(""+i);
                }
                else{
                    reviewTextView.setText("No Reviews");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onPhotoQuery() {
        Query newQuery=mDatabaseReference.child("userPhoto").orderByChild("uId").equalTo(mUserId);
        newQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    }
                }
                else{
                    // MyData mdata=new MyData(mEmail, photoUrl mUserId)
                    HashMap<String,String> dataMap=new HashMap<>();
                    dataMap.put("uId",mUserId);
                    dataMap.put("photoUrl",photoUrl);
                    dataMap.put("personEmail",mEmail);
                    mDatabaseReference.child("userPhoto").push().setValue(dataMap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onQuery0() {
        Query query1 = mDatabaseReference.child("reviews").orderByChild("personEmail").equalTo(mEmail);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if(myData.getFlag().equals("1")){
                            flag=myData.getFlag();
                            String[] wordArray = myData.getEnteredReview().trim().split("\\s+");
                            int wordCount = wordArray.length;
                            if(wordCount<20)
                                chpoints=chpoints+10;
                            else chpoints=chpoints+5+10;
                            onupdateflag(userSnapshot);
                        }
                    }

                }
                if(flag.equals("1")){onPointUpdated();
                flag="0";}
                else
                    onQuery();
            }

            private void onupdateflag(DataSnapshot userSnapshot) {

                userSnapshot.getRef().child("flag").setValue("2");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onPointUpdated() {
        Query query = mDatabaseReference.child("users").orderByChild("uId").equalTo(mUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        String retrievedpoints = myData.getPoints();
                        userSnapshot.getRef().child("level").removeValue();
                        chpoints=chpoints+Integer.parseInt(retrievedpoints);
                        userSnapshot.getRef().child("points").setValue("" + chpoints);
                        //userSnapshot.getRef().child("level").setValue();
                    }
                }
                chpoints=0;
                onQuery();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onQuery() {
        Query query1 = mDatabaseReference.child("users").orderByChild("uId").equalTo(mUserId);
        query1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        points = myData.getPoints();
                        level=myData.getLevel();
                        pointsTextView.setText(myData.getPoints());
                    }
                } else {
                    MyData myData = new MyData(mUsername, mEmail, photoUrl ,points,mUserId);
                    mDatabaseReference.child("users").push().setValue(myData);
                }
                levelUpdated(points,level);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void levelUpdated(String points,String level) {
        int d;
        if (level != null) {

            if (level.equals("1")) {
                levelTextView.setText("1");
                levelImageView.setImageResource(R.drawable.l1);
            } else if (level.equals("2")) {
                levelTextView.setText("2");
                levelImageView.setImageResource(R.drawable.l2);
            } else if (level.equals("3")) {
                levelTextView.setText("3");
                levelImageView.setImageResource(R.drawable.l3);
            } else if (level.equals("4")) {
                levelTextView.setText("4");
                levelImageView.setImageResource(R.drawable.l4);
            } else if (level.equals("5")) {
                levelTextView.setText("5");
                levelImageView.setImageResource(R.drawable.l5);
            } else if (level.equals("6")) {
                levelTextView.setText("6");
                levelImageView.setImageResource(R.drawable.l6);
            } else if (level.equals("7")) {
                levelTextView.setText("7");
                levelImageView.setImageResource(R.drawable.l7);
            } else if (level.equals("8")) {
                levelTextView.setText("8");
                levelImageView.setImageResource(R.drawable.l8);
            } else if (level.equals("9")) {
                levelTextView.setText("9");
                levelImageView.setImageResource(R.drawable.l9);
            } else if (level.equals("10")) {
                levelTextView.setText("10");
                levelImageView.setImageResource(R.drawable.l10);
            }

        } else {
            d = Integer.parseInt(points);
            if (d < 50) {
                levelTextView.setText("0");
                redeemTextView.setVisibility(View.GONE);
                levelImageView.setVisibility(View.GONE);
            }
            if (d >= 50 && d < 100) {
                levelTextView.setText("1");
                levelImageView.setImageResource(R.drawable.l1);
            } else if (d >= 100 && d < 250) {
                levelTextView.setText("2");
                levelImageView.setImageResource(R.drawable.l2);
            } else if (d >= 250 && d < 500) {
                levelTextView.setText("3");
                levelImageView.setImageResource(R.drawable.l3);
            } else if (d >= 500 && d < 750) {
                levelTextView.setText("4");
                levelImageView.setImageResource(R.drawable.l4);
            } else if (d >= 750 && d < 1000) {
                levelTextView.setText("5");
                levelImageView.setImageResource(R.drawable.l5);
            } else if (d >= 1000 && d < 2000) {
                levelTextView.setText("6");
                levelImageView.setImageResource(R.drawable.l6);
            } else if (d >= 2000 && d < 3000) {
                levelTextView.setText("7");
                levelImageView.setImageResource(R.drawable.l7);
            } else if (d >= 3000 && d < 5000) {
                levelTextView.setText("8");
                levelImageView.setImageResource(R.drawable.l8);
            } else if (d >= 5000 && d < 10000) {
                levelTextView.setText("9");
                levelImageView.setImageResource(R.drawable.l9);
            } else if (d > 10000) {
                levelTextView.setText("10");
                levelImageView.setImageResource(R.drawable.l10);
            }
        }
    }

      /* if (prefs.getBoolean("firstrun", true)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstrun", false);
            editor.apply();
            Toast.makeText(getApplicationContext(),"asdf",Toast.LENGTH_LONG).show();

        }*/

    /*private void requestBackup() {
        BackupManager backupManager = new BackupManager(this);
        backupManager.dataChanged();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Signed in",Toast.LENGTH_LONG).show();
            } else if(resultCode == RESULT_CANCELED){
                finishAffinity();
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            AuthUI.getInstance().signOut(getApplicationContext());
            points="0";
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(mAuthStateListener);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(mAuthStateListener);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myreviews) {
            Intent intent=new Intent(getApplicationContext(),ReviewList.class);
            intent.putExtra("myreviews","myreviews");
            startActivity(intent);
        } else if (id == R.id.levelofrewards) {
            startActivity(new Intent(getApplicationContext(),RewardList.class));

        }
        else if (id==R.id.pendingRewardRequest){
            startActivity(new Intent(getApplicationContext(),PendingGift.class));
        }
        else if(id==R.id.forservicepurpose){
            Query query=mDatabaseReference.child("onSpotRewardApproval").orderByChild("userId").equalTo(mEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        startActivity(new Intent(getApplicationContext(),ServiceScanner.class));
                    }
                    else{
                        Toast.makeText(HomeActivity.this, "You are not permitted", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    /*   else if(id==R.id.abc){
              onAdd();
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




 /*   private void onAdd() {
        Query query=mDatabaseReference.child("product");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        MyData myData=userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        userSnapshot.getRef().child("address").removeValue();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }*/
}
