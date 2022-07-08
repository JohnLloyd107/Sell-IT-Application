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
import com.group5.sellit.Viewholder.OrderViewHolder;
import com.group5.sellit.model.CartModel;
import com.group5.sellit.model.OrdersModel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

public class Orders_Activity extends AppCompatActivity {

    private RecyclerView ordered_item_recycle;
    private RecyclerView.LayoutManager layoutManager;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getSupportActionBar().hide();

        ordered_item_recycle = (RecyclerView) findViewById(R.id.order_recycle);
        ordered_item_recycle.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        ordered_item_recycle.setLayoutManager(layoutManager);

        back = (ImageView) findViewById(R.id.back_ar_order);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Orders_Activity.this,AdminDasboard.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference orderreference = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<OrdersModel> options = new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(orderreference,OrdersModel.class).build();


       FirebaseRecyclerAdapter<OrdersModel, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<OrdersModel, OrderViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull OrdersModel model) {

               holder.OrderNametxt.setText(model.getCustomer_name());
               holder.Ordereduser_phonenumber.setText(model.getCustomer_number());
               holder.ordereduser_address.setText(model.getCustomer_address());
               Picasso.get().load(model.getImg_prof()).into(holder.Productitem_img);
               holder.ordersviewbtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       String uID = holder.Ordereduser_phonenumber.getText().toString();
                       String id = model.getId();


                       Intent intent = new Intent(Orders_Activity.this, List_of_UsersOrders_Activity.class);
                       intent.putExtra("uid", uID);
                       startActivity(intent);
                   }
               });

           }

           @NonNull
           @Override
           public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list, parent, false);
               OrderViewHolder orderViewHolder = new OrderViewHolder(view);
               return orderViewHolder;
           }
       };

        ordered_item_recycle.setAdapter(adapter);
        adapter.startListening();
    }
}