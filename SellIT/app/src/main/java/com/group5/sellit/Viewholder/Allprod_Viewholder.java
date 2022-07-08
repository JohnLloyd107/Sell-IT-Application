package com.group5.sellit.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;

public class Allprod_Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView allproductnametxt, productdestxt, productpricetxt;
    public ImageView allimageView,del_img;
    public Itemclicklistener listner;

    public Allprod_Viewholder(@NonNull View itemView) {
        super(itemView);

        allimageView = (ImageView) itemView.findViewById(R.id.allproduct_list_img);
        del_img = (ImageView) itemView.findViewById(R.id.del_btn);
        allproductnametxt = (TextView) itemView.findViewById(R.id.allproduct_list_name);

    }

    public void setItemclicklistener(Itemclicklistener listner){
        this.listner= listner;
    }

    @Override
    public void onClick(View v) {

        listner.onClick(v,getAdapterPosition(),false);

    }
}
