package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.model.CartModel;
import com.group5.sellit.model.OrdersModel;
import com.group5.sellit.model.Productmodel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Ordered_Product_details extends AppCompatActivity {
    private TextView product_Name, product_Description, product_Price, status, color, size;
    private ImageView product_Image, product_Image2, product_Image3, product_Image4, product_Image5, backarrow;
    private Button add_cart_Button, approvebtn,deliverybtn, ondeliverybtn;
    private ElegantNumberButton quantity_num;

    private String pid,categ;
    private String uid;
    private DatabaseReference statusref,getpidref;
    private String notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_product_details);
        uid = getIntent().getStringExtra("uid");
        pid = getIntent().getStringExtra("pid");
        getSupportActionBar().hide();

        statusref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(uid).child("Products");

        product_Description = (TextView) findViewById(R.id.product_info_description);
        product_Name = (TextView) findViewById(R.id.product_info_name);
        product_Price = (TextView) findViewById(R.id.product_info_price);
        color = (TextView) findViewById(R.id.product_info_color);
        size = (TextView) findViewById(R.id.product_info_size);
        product_Image = (ImageView) findViewById(R.id.product_info_img);
        product_Image2 = (ImageView) findViewById(R.id.product_info_img2);
        product_Image3 = (ImageView) findViewById(R.id.product_info_img3);
        product_Image4 = (ImageView) findViewById(R.id.product_info_img4);
        product_Image5 = (ImageView) findViewById(R.id.product_info_img5);
        backarrow = (ImageView) findViewById(R.id.back_arrow);
        status = (TextView) findViewById(R.id.status);

        add_cart_Button = (Button) findViewById(R.id.add_cart_btn);
        approvebtn = (Button) findViewById(R.id.approve_btn);
        deliverybtn = (Button) findViewById(R.id.delivered_btn);
        ondeliverybtn = (Button) findViewById(R.id.ondelivery_btn);


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(Ordered_Product_details.this);
//        builder.setContentTitle("Sell IT");
//        builder.setContentText("The Product "+ product_Name+" that you have purchased has been approved");
//        builder.setSmallIcon(R.drawable.sellitlogo);


        Product_Info(pid);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        approvebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveButton();
            }
        });

        deliverybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryButton();
            }
        });

        ondeliverybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ondeliveryButton();
            }
        });



    }

    private void ondeliveryButton() {
        pid = getIntent().getStringExtra("pid");

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("status2", "On Delivery");

        statusref.child(pid).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            status.setText("On Delivery");
                            Toast.makeText(Ordered_Product_details.this, "Product is now on Delivery", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Ordered_Product_details.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void deliveryButton() {
        pid = getIntent().getStringExtra("pid");

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("status2", "Delivered");

        statusref.child(pid).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            status.setText("Delivered");
                            Toast.makeText(Ordered_Product_details.this, "Product Delivered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Ordered_Product_details.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void ApproveButton() {
        pid = getIntent().getStringExtra("pid");

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("status", "Approved");

        statusref.child(pid).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            deliverybtn.setVisibility(View.VISIBLE);
                            status.setTextColor(Color.GREEN);
                            ondeliverybtn.setVisibility(View.VISIBLE);
                            approvebtn.setVisibility(View.INVISIBLE);

                            Toast.makeText(Ordered_Product_details.this, "Product Approved", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Ordered_Product_details.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    private void Product_Info(String pid) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        databaseReference.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Productmodel productmodel = snapshot.getValue(Productmodel.class);

                    product_Name.setText(productmodel.getName());
                    product_Description.setText(productmodel.getDescription());
                    product_Price.setText(productmodel.getPrice());
                    Picasso.get().load(productmodel.getImage()).into(product_Image);
                    Picasso.get().load(productmodel.getImage2()).into(product_Image2);
                    Picasso.get().load(productmodel.getImage3()).into(product_Image3);
                    Picasso.get().load(productmodel.getImage4()).into(product_Image4);
                    Picasso.get().load(productmodel.getImage5()).into(product_Image5);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View")
                .child(uid).child("Products");

        databaseReference2.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CartModel cartModel = snapshot.getValue(CartModel.class);
                categ = cartModel.getCategory();
                if (categ.equals("Clothes")){
                    color.setText("Color: "+ cartModel.getColor());
                    size.setText("Size: " + cartModel.getSize());
                }
                else {
                    color.setVisibility(View.INVISIBLE);
                    size.setVisibility(View.INVISIBLE);
                }


                if (snapshot.exists()){
                    OrdersModel ordersModel = snapshot.getValue(OrdersModel.class);
                    status.setText(ordersModel.getStatus2());
                    String stat = ordersModel.getStatus();

                    if (stat.equals("Approved")){
                        deliverybtn.setVisibility(View.VISIBLE);
                        ondeliverybtn.setVisibility(View.VISIBLE);
                        approvebtn.setVisibility(View.INVISIBLE);


                        status.setTextColor(Color.GREEN);
                    }
                    else if(stat.equals("Done")){
                        approvebtn.setVisibility(View.INVISIBLE);
                    }

                }
                else {
                    return;
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}