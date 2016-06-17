package com.sjwoh.grabgas.order;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sjwoh.grabgas.R;

/**
 * Created by choonho on 16/6/2016.
 */
public class OrderViewHolder extends RecyclerView.ViewHolder {
    protected TextView textViewOrderBy, textViewBrandName, textViewBrandPrice, textViewQuantity, textViewTotalPrice,
                        textViewDeliveryDate, textViewDeliveryTime, textViewAddress;
    private OnItemClickListener mItemClickListener;

    public OrderViewHolder(View v, OnItemClickListener itemClickListener) {
        super(v);

        textViewOrderBy = (TextView)v.findViewById(R.id.textViewOrderBy);
        textViewBrandName = (TextView)v.findViewById(R.id.textViewBrandName);
        textViewBrandPrice = (TextView)v.findViewById(R.id.textViewBrandPrice);
        textViewQuantity = (TextView)v.findViewById(R.id.textViewQuantity);
        textViewTotalPrice = (TextView)v.findViewById(R.id.textViewTotalPrice);
        textViewDeliveryDate = (TextView)v.findViewById(R.id.textViewDeliveryDate);
        textViewDeliveryTime = (TextView)v.findViewById(R.id.textViewDeliveryTime);
        textViewAddress = (TextView)v.findViewById(R.id.textViewAddress);
        mItemClickListener = itemClickListener;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, getAdapterPosition());
                }
            }
        });
    }
}
