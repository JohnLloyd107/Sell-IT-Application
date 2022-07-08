package com.group5.sellit.Viewholder;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView OrderNametxt, Ordereduser_phonenumber, ordereduser_address, ordereduser_totalprice;
    public ImageView Productitem_img;
    private Itemclicklistener itemclicklistener;
    public Button ordersviewbtn;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        OrderNametxt = itemView.findViewById(R.id.orderitem_name);
        Ordereduser_phonenumber = itemView.findViewById(R.id.orderitem_phonenum);
        ordereduser_address = itemView.findViewById(R.id.orderitem_adddress);
        Productitem_img = itemView.findViewById(R.id.orderitem_img);
        ordersviewbtn = itemView.findViewById(R.id.orderbtn);




    }

    @Override
    public void onClick(View view) {

        itemclicklistener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemclicklistener(Itemclicklistener itemclicklistener) {
        this.itemclicklistener = itemclicklistener;
    }
}
