package com.group5.sellit.Viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;

public class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView c_color;
    private Itemclicklistener itemclicklistener;

    public ColorViewHolder(@NonNull View itemView) {
        super(itemView);

        c_color = itemView.findViewById(R.id.c_color);

    }

    @Override
    public void onClick(View view) {

        itemclicklistener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemclicklistener(Itemclicklistener itemclicklistener) {
        this.itemclicklistener = itemclicklistener;
    }
}
