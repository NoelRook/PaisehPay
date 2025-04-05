package com.example.paisehpay.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.paisehpay.databinding.ActivityMainBinding;
import com.example.paisehpay.mainActivityFragments.AddFragment;

public class MainActivity extends AppCompatActivity {
    //main activity that loads the bottom nav screen and the 5 fragments

    ActivityMainBinding binding;

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


        // check which fragment to load
        Intent intent = getIntent();
        String fragmentToLoad = intent.getStringExtra("fragmentToLoad");
        if (fragmentToLoad != null) { //if navigating from activity to fragment
            loadFragmentFromIntent(fragmentToLoad);
        } else { //if navigating from SignIn to fragment, where the default fragment is the home page
            replaceFragment(new HomeFragment());
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
        fragmentTransaction.commitNow(); //bloody fucking fucking hell
    }

    //load specific fragment from activity
    private void loadFragmentFromIntent(String fragmentName) {
        Fragment fragment = new HomeFragment();
        int fragmentId = R.id.home;

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
        }
        if (fragment != null){
            replaceFragment(fragment);
            binding.bottomNavigationView.setSelectedItemId(fragmentId);
        }
    }




}