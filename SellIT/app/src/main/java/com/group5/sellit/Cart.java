package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.Viewholder.CartViewHolder;
import com.group5.sellit.Viewholder.Productviewholder;
import com.group5.sellit.model.CartModel;
import com.group5.sellit.model.Productmodel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

public class Cart extends AppCompatActivity {

    private RecyclerView added_item_recycle;
    private RecyclerView.LayoutManager layoutManager;
    private TextView total_P;
    private Button purchase_btn;
    private int totalPrice = 0 ;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();

        total_P = (TextView) findViewById(R.id.total_p);
        purchase_btn = (Button) findViewById(R.id.purchase_button);
        added_item_recycle = (RecyclerView)  findViewById(R.id.added_item);
        added_item_recycle.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        added_item_recycle.setLayoutManager(layoutManager);




        purchase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference checkref = FirebaseDatabase.getInstance().getReference().child("Cart List");
                checkref.child("User View").child(Prevalent.currentUser.getPhonenumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            Intent intent = new Intent(Cart.this, FinalPurchasedOrder.class);
                            intent.putExtra("total_price",String.valueOf(totalPrice));
                            intent.putExtra("id",id);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(Cart.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartreference = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<CartModel> options = new FirebaseRecyclerOptions.Builder<CartModel>()
                .setQuery(cartreference.child("User View").child(Prevalent.currentUser.getPhonenumber()).child("Products"),CartModel.class).build();


        FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {


                        holder.ProductNametxt.setText(model.getPname());
                        holder.ProductPricetxt.setText("Price = PHP " + model.getPprice());
                        holder.ProductQuatitytxt.setText("Quantity = " + model.getQuantity());
                        holder.ProductPurchaseDate.setText(model.getDate() + " " + model.getTime());
                        Picasso.get().load(model.getImage()).into(holder.ProductImage);
                        id = model.getPid();

                        int productPrice = ((Integer.valueOf(model.getPprice()))) * Integer.valueOf(model.getQuantity());
                        totalPrice = totalPrice + productPrice;

                        total_P.setText("Total Price = PHP " + String.valueOf(totalPrice));



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence charSequence[] = new CharSequence[]{
                                        "Edit",
                                        "Remove"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                                builder.setTitle("Cart Options");
                                builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (i == 0){
                                            cartreference.child("User View").child(Prevalent.currentUser.getPhonenumber()).child("Products").child(model.getPid()).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Intent intent = new Intent(Cart.this, Products_details.class);
                                                                intent.putExtra("pid", model.getPid());
                                                                startActivity(intent);
                                                            }

                                                        }
                                                    });
                                        }

                                        if (i == 1){
                                            cartreference.child("User View").child(Prevalent.currentUser.getPhonenumber()).child("Products").child(model.getPid()).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                cartreference.child("Admin View").child(Prevalent.currentUser.getPhonenumber()).child("Products").child(model.getPid()).removeValue();
                                                                int productPrice = ((Integer.valueOf(model.getPprice()))) * Integer.valueOf(model.getQuantity());
                                                                totalPrice = totalPrice - productPrice;


                                                                total_P.setText("Total Price = PHP " + String.valueOf(totalPrice));
                                                                Toast.makeText(Cart.this, "Product Removed", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else if (!task.isSuccessful()){
                                                                Toast.makeText(Cart.this, "Failed to remove Product", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.show();
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
        added_item_recycle.setAdapter(adapter);
        adapter.startListening();


    }
}