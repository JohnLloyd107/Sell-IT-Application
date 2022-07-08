package com.group5.sellit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class USER_selection extends AppCompatActivity {

    Button oldbtn,newbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        getSupportActionBar().hide();

        oldbtn = (Button) findViewById(R.id.olduser_btn);
        newbtn = (Button) findViewById(R.id.newuser_btn);

        oldbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(USER_selection.this,MainActivity.class);
                startActivity(intent);
            }
        });

        newbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(USER_selection.this, Register.class));
            }
        });
    }
}