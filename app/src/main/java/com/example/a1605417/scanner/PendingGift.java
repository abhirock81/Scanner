package com.example.a1605417.scanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class PendingGift extends AppCompatActivity {

    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    FirebaseDatabase mDatabase;
    ImageView QrimageView;
    DatabaseReference mDataReference;
    String mUid,mUsername;
    Intent intent;
    QRCodeWriter writer;
    Bitmap bitmap;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_gift);

        QrimageView=findViewById(R.id.qrImage);
        writer=new QRCodeWriter();
        mDatabase = FirebaseDatabase.getInstance();
        mDataReference = mDatabase.getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mFirebaseUser!= null;
        mUid=mFirebaseUser.getUid();
        intent=getIntent();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    mUsername = firebaseUser.getDisplayName();

                } else {
                    mAuth.signOut();
                }
            }
        };
        onQuery();


    }

    private void onQuery() {
        Query query=mDataReference.child("redeem").orderByChild("uId").equalTo(mUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        MyData myData = userSnapshot.getValue(MyData.class);
                        assert myData != null;
                        if (myData.getFlag().equals("0")) {
                            BitMatrix bitMatrix = null;
                            try {
                                bitMatrix = writer.encode(mUid+"?"+myData.getGifts(), BarcodeFormat.QR_CODE, 200, 200);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                            assert bitMatrix != null;
                            int width = bitMatrix.getWidth();
                            int height = bitMatrix.getHeight();
                            int[] pixels = new int[width * height];
                            for (int y = 0; y < height; y++) {
                                int offset = y * width;
                                for (int x = 0; x < width; x++) {
                                    pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
                                }
                            }

                            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                            QrimageView.setImageBitmap(bitmap);
                            flag=true;
                            break;
                        }
                    }
                    if(!flag){
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        Toast.makeText(PendingGift.this, "No Pending Gifts", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    Toast.makeText(PendingGift.this, "No Pending Gifts", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
