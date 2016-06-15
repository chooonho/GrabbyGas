package com.sjwoh.grabgas.order;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sjwoh.grabgas.R;

/**
 * Created by choonho on 15/6/2016.
 */
public class BrandViewHolder extends RecyclerView.ViewHolder {
    protected TextView textViewBrandName, textViewBrandPrice;
    private OnItemClickListener mItemClickListener;

    public BrandViewHolder(View v, OnItemClickListener itemClickListener) {
        super(v);

        textViewBrandName = (TextView)v.findViewById(R.id.textViewBrandName);
        textViewBrandPrice = (TextView)v.findViewById(R.id.textViewBrandPrice);
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
