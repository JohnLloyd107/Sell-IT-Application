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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group5.sellit.Viewholder.CartViewHolder;
import com.group5.sellit.model.CartModel;
import com.squareup.picasso.Picasso;

public class List_of_UsersOrders_Activity extends AppCompatActivity {

    private RecyclerView UsersOrderedProductList;
    RecyclerView.LayoutManager layoutManager;
    private int totalPrice = 0 ;
    private TextView total_Price;

    private String uid,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users_orders);
        uid = getIntent().getStringExtra("uid");
        getSupportActionBar().hide();


        UsersOrderedProductList = findViewById(R.id.users_ordered_prod_list);
        UsersOrderedProductList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        total_Price = (TextView) findViewById(R.id.total_Price_txt);

        UsersOrderedProductList.setLayoutManager(layoutManager);





    }

    @Override
    protected void onStart() {
        super.onStart();

        id = getIntent().getStringExtra("id");

        final DatabaseReference prodref = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<CartModel> options = new FirebaseRecyclerOptions.Builder<CartModel>()
                .setQuery(prodref.child("Admin View").child(uid).child("Products"),CartModel.class).build();

        FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {

                holder.ProductNametxt.setText(model.getPname());
                holder.ProductPricetxt.setText("Price = PHP " + model.getPprice());
                holder.ProductQuatitytxt.setText("Quantity = " + model.getQuantity());
                holder.ProductPurchaseDate.setText("Date Purchased: " + model.getDate() + " " + model.getTime());

                Picasso.get().load(model.getImage()).into(holder.ProductImage);

                int productPrice = ((Integer.valueOf(model.getPprice()))) * Integer.valueOf(model.getQuantity());
                totalPrice = totalPrice + productPrice;

                total_Price.setText("Total Price = PHP " + String.valueOf(totalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(List_of_UsersOrders_Activity.this, Ordered_Product_details.class);
                        intent.putExtra("pid", model.getPid());
                        intent.putExtra("uid", uid);
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

        UsersOrderedProductList.setAdapter(adapter);
        adapter.startListening();



    }
}