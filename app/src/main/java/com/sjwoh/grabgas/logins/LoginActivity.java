package com.sjwoh.grabgas.logins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.customer.MainActivityCustomer;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mUserReference;
    private Button mButtonLogin;
    private EditText mEditTextUsername, mEditTextPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mButtonLogin = (Button)findViewById(R.id.buttonLogin);
        mButtonLogin.setOnClickListener(this);
        mEditTextUsername = (EditText)findViewById(R.id.editTextUsername);
        mEditTextPassword = (EditText)findViewById(R.id.editTextPassword);

        sharedPreferences = getSharedPreferences("USER_PREFERENCE", Context.MODE_PRIVATE);
    }

    private boolean login() {
        final String username = mEditTextUsername.getText().toString();
        final String password = mEditTextPassword.getText().toString();
        final String token = username + UUID.randomUUID().toString();
        if(username.isEmpty() || password.isEmpty()) {
            return false;
        }

        final ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(dataSnapshot.child("password").getValue().toString().equals(password)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USER_USERNAME", username);
                    editor.putString("USER_TOKEN", token);
                    editor.apply();

                    if(dataSnapshot.child("userType").getValue().toString().equals("C")) {
                        Customer customer = new Customer();

                        customer.setUsername(dataSnapshot.getKey());
                        customer.setMobilePhone(dataSnapshot.child("mobilePhone").getValue().toString());
                        customer.setName(dataSnapshot.child("name").getValue().toString());
                        customer.setAddress(dataSnapshot.child("address").getValue().toString());

                        loginCallback(customer, token, this);
                    }
                    else {
                        Supplier supplier = new Supplier();

                        supplier.setUsername(dataSnapshot.getKey());
                        supplier.setMobilePhone(dataSnapshot.child("mobilePhone").getValue().toString());
                        supplier.setName(dataSnapshot.child("name").getValue().toString());
                        supplier.setLocLatitude(Double.parseDouble(dataSnapshot.child("locLatitude").getValue().toString()));
                        supplier.setLocLongitude(Double.parseDouble(dataSnapshot.child("locLongitude").getValue().toString()));

                        loginCallback(supplier, token, this);
                    }

                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                // TODO: Log message
            }
        };

        mUserReference = FirebaseDatabase.getInstance().getReference().child("user").child(username);
        mUserReference.addValueEventListener(userListener);

        return true;
    }

    private void loginCallback(User user, String token, ValueEventListener valueEventListener) {
        mUserReference.removeEventListener(valueEventListener);
        mUserReference.child("token").setValue(token);

        Intent intent = new Intent(getApplicationContext(), MainActivityCustomer.class);
        intent.putExtra("USER_OBJECT", user);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonLogin:
                login();
                break;
        }
    }
}
