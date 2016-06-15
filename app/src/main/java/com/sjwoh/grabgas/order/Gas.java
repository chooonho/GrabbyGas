package com.sjwoh.grabgas.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by choonho on 15/6/2016.
 */
public class Gas implements Parcelable {
    private String mName;
    private double mPrice;

    public Gas() {
    }

    public Gas(String mName, double mPrice) {
        this.mName = mName;
        this.mPrice = mPrice;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(getName());
        outParcel.writeDouble(getPrice());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Gas> CREATOR = new Parcelable.Creator<Gas>() {
        public Gas createFromParcel(Parcel inParcel) {
            return new Gas(inParcel);
        }
        public Gas[] newArray(int size) {
            return new Gas[size];
        }
    };

    protected Gas(Parcel inParcel) {
        mName = inParcel.readString();
        mPrice = inParcel.readDouble();
    }
}
