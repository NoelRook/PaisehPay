package com.example.paisehpay.blueprints;

import java.util.HashMap;
import java.util.Map;

public class User  {
    private String id;
    private String email;
    private String username;
    private String friendKey;
    private HashMap<String, String> friends;
    private boolean isSelected;

    public User(){}
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

    public String getFriendKey() {return friendKey;}

    public void setFriendKey(String friendKey) {this.friendKey = friendKey;}

    public void setId(String key) {
        this.id = key;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public HashMap<String, String> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, String> friends) {
        this.friends = friends;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("username", username);
        result.put("friendKey", friendKey);
        result.put("friends", friends);
        return result;
    }
}
