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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private PreferenceManager preferenceManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String Username;
    String Email;
    String id;
    TextView profileName;
    Button editProfileBtn;
    Button logoutBtn;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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