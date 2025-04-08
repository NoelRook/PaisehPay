package com.example.paisehpay.blueprints;
public class User  {
    String id;
    String email;
    String username;
    private boolean isSelected;

    public User(){}
    public User(String id, String email, String username){
        this.id = id;
        this.email = email;
        this.username = username;
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

    public void setId(String key) {
        this.id = key;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
