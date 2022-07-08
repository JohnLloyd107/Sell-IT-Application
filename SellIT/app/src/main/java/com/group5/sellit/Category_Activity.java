package com.group5.sellit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group5.sellit.Viewholder.Productviewholder;
import com.group5.sellit.model.Productmodel;
import com.squareup.picasso.Picasso;

public class Category_Activity extends AppCompatActivity {

    private TextView categ_txt;
    private RecyclerView categ_recicleview;
    RecyclerView.LayoutManager categlayout;
    private DatabaseReference productReference;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().hide();

        pid = getIntent().getStringExtra("pid");

        categ_txt = (TextView) findViewById(R.id.categ_text);
        categ_recicleview = (RecyclerView) findViewById(R.id.categ_recycle);

        categ_recicleview.setHasFixedSize(true);
        categlayout = new LinearLayoutManager(this);
        categ_recicleview.setLayoutManager(categlayout);

        productReference = FirebaseDatabase.getInstance().getReference().child("Products");


    }


    @Override
    protected void onStart() {
        super.onStart();

        categ_txt.setText(pid);

        FirebaseRecyclerOptions<Productmodel> options = new FirebaseRecyclerOptions.Builder<Productmodel>()
                .setQuery(productReference.orderByChild("category").startAt(pid).endAt(pid), Productmodel.class).build();

        FirebaseRecyclerAdapter<Productmodel, Productviewholder> adapter =
                new FirebaseRecyclerAdapter<Productmodel, Productviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Productviewholder holder, int position, @NonNull Productmodel model) {
                        holder.productnametxt.setText(model.getName());
                        holder.productdestxt.setText(model.getDescription());
                        holder.productpricetxt.setText("Php " + model.getPrice());
                        holder.tagview1.setText(model.getTag1()+",");
                        holder.tagview2.setText(model.getTag2()+",");
                        holder.tagview3.setText(model.getTag3());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        String maxuserratenum = model.getRatemaxusernum();
                        int maxusernum=Integer.parseInt(maxuserratenum.replaceAll("[\\D]",""));

                        String total5rate = model.getStar5_0();
                        String total4_0rate = model.getStar4_5();
                        String total4_5rate = model.getStar4_0();
                        String total3_5rate = model.getStar3_5();
                        String total3_0rate = model.getStar3_0();
                        String total2_5rate = model.getStar2_5();
                        String total2_0rate = model.getStar2_0();
                        String total1_5rate = model.getStar1_5();
                        String total1_0rate = model.getStar1_0();

                        int totalstars5=Integer.parseInt(total5rate.replaceAll("[\\D]",""));
                        double totals5= totalstars5*5.0;

                        int totalstars4_5=Integer.parseInt(total4_5rate.replaceAll("[\\D]",""));
                        double totals4_5= totalstars4_5 * 4.5;

                        int totalstars4_0=Integer.parseInt(total4_0rate.replaceAll("[\\D]",""));
                        double totals4_0= totalstars4_0 * 4.0;

                        int totalstars3_5=Integer.parseInt(total3_5rate.replaceAll("[\\D]",""));
                        double totals3_5= totalstars3_5 * 3.5;

                        int totalstars3_0=Integer.parseInt(total3_0rate.replaceAll("[\\D]",""));
                        double totals3_0= totalstars3_0 * 3.0;

                        int totalstars2_5=Integer.parseInt(total2_5rate.replaceAll("[\\D]",""));
                        double totals2_5= totalstars2_5 * 2.5;

                        int totalstars2_0=Integer.parseInt(total2_0rate.replaceAll("[\\D]",""));
                        double totals2_0= totalstars2_0 * 2.0;

                        int totalstars1_5=Integer.parseInt(total1_5rate.replaceAll("[\\D]",""));
                        double totals1_5= totalstars1_5 * 1.5;

                        int totalstars1_0=Integer.parseInt(total1_0rate.replaceAll("[\\D]",""));
                        double totals1_0= totalstars1_0 * 1.0;

                        double totalrate = totals5 + totals4_5 + totals4_0 + totals3_5 + totals3_0 + totals2_5 + totals2_0 + totals1_5 + totals1_0;

                        double finaltotalrate = totalrate/maxusernum;

                        if (finaltotalrate > 5.0){
                            finaltotalrate = 5.0;
                        }
                        else if(finaltotalrate<0){
                            finaltotalrate = 0.0;
                        }

                        String finalstringtotalrate = Double.toString(finaltotalrate);

                        holder.rating.setText(finalstringtotalrate);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Category_Activity.this, Products_details.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_grid, parent, false);
                        Productviewholder holder = new Productviewholder(view);
                        return holder;
                    }
                };
        categ_recicleview.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2 ,GridLayoutManager.VERTICAL,false);
        categ_recicleview.setLayoutManager(gridLayoutManager);
        adapter.startListening();
    }
}