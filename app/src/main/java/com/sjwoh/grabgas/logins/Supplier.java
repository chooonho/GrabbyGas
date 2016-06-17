package com.sjwoh.grabgas.logins;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.sjwoh.grabgas.order.Gas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choonho on 13/6/2016.
 */
public class Supplier extends User {
    private double mLocLatitude;
    private double mLocLongitude;
    private List<Gas> mGases;

    public Supplier() {
        mLocLatitude = 0;
        mLocLongitude = 0;
    }

    public Supplier(String username, String mobilePhone, String name, double mLocLatitude, double mLocLongitude, List<Gas> gases) {
        super(username, mobilePhone, name);
        this.mLocLatitude = mLocLatitude;
        this.mLocLongitude = mLocLongitude;
        this.mGases = gases;
    }

    public List<Gas> getGases() {
        return mGases;
    }

    public double getLocLatitude() {
        return mLocLatitude;
    }

    public double getLocLongitude() {
        return mLocLongitude;
    }

    public void setLocLongitude(double locLongitude) {
        this.mLocLongitude = locLongitude;
    }

    public void setLocLatitude(double locLatitude) {
        this.mLocLatitude = locLatitude;
    }

    public void setGases(List<Gas> gases) {
        mGases = gases;
    }

    public void addGass(Gas gas) {
        if(mGases == null) {
            mGases = new ArrayList<>();
        }

        mGases.add(gas);
    }

    public static final Parcelable.Creator<Supplier> CREATOR = new Parcelable.Creator<Supplier>() {
        public Supplier createFromParcel(Parcel inParcel) {
            return new Supplier(inParcel);
        }
        public Supplier[] newArray(int size) {
            return new Supplier[size];
        }
    };

    private Supplier(Parcel inParcel) {
        super(inParcel);
        mLocLatitude = inParcel.readDouble();
        mLocLongitude = inParcel.readDouble();

        Bundle bGases = new Bundle();
        if(!bGases.isEmpty()) {
            for(String key : bGases.keySet()) {
                Gas gas = new Gas(key, bGases.getDouble(key));
                mGases.add(gas);
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        super.writeToParcel(outParcel, flags);
        outParcel.writeDouble(getLocLatitude());
        outParcel.writeDouble(getLocLongitude());

        Bundle bGases = new Bundle();
        if(getGases() != null) {
            for(int i = 0; i < mGases.size(); i++) {
                bGases.putDouble(mGases.get(i).getBrand(), mGases.get(i).getPrice());
            }
        }
        outParcel.writeBundle(bGases);
    }
}
