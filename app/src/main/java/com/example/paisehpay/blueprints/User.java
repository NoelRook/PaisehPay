package com.example.paisehpay.blueprints;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User  {
    String id;
    String email;
    String username;

    public User(){}
    public User(String id, String email, String username){
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public void setUserId(String key) {
        id = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }
}
