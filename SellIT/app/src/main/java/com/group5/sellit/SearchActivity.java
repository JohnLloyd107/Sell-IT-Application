package com.group5.sellit;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group5.sellit.Viewholder.ProductAdaptor;
import com.group5.sellit.Viewholder.Productviewholder;
import com.group5.sellit.model.Productmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchlistrecycle;
    private EditText searchbar;
    private Button searchbutton,categ_button,name_button;
    private String inputtext,searchfil;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Productmodel> productList;
    FirebaseRecyclerAdapter<Productmodel, Productviewholder> adapter;
    ProductAdaptor productAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        searchlistrecycle = (RecyclerView) findViewById(R.id.search_recycle);
        searchbar = (EditText) findViewById(R.id.search_edittext);
        searchbutton = (Button) findViewById(R.id.searchbar_button);
        categ_button = (Button) findViewById(R.id.categ_button);
        name_button = (Button) findViewById(R.id.name_button);

        searchlistrecycle.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        searchlistrecycle.setLayoutManager(layoutManager);

        searchfil = "name";

//        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String q) {
//                searchprocess(q);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String q) {
//
//                searchprocess(q);
//                return false;
//            }
//        });



//        searchbar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                filter(s.toString());
//
//            }
//        });

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputtext = searchbar.getText().toString();



                onStart();

            }
        });

        categ_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchfil = "category";


                Toast.makeText(SearchActivity.this, "Search filter set to  Products Category", Toast.LENGTH_SHORT).show();
                onStart();

            }
        });

        name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchfil = "name";

                Toast.makeText(SearchActivity.this, "Search filter set to  Products Name", Toast.LENGTH_SHORT).show();
                onStart();

            }
        });


    }

//    private void searchprocess(String q) {
//        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");
//        FirebaseRecyclerOptions<Productmodel> options = new FirebaseRecyclerOptions.Builder<Productmodel>()
//                .setQuery(productReference.orderByChild(searchfil).startAt(q).endAt(q+"\uf8ff"),Productmodel.class).build();
//
//        FirebaseRecyclerAdapter<Productmodel, Productviewholder> adapter =
//                new FirebaseRecyclerAdapter<Productmodel, Productviewholder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull Productviewholder holder, int position, @NonNull Productmodel model) {
//                        holder.productnametxt.setText(model.getName());
//                        holder.productdestxt.setText(model.getDescription());
//                        holder.productpricetxt.setText("Php " + model.getPrice());
//                        holder.tagview1.setText(model.getTag1()+",");
//                        holder.tagview2.setText(model.getTag2()+",");
//                        holder.tagview3.setText(model.getTag3());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);
//
//                        String maxuserratenum = model.getRatemaxusernum();
//                        int maxusernum=Integer.parseInt(maxuserratenum.replaceAll("[\\D]",""));
//
//                        String total5rate = model.getStar5_0();
//                        String total4_0rate = model.getStar4_5();
//                        String total4_5rate = model.getStar4_0();
//                        String total3_5rate = model.getStar3_5();
//                        String total3_0rate = model.getStar3_0();
//                        String total2_5rate = model.getStar2_5();
//                        String total2_0rate = model.getStar2_0();
//                        String total1_5rate = model.getStar1_5();
//                        String total1_0rate = model.getStar1_0();
//
//                        int totalstars5=Integer.parseInt(total5rate.replaceAll("[\\D]",""));
//                        double totals5= totalstars5*5.0;
//
//                        int totalstars4_5=Integer.parseInt(total4_5rate.replaceAll("[\\D]",""));
//                        double totals4_5= totalstars4_5 * 4.5;
//
//                        int totalstars4_0=Integer.parseInt(total4_0rate.replaceAll("[\\D]",""));
//                        double totals4_0= totalstars4_0 * 4.0;
//
//                        int totalstars3_5=Integer.parseInt(total3_5rate.replaceAll("[\\D]",""));
//                        double totals3_5= totalstars3_5 * 3.5;
//
//                        int totalstars3_0=Integer.parseInt(total3_0rate.replaceAll("[\\D]",""));
//                        double totals3_0= totalstars3_0 * 3.0;
//
//                        int totalstars2_5=Integer.parseInt(total2_5rate.replaceAll("[\\D]",""));
//                        double totals2_5= totalstars2_5 * 2.5;
//
//                        int totalstars2_0=Integer.parseInt(total2_0rate.replaceAll("[\\D]",""));
//                        double totals2_0= totalstars2_0 * 2.0;
//
//                        int totalstars1_5=Integer.parseInt(total1_5rate.replaceAll("[\\D]",""));
//                        double totals1_5= totalstars1_5 * 1.5;
//
//                        int totalstars1_0=Integer.parseInt(total1_0rate.replaceAll("[\\D]",""));
//                        double totals1_0= totalstars1_0 * 1.0;
//
//                        double totalrate = totals5 + totals4_5 + totals4_0 + totals3_5 + totals3_0 + totals2_5 + totals2_0 + totals1_5 + totals1_0;
//
//                        double finaltotalrate = totalrate/maxusernum;
//
//                        if (finaltotalrate > 5.0){
//                            finaltotalrate = 5.0;
//                        }
//                        else if(finaltotalrate<0){
//                            finaltotalrate = 0.0;
//                        }
//
//                        String finalstringtotalrate = Double.toString(finaltotalrate);
//
//                        holder.rating.setText(finalstringtotalrate);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(SearchActivity.this, Products_details.class);
//                                intent.putExtra("pid", model.getPid());
//                                startActivity(intent);
//
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public Productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_single_item, parent, false);
//                        Productviewholder holder = new Productviewholder(view);
//                        return holder;
//                    }
//                };
//
//        searchlistrecycle.setAdapter(adapter);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1 ,GridLayoutManager.VERTICAL,false);
//        searchlistrecycle.setLayoutManager(gridLayoutManager);
//        adapter.startListening();
//
//    }

//    private void filter(String text) {
//        ArrayList<Productmodel> filteredList = new ArrayList<>();
//
//        for (Productmodel item :productList){
//            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(item);
//            }
//
//
//
//
//        }
//
//    }



    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Productmodel> options = new FirebaseRecyclerOptions.Builder<Productmodel>()
                .setQuery(productReference.orderByChild(searchfil).startAt(inputtext).endAt(inputtext+"\uf8ff"),Productmodel.class).build();

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
                                Intent intent = new Intent(SearchActivity.this, Products_details.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_single_item, parent, false);
                        Productviewholder holder = new Productviewholder(view);
                        return holder;
                    }
                };



        searchlistrecycle.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1 ,GridLayoutManager.VERTICAL,false);
        searchlistrecycle.setLayoutManager(gridLayoutManager);
        adapter.startListening();
    }


}