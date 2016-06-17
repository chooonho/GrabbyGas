package com.sjwoh.grabgas.customer;

import android.os.Parcel;
import android.os.Parcelable;

import com.sjwoh.grabgas.logins.User;

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

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        public Customer createFromParcel(Parcel inParcel) {
            return new Customer(inParcel);
        }
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    private Customer(Parcel inParcel) {
        super(inParcel);
        mAddress = inParcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        super.writeToParcel(outParcel, flags);
        outParcel.writeString(getAddress());
    }
}
