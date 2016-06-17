package com.sjwoh.grabgas.supplier;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.order.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by choonho on 15/6/2016.
 */
public class SupplierAdapter extends RecyclerView.Adapter<SupplierViewHolder> {
    private final int MAX_SUPPLIER_RANGE = 5;
    private List<Supplier> mSuppliers;
    private LatLng mCustomerLatLng;
    private DatabaseReference mDatabaseReference;
    private OnItemClickListener mItemClickListener;

    public SupplierAdapter(DatabaseReference databaseReference, LatLng customerLatLng) {
        mCustomerLatLng = customerLatLng;
        mDatabaseReference = databaseReference;

        mSuppliers = new ArrayList<>();
        fetchSuppliersWithGases();
    }

    @Override
    public void onBindViewHolder(SupplierViewHolder supplierViewHolder, int index) {
        if(mSuppliers == null || mSuppliers.size() == 0) {
            return;
        }

        Supplier supplier = mSuppliers.get(index);

        LatLng endLatLng = new LatLng(supplier.getLocLatitude(), supplier.getLocLongitude());
        double distance = calculateDistance(mCustomerLatLng, endLatLng);

        String brands = "";
        List<Gas> gases = supplier.getGases();
        if(gases != null) {
            for(int i = 0; i < gases.size(); i++) {
                brands += gases.get(i).getBrand();

                if(i != (gases.size() - 1)) {
                    brands += ", ";
                }
            }
        }

        supplierViewHolder.textViewSupplierName.setText(supplier.getName());
        supplierViewHolder.textViewSupplierUsername.setText("@" + supplier.getUsername());
        supplierViewHolder.textViewSupplierMobile.setText(supplier.getMobilePhone());
        if(distance != -1) {
            supplierViewHolder.textViewSupplierDistance.setText("Approx. " + String.format(Locale.getDefault(), "%.4f", distance) + "km away");
        }
        else {
            supplierViewHolder.textViewSupplierDistance.setText("Approx. unknown");
        }
        supplierViewHolder.textViewBrands.setText(brands);
    }

    @Override
    public SupplierViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_supplier, viewGroup, false);

        return new SupplierViewHolder(itemView, mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mSuppliers.size();
    }

    private double calculateDistance(LatLng startLatLng, LatLng endLatLng) {
        if(startLatLng != null && endLatLng != null) {
            Location startLocation = new Location("");
            startLocation.setLatitude(startLatLng.latitude);
            startLocation.setLongitude(startLatLng.longitude);

            Location endLocation = new Location("");
            endLocation.setLatitude(endLatLng.latitude);
            endLocation.setLongitude(endLatLng.longitude);

            return (startLocation.distanceTo(endLocation) / 1000);
        }

        return -1;
    }

    private void fetchSuppliersWithGases() {
        final ValueEventListener supplierListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }

                DataSnapshot supplierGasSnapshot = dataSnapshot.child("supplier");
                if(!supplierGasSnapshot.exists()) {
                    return;
                }

                for(DataSnapshot childSnapshot: supplierGasSnapshot.getChildren()) {
                    Supplier supplier = new Supplier();
                    supplier.setUsername(childSnapshot.getKey());

                    mSuppliers.add(supplier);
                }

                for(Supplier supplier : mSuppliers) {
                    List<Gas> gases = new ArrayList<>();
                    for(DataSnapshot childSnapshot : dataSnapshot.child("supplier").child(supplier.getUsername()).child("gas").getChildren()) {

                        Gas gas = new Gas();

                        gas.setBrand(childSnapshot.getKey());
                        gas.setPrice(Double.parseDouble(childSnapshot.child("price").getValue().toString()));

                        gases.add(gas);
                    }

                    supplier.setGases(gases);
                }

                DataSnapshot supplierInfoSnapshot = dataSnapshot.child("user");
                if(!supplierInfoSnapshot.exists()) {
                    return;
                }

                for(Supplier supplier : mSuppliers) {
                    supplier.setName(supplierInfoSnapshot.child(supplier.getUsername()).child("name").getValue().toString());
                    supplier.setMobilePhone(supplierInfoSnapshot.child(supplier.getUsername()).child("mobilePhone").getValue().toString());
                    supplier.setLocLatitude(Double.parseDouble(supplierInfoSnapshot.child(supplier.getUsername()).child("locLatitude").getValue().toString()));
                    supplier.setLocLongitude(Double.parseDouble(supplierInfoSnapshot.child(supplier.getUsername()).child("locLongitude").getValue().toString()));

                    LatLng endLatLng = new LatLng(supplier.getLocLatitude(), supplier.getLocLongitude());
                    double distance = calculateDistance(mCustomerLatLng, endLatLng);

                    if(distance > MAX_SUPPLIER_RANGE) {
                        mSuppliers.remove(supplier);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        };

        mDatabaseReference.addValueEventListener(supplierListener);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public Supplier getItem(int position) {
        return mSuppliers.get(position);
    }
}
