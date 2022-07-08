package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.databinding.ActivityAdminDasboardBinding;
import com.group5.sellit.databinding.ActivityHome1Binding;
import com.group5.sellit.model.AdminModel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class AdminDasboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminDasboardBinding binding;
    private CardView addpro, order_card, logout_card;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dasboard);
        getSupportActionBar().hide();


        TextView adminname = (TextView) findViewById(R.id.adminname);

        Paper.init(this);

        uid = getIntent().getStringExtra("uid");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Admins");

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AdminModel adminModel = snapshot.getValue(AdminModel.class);

                adminname.setText(adminModel.getUsername());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding = ActivityAdminDasboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.adminDrawerLayout;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);





    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_adminprofile){


        }

        else if (id == R.id.nav_addproduct){
            Intent intent = new Intent(AdminDasboard.this, AdminCategoryActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_adminorders){
            Intent intent = new Intent(AdminDasboard.this, Orders_Activity.class);
            startActivity(intent);

        }


        else if (id == R.id.nav_adminlogout){
            Paper.book().destroy();
            startActivity(new Intent(AdminDasboard.this, MainActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout)  findViewById(R.id.admin_drawer_Layout);
        drawer.closeDrawer(GravityCompat.START);



        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        startActivity(new Intent(AdminDasboard.this, MainActivity.class));
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        addpro = (CardView) findViewById(R.id.admin_addProducts_cardview);
        order_card = (CardView) findViewById(R.id.admin_order_cardview);
        logout_card = (CardView) findViewById(R.id.admin_logout_cardview);

        addpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDasboard.this, AddProduct_Activity.class));

            }
        });

        order_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDasboard.this, Orders_Activity.class);
                startActivity(intent);

            }
        });

        logout_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                Toast.makeText(AdminDasboard.this, "Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminDasboard.this, MainActivity.class));
                finish();

            }
        });
    }
}