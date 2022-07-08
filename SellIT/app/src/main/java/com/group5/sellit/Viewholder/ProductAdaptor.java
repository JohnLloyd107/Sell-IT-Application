package com.group5.sellit.Viewholder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.group5.sellit.Category_Activity;
import com.group5.sellit.Home1Activity;
import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.Products_details;
import com.group5.sellit.R;
import com.group5.sellit.SearchActivity;
import com.group5.sellit.model.Productmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdaptor extends FirebaseRecyclerAdapter <Productmodel, ProductAdaptor.newViewHolder> {
    private ArrayList<Productmodel> productList;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductAdaptor(@NonNull FirebaseRecyclerOptions<Productmodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull newViewHolder holder, int position, @NonNull Productmodel model) {

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




    }

    @NonNull
    @Override
    public newViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_single_item, parent, false);
        newViewHolder holder = new newViewHolder(view);

        return holder;
    }

    public class newViewHolder extends RecyclerView.ViewHolder {

        public TextView productnametxt, productdestxt, productpricetxt, tagview1, tagview2, tagview3,rating;
        public ImageView imageView, imageView2, imageView3, imageView4, imageView5;
        public Itemclicklistener listner;

        public newViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(com.group5.sellit.R.id.productitem_img);
            productnametxt = (TextView) itemView.findViewById(com.group5.sellit.R.id.productitem_name);
            productdestxt = (TextView) itemView.findViewById(com.group5.sellit.R.id.productitem_description);
            productpricetxt = (TextView) itemView.findViewById(com.group5.sellit.R.id.productitem_price_data);
            tagview1 = (TextView) itemView.findViewById(com.group5.sellit.R.id.tags1_data);
            tagview2 = (TextView) itemView.findViewById(com.group5.sellit.R.id.tags2_data);
            tagview3 = (TextView) itemView.findViewById(com.group5.sellit.R.id.tags3_data);
            rating = (TextView) itemView.findViewById(com.group5.sellit.R.id.rating);
        }
    }



    public void filterList(ArrayList<Productmodel> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
    }
}
