package com.sjwoh.grabgas.order;

import android.graphics.Color;
import android.graphics.Interpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.customer.Customer;
import com.sjwoh.grabgas.supplier.Gas;
import com.sjwoh.grabgas.supplier.Supplier;
import com.sjwoh.grabgas.logins.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choonho on 16/6/2016.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    User mUser;
    List<Order> mOrders;
    List<String> mKeys;
    private DatabaseReference mDatabaseReference;
    private OnItemClickListener mItemClickListener;

    public OrderAdapter(DatabaseReference databaseReference, User user) {
        mDatabaseReference = databaseReference;
        mUser = user;
        fetchOrders();
    }

    @Override
    public void onBindViewHolder(OrderViewHolder orderViewHolder, int index) {
        if(mOrders == null || mOrders.size() == 0) {
            return;
        }

        Order order = mOrders.get(index);

        if(order.getStatus() == Order.ORDER_DONE) {
            orderViewHolder.textViewOrderBy.setBackgroundColor(Color.parseColor("#545AA7"));

            if(mUser instanceof Supplier) {
                orderViewHolder.textViewOrderBy.setText(order.getOrderedBy() + " (Processed)");
            }
            else {
                orderViewHolder.textViewOrderBy.setText(order.getSuppliedBy() + " (Processed)");
            }
        }
        else if(order.getStatus() == Order.ORDER_EXPIRED) {
            orderViewHolder.textViewOrderBy.setBackgroundColor(Color.parseColor("#696969"));
            orderViewHolder.textViewOrderBy.setText(order.getSuppliedBy() + " (Cancelled / Declined)");
        }
        else {
            orderViewHolder.textViewOrderBy.setBackgroundColor(Color.parseColor("#c62828"));

            if(mUser instanceof Supplier) {
                orderViewHolder.textViewOrderBy.setText(order.getOrderedBy());
            }
            else {
                orderViewHolder.textViewOrderBy.setText(order.getSuppliedBy());
            }
        }

        orderViewHolder.textViewBrandName.setText(order.getGas().getBrand().toUpperCase());
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

        return new OrderViewHolder(itemView, mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public void fetchOrders() {
        mOrders = new ArrayList<>();
        mKeys = new ArrayList<>();

        final ChildEventListener orderListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(mKeys.contains(dataSnapshot.getKey())) {
                    return;
                }

                if(mUser instanceof Supplier) {
                    if(dataSnapshot.getValue().toString().equals(Integer.toString(Order.ORDER_EXPIRED))) {
                        return;
                    }
                }

                final String orderRef = dataSnapshot.getKey();
                final int status = Integer.parseInt(dataSnapshot.getValue().toString());

                mDatabaseReference.child("order").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Order order = new Order();
                        order.setOrderRef(orderRef);
                        order.setStatus(status);
                        order.setAddress(dataSnapshot.child("address").getValue().toString());
                        order.setDeliveryDateText(dataSnapshot.child("deliveryDate").getValue().toString());
                        order.setDeliveryTimeText(dataSnapshot.child("deliveryTime").getValue().toString());
                        order.setOrderedBy(dataSnapshot.child("orderedBy").getValue().toString());
                        order.setSuppliedBy(dataSnapshot.child("suppliedBy").getValue().toString());

                        Gas gas = new Gas();
                        gas.setBrand(dataSnapshot.child("gasOrdered").child("brand").getValue().toString());
                        gas.setPrice(Double.parseDouble(dataSnapshot.child("gasOrdered").child("price").getValue().toString()));
                        order.setGas(gas);
                        order.setQuantity(Integer.parseInt(dataSnapshot.child("gasOrdered").child("quantity").getValue().toString()));

                        mOrders.add(mOrders.size(), order);
                        mKeys.add(mKeys.size(), dataSnapshot.getKey());

                        notifyItemInserted(mOrders.size() - 1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(!mKeys.contains(dataSnapshot.getKey())) {
                    return;
                }

                final String orderRef = dataSnapshot.getKey();
                final int status = Integer.parseInt(dataSnapshot.getValue().toString());

                // For a supplied, OnChildChanged here means that the order is either cancelled
                // or declined, whereby the order status -1 labelled as expired
                // That case, we will have to remove it from the view
                if(mUser instanceof Supplier) {
                    if(status == Order.ORDER_EXPIRED) {
                        int removeIndex = mKeys.indexOf(orderRef);

                        mOrders.remove(removeIndex);
                        mKeys.remove(removeIndex);

                        notifyItemRemoved(removeIndex);

                        return;
                    }
                }

                mDatabaseReference.child("order").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Order order = new Order();
                        order.setOrderRef(orderRef);
                        order.setStatus(status);
                        order.setAddress(dataSnapshot.child("address").getValue().toString());
                        order.setDeliveryDateText(dataSnapshot.child("deliveryDate").getValue().toString());
                        order.setDeliveryTimeText(dataSnapshot.child("deliveryTime").getValue().toString());
                        order.setOrderedBy(dataSnapshot.child("orderedBy").getValue().toString());
                        order.setSuppliedBy(dataSnapshot.child("suppliedBy").getValue().toString());

                        Gas gas = new Gas();
                        gas.setBrand(dataSnapshot.child("gasOrdered").child("brand").getValue().toString());
                        gas.setPrice(Double.parseDouble(dataSnapshot.child("gasOrdered").child("price").getValue().toString()));
                        order.setGas(gas);
                        order.setQuantity(Integer.parseInt(dataSnapshot.child("gasOrdered").child("quantity").getValue().toString()));

                        int changedIndex = mKeys.indexOf(orderRef);
                        mOrders.set(changedIndex, order);

                        notifyItemChanged(changedIndex);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(!mKeys.contains(dataSnapshot.getKey())) {
                    return;
                }

                String key = dataSnapshot.getKey();
                int removeIndex = mKeys.indexOf(key);

                mOrders.remove(removeIndex);
                mKeys.remove(removeIndex);

                notifyItemRemoved(removeIndex);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if(!mKeys.contains(dataSnapshot.getKey())) {
                    return;
                }

                String key = dataSnapshot.getKey();
                int removeIndex = mKeys.indexOf(key);

                mOrders.remove(removeIndex);
                mKeys.remove(removeIndex);

                notifyItemRemoved(removeIndex);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if(mUser instanceof Supplier) {
            mDatabaseReference.child("supplier").child(mUser.getUsername()).child("order").addChildEventListener(orderListener);
        }
        else if(mUser instanceof Customer) {
            mDatabaseReference.child("customer").child(mUser.getUsername()).child("order").addChildEventListener(orderListener);
        }
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public Order getItem(int position) {
        return mOrders.get(position);
    }
}
