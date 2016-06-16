package com.sjwoh.grabgas.order;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.logins.Supplier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choonho on 16/6/2016.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    Supplier mSupplier;
    List<Order> mOrders;
    private DatabaseReference mDatabaseReference;

    public OrderAdapter(DatabaseReference databaseReference, Supplier supplier) {
        mDatabaseReference = databaseReference;
        mSupplier = supplier;
        fetchOrders();
    }

    @Override
    public void onBindViewHolder(OrderViewHolder orderViewHolder, int index) {
        if(mOrders == null || mOrders.size() == 0) {
            return;
        }

        Order order = mOrders.get(index);

        orderViewHolder.textViewOrderBy.setText(order.getOrderedBy());
        orderViewHolder.textViewBrandName.setText(order.getGas().getName().toUpperCase());
        orderViewHolder.textViewBrandPrice.setText("RM " + String.format("%.2f", order.getGas().getPrice()) + " ea");
        orderViewHolder.textViewQuantity.setText(Integer.toString(order.getQuantity()));
        orderViewHolder.textViewTotalPrice.setText("RM " + String.format("%.2f", (order.getQuantity() * order.getGas().getPrice())));
        orderViewHolder.textViewDeliveryDate.setText(order.getDeliveryDateText());
        orderViewHolder.textViewDeliveryTime.setText(order.getDeliveryTimeText());
        orderViewHolder.textViewAddress.setText(order.getAddress());
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, viewGroup, false);

        return new OrderViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public void fetchOrders() {
        mOrders = new ArrayList<>();

        final ValueEventListener orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.child("supplier").child(mSupplier.getUsername()).child("order").getChildren()) {
                    if(!childSnapshot.exists()) {
                        break;
                    }

                    if(childSnapshot.getValue().toString().equals("0")) {
                        Order order = new Order();
                        order.setOrderRef(childSnapshot.getKey());
                        order.setStatus(Integer.parseInt(childSnapshot.getValue().toString()));
                        mOrders.add(order);
                    }
                }

                for(Order order : mOrders) {
                    DataSnapshot childSnapshot = dataSnapshot.child("order").child(order.getOrderRef());
                    if(!childSnapshot.exists()) {
                        continue;
                    }

                    order.setAddress(childSnapshot.child("address").getValue().toString());
                    order.setDeliveryDateText(childSnapshot.child("deliveryDate").getValue().toString());
                    order.setDeliveryTimeText(childSnapshot.child("deliveryTime").getValue().toString());
                    order.setOrderedBy(childSnapshot.child("orderBy").getValue().toString());
                    order.setSuppliedBy(mSupplier.getUsername());

                    for(DataSnapshot grandChildSnapshot : childSnapshot.child("gasOrdered").getChildren()) {
                        if(!grandChildSnapshot.exists()) {
                            break;
                        }

                        Gas gas = new Gas();
                        gas.setName(grandChildSnapshot.getKey());
                        gas.setPrice(Double.parseDouble(grandChildSnapshot.child("price").getValue().toString()));

                        order.setGas(gas);
                        order.setQuantity(Integer.parseInt(grandChildSnapshot.child("quantity").getValue().toString()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addValueEventListener(orderListener);
    }
}
