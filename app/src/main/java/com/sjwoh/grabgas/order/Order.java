package com.sjwoh.grabgas.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by choonho on 16/6/2016.
 */
public class Order implements Parcelable {
    public static final int ORDER_EXPIRED = -1;
    public static final int ORDER_PENDING = 0;
    public static final int ORDER_DONE = 1;

    private String mOrderRef;
    private String mOrderedBy;
    private String mSuppliedBy;
    private String mAddress;
    private String mOrderDateText;
    private String mOrderTimeText;
    private String mDeliveryDateText;
    private String mDeliveryTimeText;
    private int mQuantity;
    private int mStatus;
    private Gas mGas;

    public Order() {
        mOrderRef = "";
    }

    public Order(String mOrderedBy, String mSuppliedBy, String mAddress, String mOrderDateText, String mOrderTimeText, String mDeliveryDateText, String mDeliveryTimeText, int mQuantity, int mStatus, Gas mGas) {
        this.mOrderedBy = mOrderedBy;
        this.mSuppliedBy = mSuppliedBy;
        this.mAddress = mAddress;
        this.mOrderDateText = mOrderDateText;
        this.mOrderTimeText = mOrderTimeText;
        this.mDeliveryDateText = mDeliveryDateText;
        this.mDeliveryTimeText = mDeliveryTimeText;
        this.mQuantity = mQuantity;
        this.mStatus = mStatus;
        this.mGas = mGas;
    }

    public String getOrderRef() { return mOrderRef; }

    public String getOrderedBy() {
        return mOrderedBy;
    }

    public String getSuppliedBy() {
        return mSuppliedBy;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getOrderDateText() {
        return mOrderDateText;
    }

    public String getOrderTimeText() {
        return mOrderTimeText;
    }

    public String getDeliveryDateText() {
        return mDeliveryDateText;
    }

    public String getDeliveryTimeText() {
        return mDeliveryTimeText;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public int getStatus() {
        return mStatus;
    }

    public Gas getGas() {
        return mGas;
    }

    public void setOrderRef(String mOrderRef) { this.mOrderRef = mOrderRef; }

    public void setOrderedBy(String mOrderedBy) {
        this.mOrderedBy = mOrderedBy;
    }

    public void setSuppliedBy(String mSuppliedBy) {
        this.mSuppliedBy = mSuppliedBy;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public void setOrderDateText(String mOrderDateText) {
        this.mOrderDateText = mOrderDateText;
    }

    public void setOrderTimeText(String mOrderTimeText) {
        this.mOrderTimeText = mOrderTimeText;
    }

    public void setDeliveryDateText(String mDeliveryDateText) {
        this.mDeliveryDateText = mDeliveryDateText;
    }

    public void setDeliveryTimeText(String mDeliveryTimeText) {
        this.mDeliveryTimeText = mDeliveryTimeText;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public void setGas(Gas mGas) {
        this.mGas = mGas;
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel inParcel) {
            return new Order(inParcel);
        }
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(getOrderRef());
        outParcel.writeString(getOrderedBy());
        outParcel.writeString(getSuppliedBy());
        outParcel.writeString(getAddress());
        outParcel.writeString(getOrderDateText());
        outParcel.writeString(getOrderTimeText());
        outParcel.writeString(getDeliveryDateText());
        outParcel.writeString(getDeliveryTimeText());
        outParcel.writeInt(getQuantity());
        outParcel.writeInt(getStatus());
        outParcel.writeParcelable(getGas(), flags);
    }

    protected Order(Parcel inParcel) {
        mOrderRef = inParcel.readString();
        mOrderedBy = inParcel.readString();
        mSuppliedBy = inParcel.readString();
        mAddress = inParcel.readString();
        mOrderDateText = inParcel.readString();
        mOrderTimeText = inParcel.readString();
        mDeliveryDateText = inParcel.readString();
        mDeliveryTimeText = inParcel.readString();
        mQuantity = inParcel.readInt();
        mStatus = inParcel.readInt();
        mGas = inParcel.readParcelable(Gas.class.getClassLoader());
    }
}
