package com.sjwoh.grabgas;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.sjwoh.grabgas.customer.Customer;
import com.sjwoh.grabgas.logins.User;
import com.sjwoh.grabgas.supplier.Supplier;

public class ActivitySettings extends AppCompatActivity {

    private TextView textViewTitle, textViewUsername, textViewAddress, textViewMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Profile Settings");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewUsername = (TextView)findViewById(R.id.textViewUsername);
        textViewAddress = (TextView)findViewById(R.id.textViewAddress);
        textViewMobile = (TextView)findViewById(R.id.textViewMobile);
        textViewTitle = (TextView)findViewById(R.id.textViewTitle);

        initUserProfile();
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

    private void initUserProfile() {
        User user = getIntent().getParcelableExtra("USER_OBJECT");

        textViewUsername.setText(user.getName() + "\n@" + user.getUsername() + "");
        if(user instanceof Customer) {
            textViewAddress.setText(((Customer)user).getAddress());
        }
        else if(user instanceof Supplier) {
            textViewTitle.setBackgroundColor(Color.parseColor("#545AA7"));
            textViewAddress.setText(((Supplier)user).getLocLatitude() + ", " + ((Supplier)user).getLocLongitude());
        }
        textViewMobile.setText(user.getMobilePhone());
    }
}
