package com.sjwoh.grabgas.order;

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
import com.sjwoh.grabgas.logins.Supplier;
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

        if(mUser instanceof Supplier) {
            orderViewHolder.textViewOrderBy.setText(order.getOrderedBy());
        }
        else {
            orderViewHolder.textViewOrderBy.setText(order.getSuppliedBy());
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

        return new OrderViewHolder(itemView);
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

                if(dataSnapshot.getValue().toString().equals(Integer.toString(Order.ORDER_PENDING))) {
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
                            order.setOrderedBy(dataSnapshot.child("orderBy").getValue().toString());
                            order.setSuppliedBy(mUser.getUsername());

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
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(!mKeys.contains(dataSnapshot.getKey())) {
                    return;
                }

                // OnChildChanged here means that the order is either cancelled or handled/declined,
                // whereby the order status = 1 or -1
                // That case, we will have to remove it from the view
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
        else {
            mDatabaseReference.child("customer").child(mUser.getUsername()).child("order").addChildEventListener(orderListener);
        }
    }
}
