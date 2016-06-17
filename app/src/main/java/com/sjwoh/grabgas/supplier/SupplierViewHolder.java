package com.sjwoh.grabgas.supplier;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.order.OnItemClickListener;

/**
 * Created by choonho on 15/6/2016.
 */
public class SupplierViewHolder extends RecyclerView.ViewHolder {

    protected TextView textViewSupplierName, textViewSupplierUsername,
            textViewSupplierDistance, textViewSupplierMobile, textViewBrands;
    private OnItemClickListener mItemClickListener;

    public SupplierViewHolder(View v, OnItemClickListener itemClickListener) {
        super(v);
        textViewSupplierName = (TextView)v.findViewById(R.id.textViewSupplierName);
        textViewSupplierUsername = (TextView)v.findViewById(R.id.textViewSupplierUsername);
        textViewSupplierDistance = (TextView)v.findViewById(R.id.textViewSupplierDistance);
        textViewSupplierMobile = (TextView)v.findViewById(R.id.textViewSupplierMobile);
        textViewBrands = (TextView)v.findViewById(R.id.textViewBrands);
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
