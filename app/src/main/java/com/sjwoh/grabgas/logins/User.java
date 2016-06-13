package com.sjwoh.grabgas.logins;

/**
 * Created by choonho on 13/6/2016.
 */
public class User {
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
        this.mMobilePhone = mobilePhone;
        this.mName = name;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getMobilePhone() {
        return mMobilePhone;
    }

    public String getName() {
        return mName;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mMobilePhone = mobilePhone;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
