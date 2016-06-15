package com.sjwoh.grabgas.order;

/**
 * Created by choonho on 15/6/2016.
 */
public class Gas {
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
}
