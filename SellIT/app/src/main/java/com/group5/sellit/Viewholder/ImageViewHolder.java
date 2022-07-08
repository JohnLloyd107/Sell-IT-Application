package com.group5.sellit.Viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;

public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView image_view;
    public Itemclicklistener listner;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        image_view = (ImageView) itemView.findViewById(R.id.img_view);
    }

    public void setItemclicklistener(Itemclicklistener listner){
        this.listner= listner;
    }

    @Override
    public void onClick(View v) {

        listner.onClick(v,getAdapterPosition(),false);

    }
}
