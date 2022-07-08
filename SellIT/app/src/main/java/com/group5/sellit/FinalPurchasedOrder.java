package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FinalPurchasedOrder extends AppCompatActivity {

    private EditText customer_name, customer_address, customer_number;
    private Button buy_btn;
    String totalP,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_purchased_order);
        getSupportActionBar().hide();

        totalP = getIntent().getStringExtra("total_price");
        id = getIntent().getStringExtra("id");

        customer_name = (EditText) findViewById(R.id.customer_name);
        customer_address = (EditText) findViewById(R.id.customer_address);
        customer_number = (EditText) findViewById(R.id.customer_Number);

        buy_btn = (Button) findViewById(R.id.buy_button);

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Check();
            }
        });



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.child(Prevalent.currentUser.getKeyid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                customer_name.setText(users.getUsername());
                customer_number.setText(users.getPhonenumber());
                customer_address.setText(users.getAddress());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Check() {

        String number = customer_number.getText().toString();

        if (TextUtils.isEmpty(customer_name.getText().toString())){
            customer_name.setError("Name is required!");
            customer_name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(customer_address.getText().toString())){
            customer_address.setError("Address is required!");
            customer_address.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(customer_number.getText().toString())){
            customer_number.setError("Phone Number is required!");
            customer_number.requestFocus();
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(FinalPurchasedOrder.this);
        ProgressDialog loader = new ProgressDialog(this);

        alert.setTitle("Confirm Purchase");
        alert.setMessage("Are you sure you want to Purchase your Order?");

        alert.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ConfirmMethod();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                return;

            }
        });
        alert.create().show();






    }

    private void ConfirmMethod() {

        final String scurrentDate, scurrentTime;

        Calendar getDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        scurrentDate = currentDate.format(getDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        scurrentTime = currentTime.format(getDate.getTime());

        final DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentUser.getPhonenumber());

        HashMap<String ,Object> orderMap = new HashMap<>();
        orderMap.put("customer_name", customer_name.getText().toString());
        orderMap.put("customer_address", customer_address.getText().toString());
        orderMap.put("customer_number", customer_number.getText().toString());
        orderMap.put("total_amount", totalP);
        orderMap.put("time", scurrentTime);
        orderMap.put("date", scurrentDate);
        orderMap.put("status", "Not Shipped");
        orderMap.put("ID", id);
        orderMap.put("img_prof",Prevalent.currentUser.getProf_img());

        OrdersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(Prevalent.currentUser.getPhonenumber()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(FinalPurchasedOrder.this, "Your Final Order hads been placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(FinalPurchasedOrder.this, Home1Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });


    }
}