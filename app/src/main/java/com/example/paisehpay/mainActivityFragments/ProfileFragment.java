package com.example.paisehpay.mainActivityFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.paisehpay.R;
import com.example.paisehpay.activities.SignIn;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.sessionHandler.PreferenceManager;


public class ProfileFragment extends Fragment {

    private PreferenceManager preferenceManager;

    String Username;
    String Email;
    String id;
    TextView profileName;
    Button editProfileBtn;
    Button logoutBtn;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        preferenceManager = new PreferenceManager(getContext());

        profileName = rootView.findViewById(R.id.my_profile);
        editProfileBtn= rootView.findViewById(R.id.profile_edit);
        logoutBtn= rootView.findViewById(R.id.logout_button);

        User savedUser = preferenceManager.getUser();
        if (savedUser != null) {
            id = savedUser.getId();
            Username = savedUser.getUsername();
            Email = savedUser.getEmail();
            Log.d("Usersaved",id + Username +Email);
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    //perform logout
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logoutUser())
                .setNegativeButton("No", null)
                .show();
    }

    // logout function
    private void logoutUser(){
        preferenceManager.clearPreferences();
        Intent intent = new Intent(getContext(), SignIn.class);
        startActivity(intent);
        requireActivity().finish();
    }



}

// <!-- TODO: 1. Don't touch, still WIP  -->