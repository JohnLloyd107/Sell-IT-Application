package com.group5.sellit.Viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;

public class SizeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView c_size;
    private Itemclicklistener itemclicklistener;

    public SizeViewHolder(@NonNull View itemView) {
        super(itemView);

        c_size = itemView.findViewById(R.id.c_size);

    }

    @Override
    public void onClick(View view) {

        itemclicklistener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemclicklistener(Itemclicklistener itemclicklistener) {
        this.itemclicklistener = itemclicklistener;
    }
}
