package com.sjwoh.grabgas.supplier;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by choonho on 15/6/2016.
 */
public class Gas implements Parcelable {
    private String mBrand;
    private double mPrice;

    public Gas() {
    }

    public Gas(String mBrand, double mPrice) {
        this.mBrand = mBrand;
        this.mPrice = mPrice;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        this.mBrand = brand;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(getBrand());
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
        mBrand = inParcel.readString();
        mPrice = inParcel.readDouble();
    }
}
