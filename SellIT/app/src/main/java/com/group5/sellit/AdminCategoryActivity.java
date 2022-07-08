package com.group5.sellit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView cat_headset, cat_computer, cat_clothes, cat_camera, cat_console, cat_mobile, cat_sports, cat_food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        getSupportActionBar().hide();

        cat_headset = (ImageView) findViewById(R.id.cat_headset);
        cat_computer = (ImageView) findViewById(R.id.cat_computer);
        cat_clothes = (ImageView) findViewById(R.id.cat_clothes);
        cat_camera = (ImageView) findViewById(R.id.cat_camera);
        cat_console = (ImageView) findViewById(R.id.cat_console);
        cat_mobile = (ImageView) findViewById(R.id.cat_mobile);
        cat_sports = (ImageView) findViewById(R.id.cat_sports);
        cat_food = (ImageView) findViewById(R.id.cat_food);

        //ImageView
        cat_headset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProduct.class);
                intent.putExtra("category", "Headset");
                startActivity(intent);
            }
        });

        cat_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProduct.class);
                intent.putExtra("category", "Computer");
                startActivity(intent);
            }
        });

        cat_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProduct.class);
                intent.putExtra("category", "Clothes");
                startActivity(intent);
            }
        });

        cat_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProduct.class);
                intent.putExtra("category", "Camera");
                startActivity(intent);
            }
        });

        cat_console.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProduct.class);
                intent.putExtra("category", "Console");
                startActivity(intent);
            }
        });

        cat_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProduct.class);
                intent.putExtra("category", "Mobile");
                startActivity(intent);
            }
        });

        cat_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProduct.class);
                intent.putExtra("category", "Sports");
                startActivity(intent);
            }
        });

        cat_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProduct.class);
                intent.putExtra("category", "Foods");
                startActivity(intent);
            }
        });


    }
}