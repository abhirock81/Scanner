package com.example.a1605417.scanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class PopupActivity extends AppCompatActivity {
Button give_review,check_review,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        give_review=findViewById(R.id.givereviews);
        check_review=findViewById(R.id.viewreviews);
        back=findViewById(R.id.back);


    }
}
