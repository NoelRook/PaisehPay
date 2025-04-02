package com.example.paisehpay.sessionHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.paisehpay.blueprints.User;

public class PreferenceManager {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save user data
    public void saveUser(User user) {
        Log.d("savedUseredit", user.getId() +user.getUsername() +user.getEmail());
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.apply();
    }

    // Get user data
    public User getUser() {
        String id = sharedPreferences.getString(KEY_USER_ID, null);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String email = sharedPreferences.getString(KEY_EMAIL, null);

        if (username != null && email != null) {
            return new User(id, username, email);
        }
        return null;
    }

    // Clear user data (for logout)
    public void clearUser() {
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_EMAIL);
        editor.apply();
    }
}