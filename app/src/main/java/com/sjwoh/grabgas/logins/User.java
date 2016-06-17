package com.sjwoh.grabgas.logins;

import android.os.Parcel;
import android.os.Parcelable;

import com.sjwoh.grabgas.customer.Customer;
import com.sjwoh.grabgas.supplier.Supplier;

/**
 * Created by choonho on 13/6/2016.
 */
public abstract class User implements Parcelable {
    private String mUsername;
    private String mMobilePhone;
    private String mName;

    public User() {
        mUsername = "";
        mMobilePhone = "";
        mName = "";
    }

    public User(String username, String mobilePhone, String name) {
        this.mUsername = username;
        this.mName = name;
        this.mMobilePhone = mobilePhone;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getName() {
        return mName;
    }

    public String getMobilePhone() {
        return mMobilePhone;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mMobilePhone = mobilePhone;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(getUsername());
        outParcel.writeString(getName());
        outParcel.writeString(getMobilePhone());
    }

    protected User(Parcel inParcel) {
        mUsername = inParcel.readString();
        mName = inParcel.readString();
        mMobilePhone = inParcel.readString();
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Supplier) {
            return getUsername().equals(((Supplier)object).getUsername());
        }

        if(object instanceof Customer) {
            return getUsername().equals(((Customer)object).getUsername());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
}
