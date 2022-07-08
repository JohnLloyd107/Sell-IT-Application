package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group5.sellit.Viewholder.Allprod_Viewholder;
import com.group5.sellit.Viewholder.OrderViewHolder;
import com.group5.sellit.Viewholder.Productviewholder;
import com.group5.sellit.model.OrdersModel;
import com.group5.sellit.model.Productmodel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

public class AddProduct_Activity extends AppCompatActivity {

    private RecyclerView allprod_recycle;
    private DatabaseReference productReference,categoryRef;
    RecyclerView.LayoutManager layoutManager, categlayout;
    FloatingActionButton add;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().hide();

        allprod_recycle = (RecyclerView) findViewById(R.id.allprod);
        allprod_recycle.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        allprod_recycle.setLayoutManager(layoutManager);

        add = (FloatingActionButton) findViewById(R.id.add);

        back = (ImageView) findViewById(R.id.back_ar_addpro);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProduct_Activity.this,AdminDasboard.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProduct_Activity.this,AddNewProduct.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference orderreference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Productmodel> options = new FirebaseRecyclerOptions.Builder<Productmodel>()
                .setQuery(orderreference, Productmodel.class).build();

        FirebaseRecyclerAdapter<Productmodel, Allprod_Viewholder> adapter = new FirebaseRecyclerAdapter<Productmodel, Allprod_Viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Allprod_Viewholder holder, int position, @NonNull Productmodel model) {

                holder.allproductnametxt.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.allimageView);
                holder.del_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
                        reference.child(model.getPid()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddProduct_Activity.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AddProduct_Activity.this, AddProduct_Activity.class));
                                        }

                                    }
                                });
                    }
                });
            }

            @NonNull
            @Override
            public Allprod_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allproducts_list, parent, false);
                Allprod_Viewholder allViewHolder = new Allprod_Viewholder(view);
                return allViewHolder;
            }
        };

        allprod_recycle.setAdapter(adapter);
        adapter.startListening();
    }
}