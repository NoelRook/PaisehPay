package com.example.paisehpay.mainActivityFragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.paisehpay.R;
import com.example.paisehpay.activities.MainActivity;
import com.example.paisehpay.activities.SignIn;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.sessionHandler.PreferenceManager;


public class ProfileFragment extends Fragment {

    private PreferenceManager preferenceManager;

    String Username;
    String Email;
    String id;
    String Code;
    Button logoutBtn;
    ConstraintLayout profileLayout;
    ConstraintLayout darkModeLayout;
    SwitchCompat darkModeSwitch;

    TextView profileNameText;
    TextView profileEmailText;
    TextView profileCodeText;
    boolean isDarkMode;



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


        User savedUser = preferenceManager.getUser();
        if (savedUser != null) {
            id = savedUser.getId();
            Username = savedUser.getUsername();
            Email = savedUser.getEmail();
            Code = savedUser.getFriendKey();

            Log.d("Usersaved",id + Username +Email+Code);
        }


        //set username from db
        profileLayout = rootView.findViewById(R.id.profile_layout);
        profileNameText = profileLayout.findViewById(R.id.profile_name);
        profileNameText.setText(Username);

        //set email from db
        profileEmailText = profileLayout.findViewById(R.id.profile_email);
        profileEmailText.setText(Email);

        //set profile code
        profileCodeText = rootView.findViewById(R.id.user_code);
        profileCodeText.setText(Code);




        //turn on darkmode
        SwitchCompat themeSwitch = rootView.findViewById(R.id.light_mode_switch); // Replace with your ID

        // Check and apply current mode on launch
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            themeSwitch.setChecked(true); // Dark mode on
        } else {
            themeSwitch.setChecked(false); // Light mode on
        }

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //Save the current selected fragment ID before theme change
            preferenceManager.getEditor().putInt("selectedFragmentId", R.id.profile).apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Save the current fragment name before recreating
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.putExtra("fragmentToLoad", "ProfileFragment");
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            startActivity(intent);
            requireActivity().finish(); // Finish current activity so theme applies
        });






        //press logout button to logout
        logoutBtn= rootView.findViewById(R.id.logout_button);
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