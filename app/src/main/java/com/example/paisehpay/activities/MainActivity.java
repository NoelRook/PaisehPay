package com.example.paisehpay.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.paisehpay.mainActivityFragments.FriendsFragment;
import com.example.paisehpay.mainActivityFragments.HomeFragment;
import com.example.paisehpay.mainActivityFragments.NotificationFragment;
import com.example.paisehpay.mainActivityFragments.ProfileFragment;
import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databinding.ActivityMainBinding;
import com.example.paisehpay.mainActivityFragments.AddFragment;
import com.example.paisehpay.sessionHandler.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    //main activity that loads the bottom nav screen and the 5 fragments

    ActivityMainBinding binding;
    String Username;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        PreferenceManager preferenceManager = new PreferenceManager(this);
        User savedUser = preferenceManager.getUser();
        if (savedUser != null) {
            Username = savedUser.getUsername();
            Email = savedUser.getEmail();
        }



        Intent intent = getIntent();
        String fragmentToLoad = intent.getStringExtra("fragmentToLoad");

        // Remove the savedFragmentId logic completely
        if (fragmentToLoad != null) {
            loadFragmentFromIntent(fragmentToLoad);
        } else {
            replaceFragment(new ProfileFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.home);
        }



        int savedFragmentId = preferenceManager.savedFragmentId();

        if (fragmentToLoad != null) {
            loadFragmentFromIntent(fragmentToLoad);
        } else if (savedFragmentId != -1) {
            if (savedFragmentId == R.id.profile) {
                replaceFragment(new ProfileFragment());
                binding.bottomNavigationView.setSelectedItemId(R.id.profile);
            } else if (savedFragmentId == R.id.notification) {
                replaceFragment(new NotificationFragment());
                binding.bottomNavigationView.setSelectedItemId(R.id.notification);
            } else if (savedFragmentId == R.id.friends) {
                replaceFragment(new FriendsFragment());
                binding.bottomNavigationView.setSelectedItemId(R.id.friends);
            } else {
                replaceFragment(new HomeFragment());
                binding.bottomNavigationView.setSelectedItemId(R.id.home);
            }
        } else {
            replaceFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.home);
        }


        //if navigating between fragments using the bottom navigation view
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.notification) {
                replaceFragment(new NotificationFragment());
            } else if (itemId == R.id.add) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.constraint_layout);

                // Show ModalBottomSheet only if AddFragment is not already open
                if (!(currentFragment instanceof AddFragment)) {
                    ModalBottomSheet bottomSheet = new ModalBottomSheet();
                    bottomSheet.show(getSupportFragmentManager(), "ScanSelectionBottomSheet");
                }
                return false; // Prevents user from reselecting Add Expense in bottom navigation menu

            } else if (itemId == R.id.friends) {
                replaceFragment(new FriendsFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

    }
    //replace fragment if navigating from bottom navigation page
    protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraint_layout, fragment);

        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        fragmentTransaction.commitNow(); //bloody fucking fucking hell
    }

    //load specific fragment from activity
    private void loadFragmentFromIntent(String fragmentName) {
        Log.d("ThemeDebug", "Using loadFragmentFromIntent with: " + fragmentName);
        Fragment fragment;
        int fragmentId;

        if (fragmentName.equals("ProfileFragment")) {
            fragment = new ProfileFragment();
            fragmentId = R.id.profile;
        } else if (fragmentName.equals("NotificationFragment")) {
            fragment = new NotificationFragment();
            fragmentId = R.id.notification;
        } else if (fragmentName.equals("AddFragment")) {
            fragment = new AddFragment();
            fragmentId = R.id.add;
        }
        else if (fragmentName.equals("FriendsFragment")) {
            fragment = new FriendsFragment();
            fragmentId = R.id.friends;
        } else{
            fragment = new HomeFragment();
            fragmentId = R.id.home;
        }
        if (fragment != null){
            replaceFragment(fragment);

            binding.bottomNavigationView.setSelectedItemId(fragmentId);
        }
    }

}
