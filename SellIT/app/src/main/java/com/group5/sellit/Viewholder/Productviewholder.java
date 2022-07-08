package com.group5.sellit.Viewholder;

import static android.os.Build.VERSION_CODES.R;

import android.media.Image;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;
import com.group5.sellit.model.Productmodel;

import java.util.ArrayList;
import java.util.List;

public class Productviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView productnametxt, productdestxt, productpricetxt, tagview1, tagview2, tagview3,rating;
    public ImageView imageView, imageView2, imageView3, imageView4, imageView5;
    public Itemclicklistener listner;

    public Productviewholder(@NonNull View itemView) {
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

    public void setItemclicklistener(Itemclicklistener listner){
        this.listner= listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view,getAdapterPosition(),false);

    }



}
