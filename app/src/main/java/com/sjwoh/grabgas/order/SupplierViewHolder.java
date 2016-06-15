package com.sjwoh.grabgas.order;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sjwoh.grabgas.R;

/**
 * Created by choonho on 15/6/2016.
 */
public class SupplierViewHolder extends RecyclerView.ViewHolder {

    protected TextView textViewSupplierName, textViewSupplierUsername,
            textViewSupplierDistance, textViewSupplierMobile, textViewBrands;

    public SupplierViewHolder(View v) {
        super(v);
        textViewSupplierName = (TextView)v.findViewById(R.id.textViewSupplierName);
        textViewSupplierUsername = (TextView)v.findViewById(R.id.textViewSupplierUsername);
        textViewSupplierDistance = (TextView)v.findViewById(R.id.textViewSupplierDistance);
        textViewSupplierMobile = (TextView)v.findViewById(R.id.textViewSupplierMobile);
        textViewBrands = (TextView)v.findViewById(R.id.textViewBrands);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), textViewSupplierName.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
