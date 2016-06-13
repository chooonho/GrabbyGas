package com.sjwoh.grabgas.logins;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by choonho on 13/6/2016.
 */
public class Supplier extends User {
    public Supplier() { }

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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        super.writeToParcel(outParcel, flags);
    }
}
