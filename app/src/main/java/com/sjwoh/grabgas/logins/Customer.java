package com.sjwoh.grabgas.logins;

/**
 * Created by choonho on 13/6/2016.
 */
public class Customer extends User {
    private String mAddress;

    public Customer() {
        mAddress = "";
    }

    public Customer(String username, String mobilePhone, String name, String address) {
        super(username, mobilePhone, name);
        this.mAddress = address;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }
}
