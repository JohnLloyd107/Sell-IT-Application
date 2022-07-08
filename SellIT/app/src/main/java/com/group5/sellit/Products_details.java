package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.Viewholder.CategoryViewHolder;
import com.group5.sellit.Viewholder.ColorViewHolder;
import com.group5.sellit.Viewholder.FeedViewHolder;
import com.group5.sellit.Viewholder.ImageViewHolder;
import com.group5.sellit.Viewholder.SizeViewHolder;
import com.group5.sellit.model.CategoryModel;
import com.group5.sellit.model.ColorModel;
import com.group5.sellit.model.FeedModel;
import com.group5.sellit.model.ImagesModel;
import com.group5.sellit.model.Productmodel;
import com.group5.sellit.model.RateModel;
import com.group5.sellit.model.SizeModel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Products_details extends AppCompatActivity {

    private TextView product_Name, product_Description, product_Price, tag1, tag2, tag3, rate_result,text3,text1,text2,text4;
    private ImageView product_Image, product_Image2, product_Image3, product_Image4, product_Image5;
    private RatingBar rate_bar;
    private Button add_cart_Button, rate_btn, buy_now_btn,add_cart_Button_info, buy_now_btn_info;
    private ElegantNumberButton quantity_num;
    private String pid,pid2,s_color = " ",c_category,s_size= " ", image;
    private RecyclerView feedback_recycle,color_recycler, size_recycler, image_recycler;
    RecyclerView.LayoutManager feedlayout,colorlayout, sizelayout,img_layout;
    private DatabaseReference feedRef,colorref,sizeref,productimageref;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private String id,dbtn;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);
        pid = getIntent().getStringExtra("pid");
        getSupportActionBar().hide();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions. DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        account = GoogleSignIn.getLastSignedInAccount(this);

        feedRef = FirebaseDatabase.getInstance().getReference().child("Product Feedback").child(pid);
        colorref = FirebaseDatabase.getInstance().getReference().child("Products").child(pid).child("color");
        sizeref = FirebaseDatabase.getInstance().getReference().child("Products").child(pid).child("size");
        productimageref = FirebaseDatabase.getInstance().getReference().child("Products").child(pid).child("images");

        product_Description = (TextView) findViewById(R.id.product_info_description);
        product_Name = (TextView) findViewById(R.id.product_info_name);
        product_Price = (TextView) findViewById(R.id.product_info_price);
        rate_bar = (RatingBar) findViewById(R.id.rate_bar);
        tag1 = (TextView) findViewById(R.id.tag1_d);
        tag2 = (TextView) findViewById(R.id.tag2_d);
        tag3 = (TextView) findViewById(R.id.tag3_d);

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);

//        product_Image = (ImageView) findViewById(R.id.product_info_img);
//        product_Image2 = (ImageView) findViewById(R.id.product_info_img2);
//        product_Image3 = (ImageView) findViewById(R.id.product_info_img3);
//        product_Image4 = (ImageView) findViewById(R.id.product_info_img4);
//        product_Image5 = (ImageView) findViewById(R.id.product_info_img5);
        quantity_num = (ElegantNumberButton) findViewById(R.id.item_quantity);

        feedback_recycle = (RecyclerView) findViewById( R.id.feedback_recycle);

        image_recycler = (RecyclerView) findViewById( R.id.imgview_recycle);

        add_cart_Button = (Button) findViewById(R.id.add_cart_btn);
        buy_now_btn = (Button) findViewById(R.id.buynow_btn);


        feedback_recycle.setHasFixedSize(true);
        feedlayout = new LinearLayoutManager(this);
        feedback_recycle.setLayoutManager(feedlayout);



        image_recycler.setHasFixedSize(true);
        img_layout = new LinearLayoutManager(this);
        image_recycler.setLayoutManager(img_layout);


        Product_Info(pid);

        buy_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbtn = "buy";
                if (c_category.equals("Clothes")){
                    ClothesMoreInfo();
                }
                else{
                    s_color = "";
                    s_size = " ";
                    BuyNow();

                }

            }
        });

        add_cart_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbtn = "add";

                if (c_category.equals("Clothes")){
                    ClothesMoreInfo();
                }
                else{
                    s_color = "";
                    s_size = " ";
                    addItemToCart();

                }


            }
        });




    }

    private void ClothesMoreInfo() {
        dialogbuilder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.moreclothes_info_list, null);

        color_recycler = (RecyclerView) popup.findViewById( R.id.clothescolor_recycle);
        size_recycler = (RecyclerView) popup.findViewById( R.id.clothessize_recycle);
        text3 = (TextView) popup.findViewById(R.id.text3);
        text4 = (TextView) popup.findViewById(R.id.text4);

        add_cart_Button_info = (Button) popup.findViewById(R.id.add_cart_btn_info);
        buy_now_btn_info = (Button) popup.findViewById(R.id.buynow_btn_info);

        color_recycler.setHasFixedSize(true);
        colorlayout = new LinearLayoutManager(this);
        color_recycler.setLayoutManager(colorlayout);

        size_recycler.setHasFixedSize(true);
        sizelayout = new LinearLayoutManager(this);
        size_recycler.setLayoutManager(sizelayout);

        dialogbuilder.setView(popup);
        dialog = dialogbuilder.create();
        dialog.show();

        FirebaseRecyclerOptions<ColorModel> coloroption = new FirebaseRecyclerOptions.Builder<ColorModel>()
                .setQuery(colorref,ColorModel.class).build();

        FirebaseRecyclerAdapter<ColorModel,ColorViewHolder> coloradapter = new FirebaseRecyclerAdapter<ColorModel, ColorViewHolder>(coloroption) {
            @Override
            protected void onBindViewHolder(@NonNull ColorViewHolder holder, int position, @NonNull ColorModel model) {
                holder.c_color.setText(model.getColor());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s_color = model.getColor();
                        text3.setText(s_color);
                        Toast.makeText(Products_details.this, "Color "+ s_color + " is selected" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list, parent, false);
                ColorViewHolder colorViewHolder = new ColorViewHolder(view);
                return colorViewHolder;
            }
        };

        color_recycler.setAdapter(coloradapter);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(this, 1 ,GridLayoutManager.HORIZONTAL,false);
        color_recycler.setLayoutManager(gridLayoutManager3);
        coloradapter.startListening();

        FirebaseRecyclerOptions<SizeModel> sizeoption = new FirebaseRecyclerOptions.Builder<SizeModel>()
                .setQuery(sizeref,SizeModel.class).build();

        FirebaseRecyclerAdapter<SizeModel, SizeViewHolder> sizeadapter = new FirebaseRecyclerAdapter<SizeModel, SizeViewHolder>(sizeoption) {
            @Override
            protected void onBindViewHolder(@NonNull SizeViewHolder holder, int position, @NonNull SizeModel model) {
                holder.c_size.setText(model.getSize());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s_size = model.getSize();
                        text4.setText(s_size);
                        Toast.makeText(Products_details.this, "Size "+ s_size + " is selected" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_list, parent, false);
                SizeViewHolder sizeViewHolder = new SizeViewHolder(view);
                return sizeViewHolder;
            }
        };

        size_recycler.setAdapter(sizeadapter);
        GridLayoutManager gridLayoutManager4 = new GridLayoutManager(this, 1 ,GridLayoutManager.HORIZONTAL,false);
        size_recycler.setLayoutManager(gridLayoutManager4);
        sizeadapter.startListening();




        if (dbtn.equals("add")){
            buy_now_btn_info.setVisibility(View.GONE);
        }else {
            add_cart_Button_info.setVisibility(View.GONE);

        }

        add_cart_Button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (c_category.equals("Clothes")){
                    if (s_color.equals(" ")){
                        Toast.makeText(Products_details.this, "Please select Color", Toast.LENGTH_SHORT).show();
                        text3.requestFocus();
                        return;

                    }
                    else if (s_size.equals(" ")){
                        Toast.makeText(Products_details.this, "Please select Size", Toast.LENGTH_SHORT).show();
                        text4.requestFocus();
                        return;
                    }
                    else{
                        addItemToCart();
                    }
                }

            }
        });

        buy_now_btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (c_category.equals("Clothes")){
                    if (s_color.equals(" ")){
                        Toast.makeText(Products_details.this, "Please select Color", Toast.LENGTH_SHORT).show();
                        text3.requestFocus();
                        return;

                    }
                    else if (s_size.equals(" ")){
                        Toast.makeText(Products_details.this, "Please select Size", Toast.LENGTH_SHORT).show();
                        text4.requestFocus();
                        return;
                    }
                    else{
                        BuyNow();
                    }
                }

            }
        });



    }

    private void BuyNow() {




        String scurrentTime, scurrentDate;

        Calendar getDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        scurrentDate = currentDate.format(getDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        scurrentTime = currentTime.format(getDate.getTime());

        final DatabaseReference cartref = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", pid);
        cartMap.put("pname", product_Name.getText().toString());
        cartMap.put("pdescription", product_Description.getText().toString());
        cartMap.put("pprice", product_Price.getText().toString());
        cartMap.put("time", scurrentTime);
        cartMap.put("date", scurrentDate);
        cartMap.put("quantity", quantity_num.getNumber());
        cartMap.put("status", "Pending");
        cartMap.put("status2", "Pending");
        cartMap.put("color", s_color);
        cartMap.put("size", s_size);
        cartMap.put("category", c_category);
        cartMap.put("image", image);


        cartref.child("User View").child(Prevalent.currentUser.getPhonenumber()).child("Products").child(pid)
                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    cartref.child("Admin View").child(Prevalent.currentUser.getPhonenumber()).child("Products").child(pid)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                DatabaseReference checkref = FirebaseDatabase.getInstance().getReference().child("Cart List");
                                checkref.child("User View").child(Prevalent.currentUser.getPhonenumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.exists()) {
                                            Intent intent = new Intent(Products_details.this, FinalPurchasedOrder.class);
                                            intent.putExtra("total_price", String.valueOf(product_Price));
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Products_details.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                        }
                    });

                }


            }
        });


    }

    private void RateSystem() {

        String rate = String.valueOf(rate_bar.getRating());
        Toast.makeText(Products_details.this, rate +" Stars", Toast.LENGTH_SHORT).show();

        final DatabaseReference userrateref = FirebaseDatabase.getInstance().getReference().child("Product Ratings");
        HashMap<String, Object> rateMap = new HashMap<>();
        rateMap.put("userrate", rate);

        userrateref.child(Prevalent.currentUser.getPhonenumber()).child(pid).updateChildren(rateMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(Products_details.this, "Your Rating has been save", Toast.LENGTH_SHORT).show();
                            rate_btn.setVisibility(View.INVISIBLE);
                            rate_bar.setVisibility(View.INVISIBLE);
                            rate_result.setText("Your Rating: "+rate);
                            rate_result.setVisibility(View.VISIBLE);

                            final DatabaseReference prodrateref = FirebaseDatabase.getInstance().getReference().child("Products");
                            prodrateref.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Productmodel prodmodel = snapshot.getValue(Productmodel.class);
                                    String maxuserrate = prodmodel.getRatemaxusernum();
                                    int i=Integer.parseInt(maxuserrate.replaceAll("[\\D]",""));
                                    Toast.makeText(Products_details.this, maxuserrate, Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(Products_details.this, "Error", Toast.LENGTH_SHORT).show();
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

    private void addItemToCart() {


        String scurrentTime, scurrentDate;

        Calendar getDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        scurrentDate = currentDate.format(getDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        scurrentTime = currentTime.format(getDate.getTime());

        final DatabaseReference cartref = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String ,Object> cartMap = new HashMap<>();
        cartMap.put("pid", pid);
        cartMap.put("pname", product_Name.getText().toString());
        cartMap.put("pdescription", product_Description.getText().toString());
        cartMap.put("pprice", product_Price.getText().toString());
        cartMap.put("time", scurrentTime);
        cartMap.put("date", scurrentDate);
        cartMap.put("quantity", quantity_num.getNumber());
        cartMap.put("status", "Pending");
        cartMap.put("status2", "Pending");
        cartMap.put("color", s_color);
        cartMap.put("size", s_size);
        cartMap.put("category", c_category);
        cartMap.put("image", image);


        cartref.child("User View").child(Prevalent.currentUser.getPhonenumber()).child("Products").child(pid)
                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    cartref.child("Admin View").child(Prevalent.currentUser.getPhonenumber()).child("Products").child(pid)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(Products_details.this, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Products_details.this, Home1Activity.class));
                                finish();

                            }

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
                    tag1.setText(productmodel.getTag1()+", ");
                    tag2.setText(productmodel.getTag2()+", ");
                    tag3.setText(productmodel.getTag3());
                    c_category = productmodel.getCategory();
                    image = productmodel.getImage();



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
        pid2 = getIntent().getStringExtra("pid");

        ImageViewer();

        FirebaseRecyclerOptions<FeedModel> feedoption = new FirebaseRecyclerOptions.Builder<FeedModel>()
                .setQuery(feedRef, FeedModel.class).build();

        FirebaseRecyclerAdapter<FeedModel, FeedViewHolder> adapter  = new FirebaseRecyclerAdapter<FeedModel, FeedViewHolder>(feedoption) {
            @Override
            protected void onBindViewHolder(@NonNull FeedViewHolder holder, int position, @NonNull FeedModel model) {

                holder.feed_username.setText(model.getName());
                holder.feed_date.setText(model.getDate());
                holder.feed_userfeed.setText(model.getUserfeed());
                holder.feed_rate.setText(model.getRate());

            }

            @NonNull
            @Override
            public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_list, parent, false);
                FeedViewHolder holder = new FeedViewHolder(view);
                return holder;
            }
        } ;

        feedback_recycle.setAdapter(adapter);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 1 ,GridLayoutManager.VERTICAL,false);
        feedback_recycle.setLayoutManager(gridLayoutManager2);
        adapter.startListening();





    }

    private void ImageViewer() {



        FirebaseRecyclerOptions<ImagesModel> option = new FirebaseRecyclerOptions.Builder<ImagesModel>()
                .setQuery(productimageref,ImagesModel.class).build();

        FirebaseRecyclerAdapter<ImagesModel, ImageViewHolder> adapter = new FirebaseRecyclerAdapter<ImagesModel, ImageViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull ImagesModel model) {
                Picasso.get().load(model.getImages()).into(holder.image_view);
            }

            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_view_list_grid, parent, false);
                ImageViewHolder imageViewHolder = new ImageViewHolder(view);
                return imageViewHolder;
            }
        };

        image_recycler.setAdapter(adapter);
        GridLayoutManager gridLayoutManager4 = new GridLayoutManager(this, 1 ,GridLayoutManager.HORIZONTAL,false);
        image_recycler.setLayoutManager(gridLayoutManager4);
        adapter.startListening();

    }


}