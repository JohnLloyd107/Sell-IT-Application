package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group5.sellit.Viewholder.CartViewHolder;
import com.group5.sellit.model.CartModel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

public class Pending_Activity extends AppCompatActivity {

    ImageView back;
    RecyclerView pending_recycleview;
    RecyclerView.LayoutManager layoutManager;
    private int totalPrice = 0 ;
    private TextView total_Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        getSupportActionBar().hide();

        back = (ImageView) findViewById(R.id.back_ar);
        pending_recycleview = (RecyclerView) findViewById(R.id.recycle_pending);
        pending_recycleview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        total_Price = (TextView) findViewById(R.id.total_Price_txt);

        pending_recycleview.setLayoutManager(layoutManager);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pending_Activity.this,UserProfile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();



        final DatabaseReference prodref = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<CartModel> options = new FirebaseRecyclerOptions.Builder<CartModel>()
                .setQuery(prodref.child("Admin View").child(Prevalent.currentUser.getPhonenumber()).child("Products")
                        .orderByChild("status").startAt("Pending").endAt("Pending"+"\uf8ff"),CartModel.class).build();

        FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {

                holder.ProductNametxt.setText(model.getPname());
                holder.ProductPricetxt.setText("Price = PHP " + model.getPprice());
                holder.ProductQuatitytxt.setText("Quantity = " + model.getQuantity());
                holder.ProductPurchaseDate.setText(model.getDate() + " " + model.getTime());
                Picasso.get().load(model.getImage()).into(holder.ProductImage);

                int productPrice = ((Integer.valueOf(model.getPprice()))) * Integer.valueOf(model.getQuantity());
                totalPrice = totalPrice + productPrice;

                total_Price.setText("Total Price = PHP " + String.valueOf(totalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Pending_Activity.this, View_Product_Activity.class);
                        intent.putExtra("pid", model.getPid());
                        finish();
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_cart_product_item3, parent, false);
                CartViewHolder cartholder = new CartViewHolder(view);
                return cartholder;
            }
        };

        pending_recycleview.setAdapter(adapter);
        adapter.startListening();



    }
}