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

    public OrderViewHolder(View v) {
        super(v);

        textViewOrderBy = (TextView)v.findViewById(R.id.textViewOrderBy);
        textViewBrandName = (TextView)v.findViewById(R.id.textViewBrandName);
        textViewBrandPrice = (TextView)v.findViewById(R.id.textViewBrandPrice);
        textViewQuantity = (TextView)v.findViewById(R.id.textViewQuantity);
        textViewTotalPrice = (TextView)v.findViewById(R.id.textViewTotalPrice);
        textViewDeliveryDate = (TextView)v.findViewById(R.id.textViewDeliveryDate);
        textViewDeliveryTime = (TextView)v.findViewById(R.id.textViewDeliveryTime);
        textViewAddress = (TextView)v.findViewById(R.id.textViewAddress);
    }
}
