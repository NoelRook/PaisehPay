package com.example.paisehpay.blueprints;

import java.util.HashMap;
import java.util.Map;

// class used to populate users in other pages
public class User  {
    private String id;
    private String email;
    private String username;
    private String friendKey;
    private HashMap<String, String> friends;
    private boolean isSelected;


    public User(){
        // Default constructor required for Firebase
    }

    // constructor
    public User(String id,
                String email,
                String username,
                String friendkey,
                HashMap<String, String> friends){
        this.id = id;
        this.email = email;
        this.username = username;
        this.friendKey = friendkey;
        this.friends = friends;
        this.isSelected = false;
    }

    // setters
    public void setUserId(String key) {
        id = key;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setFriendKey(String friendKey) {this.friendKey = friendKey;}

    public void setId(String key) {
        this.id = key;
    }
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }


    // getters
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }
    public String getFriendKey() {return friendKey;}
    public boolean isSelected() {
        return isSelected;
    }

    // functions
    public Map<String, Object> toMap() { // convert object data to map, useful to pass data to database
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("username", username);
        result.put("friendKey", friendKey);
        result.put("friends", friends);
        return result;
    }
}
