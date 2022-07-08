package com.group5.sellit.Viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.sellit.Itemclicklistener;
import com.group5.sellit.R;

public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView feed_username, feed_date, feed_userfeed, feed_rate;
    public Itemclicklistener listner;

    public FeedViewHolder(@NonNull View itemView) {
        super(itemView);

        feed_username = (TextView) itemView.findViewById(R.id.feed_username);
        feed_date = (TextView) itemView.findViewById(R.id.feed_date);
        feed_userfeed = (TextView) itemView.findViewById(R.id.feed_userfeed);
        feed_rate = (TextView) itemView.findViewById(R.id.feed_rate);
    }

    public void setItemclicklistener(Itemclicklistener listner){
        this.listner= listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view,getAdapterPosition(),false);

    }
}
