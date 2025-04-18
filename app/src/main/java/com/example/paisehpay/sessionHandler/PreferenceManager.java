package com.example.paisehpay.sessionHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.paisehpay.blueprints.User;
//import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PreferenceManager {
    // manage the shared pref for all apps so that shared pref does not need to be re instantiated every time
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FRIENDKEY = "friendKey";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    // Save user data
    public void saveUser(User user) {
        Log.d("savedUseredit", user.getId() +user.getUsername() +user.getEmail()+user.getFriendKey());
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FRIENDKEY, user.getFriendKey());
        editor.apply();
    }

    // Get user data
    public User getUser() {
        String id = sharedPreferences.getString(KEY_USER_ID, null);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String friendKey = sharedPreferences.getString(KEY_FRIENDKEY, null);

        if (username != null && email != null) {
            return new User(id, email, username, friendKey, null);
        }
        return null;
    }
    public int savedFragmentId(){
        return sharedPreferences.getInt("selectedFragmentId", -1);
    }

    //remember me
    public void rememberMe(boolean isChecked){
        sharedPreferences.edit().putBoolean("checkbox_state", isChecked).apply();
    }

    // check if the user wants to be remember the next time they open the application
    public boolean getRememberMe(){
        return sharedPreferences.getBoolean("checkbox_state", false);
    }

    // clear all stored data
    public void clearPreferences(){
        sharedPreferences.edit().clear().apply();
    }

    // map all friends to shared pref when user logs in for the first time
    public void mapManyFriends(List<User> userList){
        for (User user : userList){
            mapOneFriend(user.getUsername(), user.getId());
        }
    }

    // maps a singular friend
    public void mapOneFriend(String Username, String userID){
        // Store plain userID as key, encrypted username as value
        editor.putString(userID, Username);
        editor.apply();
    }

    // gets friend's username based on the friend ID given.
    public String getOneFriend(String userID){
        // Retrieve encrypted value and decrypt it
        String encrypted = sharedPreferences.getString(userID, null);
        if(Objects.equals(userID, getUser().getId())){
            Log.d("test",getUser().getId());
            return getUser().getUsername();
        }
        return encrypted;
    }
}