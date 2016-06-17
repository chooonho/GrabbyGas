package com.sjwoh.grabgas.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.logins.Supplier;

import java.util.List;
import java.util.Locale;

/**
 * Created by choonho on 15/6/2016.
 */
public class BrandAdapter extends RecyclerView.Adapter<BrandViewHolder> {
    private Supplier mSupplier;
    private List<Gas> mGases;
    private OnItemClickListener mItemClickListener;

    public BrandAdapter(Supplier supplier) {
        mSupplier = supplier;
        mGases = supplier.getGases();
    }

    @Override
    public void onBindViewHolder(BrandViewHolder brandViewHolder, int index) {
        if(mGases == null || mGases.size() == 0) {
            return;
        }

        Gas gas = mGases.get(index);

        brandViewHolder.textViewBrandName.setText(gas.getBrand().toUpperCase());
        brandViewHolder.textViewBrandPrice.setText("RM " + String.format(Locale.getDefault(), "%.2f", gas.getPrice()) + " ea");
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_brand, viewGroup, false);

        return new BrandViewHolder(itemView, mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mGases.size();
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public Gas getItem(int position) {
        return mGases.get(position);
    }
}
