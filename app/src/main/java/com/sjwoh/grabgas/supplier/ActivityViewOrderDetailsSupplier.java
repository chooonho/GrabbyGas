package com.sjwoh.grabgas.supplier;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

public class ActivityViewOrderDetailsSupplier extends AppCompatActivity {

    private TextView textViewCustomerUsername, textViewSupplierUsername, textViewAddress,
            textViewBrandName, textViewQuantity, textViewTotalPrice, textViewSinglePrice,
            textViewDeliveryDate, textViewDeliveryTime;
    private Button buttonDecline, buttonProcess;
    private DatabaseReference mDatabaseReference;
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details_supplier);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uriEncodedAddress = Uri.encode(mOrder.getAddress());
                Uri gmmIntentUti = Uri.parse("geo:0,0?q=" + uriEncodedAddress);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUti);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        textViewCustomerUsername = (TextView)findViewById(R.id.textViewCustomerUsername);
        textViewSupplierUsername = (TextView)findViewById(R.id.textViewSupplierUsername);
        textViewAddress = (TextView)findViewById(R.id.textViewAddress);
        textViewBrandName = (TextView)findViewById(R.id.textViewBrandName);
        textViewQuantity = (TextView)findViewById(R.id.textViewQuantity);
        textViewTotalPrice = (TextView)findViewById(R.id.textViewTotalPrice);
        textViewSinglePrice = (TextView)findViewById(R.id.textViewSinglePrice);
        textViewDeliveryDate = (TextView)findViewById(R.id.textViewDeliveryDate);
        textViewDeliveryTime = (TextView)findViewById(R.id.textViewDeliveryTime);

        buttonDecline = (Button)findViewById(R.id.buttonDecline);
        buttonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ActivityViewOrderDetailsSupplier.this)
                        .setTitle("Decline order")
                        .setMessage("Are you sure you want to decline this order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                declineOrder();
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

        buttonProcess = (Button)findViewById(R.id.buttonProcess);
        buttonProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ActivityViewOrderDetailsSupplier.this)
                        .setTitle("Process order")
                        .setMessage("Are you sure you want to process this order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                processOrder();
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

        mOrder = getIntent().getParcelableExtra("ORDER_OBJECT");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        initTextViewValues();
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

        if(mOrder.getStatus() == Order.ORDER_DONE || mOrder.getStatus() == Order.ORDER_EXPIRED) {
            buttonDecline.setEnabled(false);
            buttonProcess.setEnabled(false);
        }
    }

    private void declineOrder() {
        if(mOrder.getStatus() == Order.ORDER_EXPIRED) {
            Toast.makeText(this, "This order was cancelled / declined previously", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabaseReference.child("order").child(mOrder.getOrderRef()).child("status").setValue(Order.ORDER_EXPIRED);
        mDatabaseReference.child("customer").child(mOrder.getOrderedBy()).child("order").child(mOrder.getOrderRef()).setValue(Order.ORDER_EXPIRED);
        mDatabaseReference.child("supplier").child(mOrder.getSuppliedBy()).child("order").child(mOrder.getOrderRef()).setValue(Order.ORDER_EXPIRED);
        Toast.makeText(this, "This order is cancelled", Toast.LENGTH_SHORT).show();
    }

    private void processOrder() {
        if(mOrder.getStatus() == Order.ORDER_EXPIRED) {
            Toast.makeText(this, "This order was processed previously", Toast.LENGTH_SHORT).show();
            return;
        }

        buttonDecline.setEnabled(false);
        buttonProcess.setEnabled(false);

        mDatabaseReference.child("order").child(mOrder.getOrderRef()).child("status").setValue(Order.ORDER_DONE);
        mDatabaseReference.child("customer").child(mOrder.getOrderedBy()).child("order").child(mOrder.getOrderRef()).setValue(Order.ORDER_DONE);
        mDatabaseReference.child("supplier").child(mOrder.getSuppliedBy()).child("order").child(mOrder.getOrderRef()).setValue(Order.ORDER_DONE);
        Toast.makeText(this, "This order is processed", Toast.LENGTH_SHORT).show();
    }
}
