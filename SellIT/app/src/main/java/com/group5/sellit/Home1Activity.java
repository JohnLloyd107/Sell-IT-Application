package com.group5.sellit;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.Viewholder.CategoryViewHolder;
import com.group5.sellit.Viewholder.Productviewholder;
import com.group5.sellit.databinding.ActivityHome1Binding;
import com.group5.sellit.model.CategoryModel;
import com.group5.sellit.model.Productmodel;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home1Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference productReference,categoryRef;
    private RecyclerView recyclerView, Category_View;
    RecyclerView.LayoutManager layoutManager, categlayout;
    private Button searchView;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHome1Binding binding;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private GoogleSignInAccount account;
    private  CircleImageView img_prof;
    private  View headerview;
    private TextView usernametext;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        productReference = FirebaseDatabase.getInstance().getReference().child("Products");
        categoryRef = FirebaseDatabase.getInstance().getReference().child("Category");
        searchView = (Button) findViewById(R.id.search_bar) ;

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions. DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);




        binding = ActivityHome1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarHome1.toolbar);
        binding.appBarHome1.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home1Activity.this, Cart.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);



//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();




//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home1);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        headerview = navigationView.getHeaderView(0);
        usernametext = headerview.findViewById(R.id.user_profile_name);
        img_prof = headerview.findViewById(R.id.user_profile_image);





        recyclerView = findViewById(R.id.recyclev);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Category_View = findViewById(R.id.categoryview);
        Category_View.setHasFixedSize(true);
        categlayout = new LinearLayoutManager(this);
        Category_View.setLayoutManager(categlayout);

        binding.appBarHome1.searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home1Activity.this, SearchActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        String key = Prevalent.currentUser.getKeyid();
        if (key!= null){
            account = GoogleSignIn.getLastSignedInAccount(this);
        }


        FirebaseRecyclerOptions<Productmodel> options = new FirebaseRecyclerOptions.Builder<Productmodel>()
                .setQuery(productReference, Productmodel.class).build();

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
                                Intent intent = new Intent(Home1Activity.this, Products_details.class);
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
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2 ,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();



        FirebaseRecyclerOptions<CategoryModel> categoryoption = new FirebaseRecyclerOptions.Builder<CategoryModel>()
                .setQuery(categoryRef, CategoryModel.class).build();

        FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder> adapter1 =
                new FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder>(categoryoption) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull CategoryModel model) {

                        holder.categoryname.setText(model.getCategoryname());
                        Picasso.get().load(model.getImage()).into(holder.categimageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Home1Activity.this, Category_Activity.class);
                                intent.putExtra("pid", model.getCategoryname());
                                startActivity(intent);

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
                        CategoryViewHolder holder = new CategoryViewHolder(view);
                        return holder;
                    }
                };

        Category_View.setAdapter(adapter1);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 1 ,GridLayoutManager.HORIZONTAL,false);
        Category_View.setLayoutManager(gridLayoutManager2);
        adapter1.startListening();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.child(Prevalent.currentUser.getKeyid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                usernametext.setText(users.getUsername());

                Picasso.get().load(users.getProf_img()).into(img_prof);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home1, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home1);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile){
            Intent intent = new Intent(Home1Activity.this, UserProfile.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_cart){
            Intent intent = new Intent(Home1Activity.this, Cart.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_orders){
            Intent intent = new Intent(Home1Activity.this, Pending_Activity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_category){

            dialogbuilder = new AlertDialog.Builder(this);
            final View popup = getLayoutInflater().inflate(R.layout.categories_popup, null);

            RecyclerView categ = (RecyclerView) popup.findViewById(R.id.categoryview1) ;
            categ.setHasFixedSize(true);
            categlayout = new LinearLayoutManager(this);
            categ.setLayoutManager(categlayout);

            dialogbuilder.setView(popup);
            dialog = dialogbuilder.create();
            dialog.show();

            FirebaseRecyclerOptions<CategoryModel> categoryoption = new FirebaseRecyclerOptions.Builder<CategoryModel>()
                    .setQuery(categoryRef, CategoryModel.class).build();

            FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder> adapter1 =
                    new FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder>(categoryoption) {
                        @Override
                        protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull CategoryModel model) {

                            holder.categoryname.setText(model.getCategoryname());
                            Picasso.get().load(model.getImage()).into(holder.categimageView);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Home1Activity.this, Category_Activity.class);
                                    intent.putExtra("pid", model.getCategoryname());
                                    startActivity(intent);

                                }
                            });



                        }

                        @NonNull
                        @Override
                        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
                            CategoryViewHolder holder = new CategoryViewHolder(view);
                            return holder;
                        }
                    };

            categ.setAdapter(adapter1);
            GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2 ,GridLayoutManager.VERTICAL,false);
            categ.setLayoutManager(gridLayoutManager2);
            adapter1.startListening();

        }

        else if (id == R.id.nav_logout){
            if (account!= null){
                Paper.book().destroy();
                Intent i = new Intent(Home1Activity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            else{
                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }
                });
            }


        }

        DrawerLayout drawer = (DrawerLayout)  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);



        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        if (account!= null){
            Toast.makeText(Home1Activity.this, "Account Log out", Toast.LENGTH_SHORT).show();
            Paper.book().destroy();
            startActivity(new Intent(Home1Activity.this, MainActivity.class));
            finish();
        }
        else{
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Paper.book().destroy();
                    Toast.makeText(Home1Activity.this, "Account Log out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Home1Activity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }
            });
        }
    }
}