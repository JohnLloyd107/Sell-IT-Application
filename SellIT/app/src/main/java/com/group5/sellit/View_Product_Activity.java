package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class View_Product_Activity extends AppCompatActivity {
    private TextView product_Name, product_Description, product_Price, status;
    private ImageView product_Image, product_Image2, product_Image3, product_Image4, product_Image5, backarrow;
    private Button add_cart_Button, recieved_btn,rate_btn,skip_btn;
    private ElegantNumberButton quantity_num;
    private String pid,savecurrentdate,savecurrenttime,randomKey;
    private EditText feedback_input;
    private RatingBar rate_bar;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogbuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        getSupportActionBar().hide();

        pid = getIntent().getStringExtra("pid");

        product_Description = (TextView) findViewById(R.id.product_info_description);
        product_Name = (TextView) findViewById(R.id.product_info_name);
        product_Price = (TextView) findViewById(R.id.product_info_price);
        status= (TextView) findViewById(R.id.status_view);
        product_Image = (ImageView) findViewById(R.id.product_info_img);
        product_Image2 = (ImageView) findViewById(R.id.product_info_img2);
        product_Image3 = (ImageView) findViewById(R.id.product_info_img3);
        product_Image4 = (ImageView) findViewById(R.id.product_info_img4);
        product_Image5 = (ImageView) findViewById(R.id.product_info_img5);
        backarrow = (ImageView) findViewById(R.id.back_arrow);

        recieved_btn = (Button) findViewById(R.id.recieved_btn);


        Product_Info(pid);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recieved_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DatabaseReference statusref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Prevalent.currentUser.getPhonenumber()).child("Products");
                    Toast.makeText(View_Product_Activity.this, "Product Recieved", Toast.LENGTH_SHORT).show();

                    pid = getIntent().getStringExtra("pid");

                    HashMap<String, Object> productMap = new HashMap<>();
                    productMap.put("status2", "Complete");
                    productMap.put("status", "Done");

                    statusref.child(pid).updateChildren(productMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Ratingfunc();

                                    }
                                    else{
                                        Toast.makeText(View_Product_Activity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

            }
        });


    }

    private void Ratingfunc() {
        dialogbuilder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.rating_popup, null);
        rate_bar = (RatingBar) popup.findViewById(R.id.rate_bar);
        rate_btn = (Button) popup.findViewById(R.id.rate_btn);
        skip_btn = (Button) popup.findViewById(R.id.skip_btn);
        feedback_input = (EditText) popup.findViewById(R.id.feedback_input);

        dialogbuilder.setView(popup);
        dialog = dialogbuilder.create();
        dialog.show();

        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feed = feedback_input.getText().toString();
                String rate = String.valueOf(rate_bar.getRating());

                if (feed.isEmpty()){
                    feedback_input.setError("Feedback is required!");
                    feedback_input.requestFocus();
                    return;
                }

                else{

                    final DatabaseReference userfeedref = FirebaseDatabase.getInstance().getReference().child("Product Feedback").child(pid);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM, dd, yyyy");
                    savecurrentdate = currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    savecurrenttime = currentTime.format(calendar.getTime());

                    randomKey = savecurrentdate + savecurrenttime;
                    HashMap<String, Object> feedMap = new HashMap<>();
                    feedMap.put("name", Prevalent.currentUser.getUsername());
                    feedMap.put("userfeed", feed);
                    feedMap.put("date", randomKey);
                    feedMap.put("rate", rate);

                    userfeedref.child(Prevalent.currentUser.getPhonenumber()).updateChildren(feedMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        RateSystem();
                                        status.setText("On Delivery");
                                        Toast.makeText(View_Product_Activity.this, "Product is now Delivered", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(View_Product_Activity.this,To_Receive_Activity.class));

                                    }
                                }
                            });




                }

            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("On Delivery");
                Toast.makeText(View_Product_Activity.this, "Product is now Delivered", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(View_Product_Activity.this,To_Receive_Activity.class));


            }
        });

    }

    private void RateSystem() {

        String rate = String.valueOf(rate_bar.getRating());
        Toast.makeText(View_Product_Activity.this, rate +" Stars", Toast.LENGTH_SHORT).show();

        final DatabaseReference userrateref = FirebaseDatabase.getInstance().getReference().child("Product Ratings");
        HashMap<String, Object> rateMap = new HashMap<>();
        rateMap.put("userrate", rate);

        userrateref.child(Prevalent.currentUser.getPhonenumber()).child(pid).updateChildren(rateMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(View_Product_Activity.this, "Your Rating has been save", Toast.LENGTH_SHORT).show();
                            rate_btn.setVisibility(View.INVISIBLE);
                            rate_bar.setVisibility(View.INVISIBLE);

                            final DatabaseReference prodrateref = FirebaseDatabase.getInstance().getReference().child("Products");
                            prodrateref.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Productmodel prodmodel = snapshot.getValue(Productmodel.class);
                                    String maxuserrate = prodmodel.getRatemaxusernum();
                                    int i=Integer.parseInt(maxuserrate.replaceAll("[\\D]",""));
                                    Toast.makeText(View_Product_Activity.this, maxuserrate, Toast.LENGTH_SHORT).show();
                                    int totalrateuser = i + 1;
                                    String total = Integer.toString(totalrateuser);

                                    //5.0 Ratings
                                    if (rate.equals("5.0")){
                                        String star5_0 = prodmodel.getStar5_0();
                                        int totalstars5=Integer.parseInt(star5_0.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star5_0", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }

                                    //4.5 Ratings
                                    else if (rate.equals("4.5")){
                                        String star4_5 = prodmodel.getStar4_5();
                                        int totalstars5=Integer.parseInt(star4_5.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star4_5", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }

                                    //4.0 Ratings
                                    else if (rate.equals("4.0")){
                                        String star4_0 = prodmodel.getStar4_0();
                                        int totalstars5=Integer.parseInt(star4_0.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star4_0", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }

                                    //3.5 Ratings
                                    else if (rate.equals("3.5")){
                                        String star3_5 = prodmodel.getStar3_5();
                                        int totalstars5=Integer.parseInt(star3_5.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star3_5", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }

                                    //3.0 Ratings
                                    else if (rate.equals("3.0")){
                                        String star3_0 = prodmodel.getStar3_0();
                                        int totalstars5=Integer.parseInt(star3_0.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star3_0", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }

                                    //2.5 Ratings
                                    else if (rate.equals("2.5")){
                                        String star2_5 = prodmodel.getStar2_5();
                                        int totalstars5=Integer.parseInt(star2_5.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star2_5", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }

                                    //2.0 Ratings
                                    else if (rate.equals("2.0")){
                                        String star2_0 = prodmodel.getStar2_0();
                                        int totalstars5=Integer.parseInt(star2_0.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star2_0", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }

                                    //1.5 Ratings
                                    else if (rate.equals("1.5")){
                                        String star1_5 = prodmodel.getStar1_5();
                                        int totalstars5=Integer.parseInt(star1_5.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star1_5", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }

                                    //1.0 Ratings
                                    else if (rate.equals("1.0")){
                                        String star1_0 = prodmodel.getStar1_0();
                                        int totalstars5=Integer.parseInt(star1_0.replaceAll("[\\D]",""));
                                        int total5rate = totalstars5 + 1;
                                        String totalstars = Integer.toString(total5rate);
                                        HashMap<String, Object> rateuserMap = new HashMap<>();
                                        rateuserMap.put("star1_0", totalstars);
                                        prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                }
                                                else {
                                                    Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }
                                    else{
                                        return;
                                    }



                                    HashMap<String, Object> rateuserMap = new HashMap<>();
                                    rateuserMap.put("ratemaxusernum", total);
                                    prodrateref.child(pid).updateChildren(rateuserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                            }
                                            else {
                                                Toast.makeText(View_Product_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

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

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View")
                            .child(Prevalent.currentUser.getPhonenumber()).child("Products");

                    databaseReference2.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                OrdersModel ordersModel = snapshot.getValue(OrdersModel.class);
                                status.setText(ordersModel.getStatus2());
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

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
                .child(Prevalent.currentUser.getPhonenumber()).child("Products");

        databaseReference2.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    OrdersModel ordersModel = snapshot.getValue(OrdersModel.class);
                    status.setText(ordersModel.getStatus2());
                    String stat = ordersModel.getStatus2();

                    if (stat.equals("Delivered")){
                        recieved_btn.setVisibility(View.VISIBLE);



                        status.setTextColor(Color.GREEN);
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