package com.sjwoh.grabgas.order;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sjwoh.grabgas.R;

public class MakeOrderActivity extends AppCompatActivity implements SelectSupplierFragment.OnFragmentInteractionListener, SelectBrandFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        if (savedInstanceState == null) {
            SelectSupplierFragment selectSupplierFragment = new SelectSupplierFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.rootLayoutMakeOrder, selectSupplierFragment, "SELECT_SUPPLIER")
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
