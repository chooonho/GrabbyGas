package com.sjwoh.grabgas.customer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.order.Order;

public class ActivityViewOrderDetailsCustomer extends AppCompatActivity {

    private TextView textViewCustomerUsername, textViewSupplierUsername, textViewAddress,
            textViewBrandName, textViewQuantity, textViewTotalPrice, textViewSinglePrice,
            textViewDeliveryDate, textViewDeliveryTime;
    private Button buttonCancel;
    private DatabaseReference mDatabaseReference;
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewCustomerUsername = (TextView)findViewById(R.id.textViewCustomerUsername);
        textViewSupplierUsername = (TextView)findViewById(R.id.textViewSupplierUsername);
        textViewAddress = (TextView)findViewById(R.id.textViewAddress);
        textViewBrandName = (TextView)findViewById(R.id.textViewBrandName);
        textViewQuantity = (TextView)findViewById(R.id.textViewQuantity);
        textViewTotalPrice = (TextView)findViewById(R.id.textViewTotalPrice);
        textViewSinglePrice = (TextView)findViewById(R.id.textViewSinglePrice);
        textViewDeliveryDate = (TextView)findViewById(R.id.textViewDeliveryDate);
        textViewDeliveryTime = (TextView)findViewById(R.id.textViewDeliveryTime);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);

        mOrder = getIntent().getParcelableExtra("ORDER_OBJECT");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        initTextViewValues();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ActivityViewOrderDetailsCustomer.this)
                        .setTitle("Cancel order")
                        .setMessage("Are you sure you want to cancel this order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrder();
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .setIcon(R.drawable.ic_warning_black)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initTextViewValues() {
        textViewCustomerUsername.setText(mOrder.getOrderedBy());
        textViewSupplierUsername.setText(mOrder.getSuppliedBy());
        textViewAddress.setText(mOrder.getAddress());
        textViewBrandName.setText(mOrder.getGas().getBrand().toUpperCase());
        textViewQuantity.setText("Qty. " + Integer.toString(mOrder.getQuantity()));
        double totalPrice = mOrder.getQuantity() * mOrder.getGas().getPrice();
        textViewTotalPrice.setText("RM " + String.format("%.2f", totalPrice));
        textViewSinglePrice.setText("RM " + String.format("%.2f", mOrder.getGas().getPrice()) + " ea");
        textViewDeliveryDate.setText(mOrder.getDeliveryDateText());
        textViewDeliveryTime.setText(mOrder.getDeliveryTimeText());

        if(mOrder.getStatus() == Order.ORDER_EXPIRED) {
            buttonCancel.setEnabled(false);
        }
    }

    private void cancelOrder() {
        if(mOrder.getStatus() == Order.ORDER_EXPIRED) {
            Toast.makeText(this, "This order was cancelled / declined previously", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabaseReference.child("order").child(mOrder.getOrderRef()).child("status").setValue(Order.ORDER_EXPIRED);
        mDatabaseReference.child("customer").child(mOrder.getOrderedBy()).child("order").child(mOrder.getOrderRef()).setValue(Order.ORDER_EXPIRED);
        mDatabaseReference.child("supplier").child(mOrder.getSuppliedBy()).child("order").child(mOrder.getOrderRef()).setValue(Order.ORDER_EXPIRED);
        Toast.makeText(this, "This order is cancelled", Toast.LENGTH_SHORT).show();
    }
}
