package com.group5.sellit.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView ProductNametxt, ProductPricetxt, ProductQuatitytxt,ProductPurchaseDate,ProductDate;
    private Itemclicklistener itemclicklistener;
    public ImageView ProductImage;
    public CardView card;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        ProductNametxt = itemView.findViewById(R.id.p_name);
        ProductPricetxt = itemView.findViewById(R.id.p_price);
        ProductQuatitytxt = itemView.findViewById(R.id.p_quantity);
        ProductPurchaseDate = itemView.findViewById(R.id.p_date);
        ProductImage = itemView.findViewById(R.id.p_image);
        card = (CardView) itemView.findViewById(R.id.card);

    }

    @Override
    public void onClick(View view) {

        itemclicklistener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemclicklistener(Itemclicklistener itemclicklistener) {
        this.itemclicklistener = itemclicklistener;
    }
}
