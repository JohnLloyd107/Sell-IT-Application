package com.group5.sellit.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView categoryname;
    public ImageView categimageView;
    public Itemclicklistener listner;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categimageView = (ImageView) itemView.findViewById(R.id.category_img);
        categoryname = (TextView) itemView.findViewById(R.id.category_name);
    }

    public void setItemclicklistener(Itemclicklistener listner){
        this.listner= listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view,getAdapterPosition(),false);


    }
}
