package com.group5.sellit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Buyer_admin_selection extends AppCompatActivity {

    Button userbtn,adminbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_admin_selection);
        getSupportActionBar().hide();

        userbtn = (Button) findViewById(R.id.buyer_btn);
        adminbtn = (Button) findViewById(R.id.admin_btn);

        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Buyer_admin_selection.this, USER_selection.class));
            }
        });

        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buyer_admin_selection.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }
}