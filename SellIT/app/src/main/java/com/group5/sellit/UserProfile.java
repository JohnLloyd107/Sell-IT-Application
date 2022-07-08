package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.model.Productmodel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private TextView Username, Usernum, UserAge, UserAddress, UserEmail,Edit;
    private CircleImageView profile_image;
    private Button pending, recieve, complete;
    private String imgid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        Username = (TextView) findViewById(R.id.acc_username);
        Usernum = (TextView) findViewById(R.id.acc_phonenumber);
        UserAddress = (TextView) findViewById(R.id.acc_address);
        UserAge = (TextView) findViewById(R.id.acc_Age);
        UserEmail = (TextView) findViewById(R.id.acc_email);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        Edit = (TextView) findViewById(R.id.edit_btn);

        imgid = Prevalent.currentUser.getProf_img();

        pending = (Button) findViewById(R.id.pending);
        recieve = (Button) findViewById(R.id.recieve);
        complete = (Button) findViewById(R.id.complete);




        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,Pending_Activity.class);
                startActivity(intent);
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,Profile_edit_Activity.class);
                startActivity(intent);
            }
        });

        recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,To_Receive_Activity.class);
                startActivity(intent);
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,Complete_Activity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.child(Prevalent.currentUser.getKeyid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                Username.setText(users.getUsername());
                Usernum.setText(users.getPhonenumber());
                UserAddress.setText(users.getAddress());
                UserAge.setText(users.getAge());
                UserEmail.setText(users.getEmail());
                Picasso.get().load(users.getProf_img()).into(profile_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}