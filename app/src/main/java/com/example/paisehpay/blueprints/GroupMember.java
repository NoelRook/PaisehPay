package com.example.paisehpay.blueprints;

import android.widget.Button;
import android.widget.ImageView;

public class GroupMember {
    //used to load recyclerview in group settings.

    String name;
    String email;
    Button button;
    ImageView avatar;

    public GroupMember(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Button getButton() {
        return button;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public ImageView getAvatar() {
        return avatar;
    }
}
// <!-- TODO: 1. honestly this can be ur user class but not sure if adaptable caus need instantiate in recycleview adapter  -->
