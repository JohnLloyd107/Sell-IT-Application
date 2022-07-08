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
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group5.sellit.Viewholder.CartViewHolder;
import com.group5.sellit.model.CartModel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

public class Complete_Activity extends AppCompatActivity {

    ImageView back;
    RecyclerView complete_recycleview;
    RecyclerView.LayoutManager layoutManager;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        getSupportActionBar().hide();

        back = (ImageView) findViewById(R.id.back_ar_complete);
        complete_recycleview = (RecyclerView) findViewById(R.id.complete_recycleview);
        complete_recycleview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        complete_recycleview.setLayoutManager(layoutManager);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Complete_Activity.this,UserProfile.class);
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
                        .orderByChild("status2").startAt("Complete").endAt("Complete"+"\uf8ff"),CartModel.class).build();

        FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {

                status = model.getStatus2();

                if (status.equals("Complete")){
                    holder.ProductNametxt.setText(model.getPname());
                    holder.ProductPricetxt.setText("Price = PHP " + model.getPprice());
                    holder.ProductQuatitytxt.setText("Quantity = " + model.getQuantity());
                    holder.ProductPurchaseDate.setText(model.getDate() + " " + model.getTime());
                    Picasso.get().load(model.getImage()).into(holder.ProductImage);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence charSequence[] = new CharSequence[]{
                                    "Remove"
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(Complete_Activity.this);
                            builder.setTitle("Options");
                            builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (i == 0){
                                        prodref.child("Admin View").child(Prevalent.currentUser.getPhonenumber()).child("Products").child(model.getPid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            Toast.makeText(Complete_Activity.this, "Product Removed", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else if (!task.isSuccessful()){
                                                            Toast.makeText(Complete_Activity.this, "Failed to remove Product", Toast.LENGTH_SHORT).show();
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
                else{
                    holder.card.setVisibility(View.INVISIBLE);


                }



            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_cart_product_item3, parent, false);
                CartViewHolder cartholder = new CartViewHolder(view);
                return cartholder;
            }
        };

        complete_recycleview.setAdapter(adapter);
        adapter.startListening();
    }
}