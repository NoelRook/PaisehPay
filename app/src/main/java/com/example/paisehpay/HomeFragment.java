package com.example.paisehpay;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.sessionHandler.PreferenceManager;


public class HomeFragment extends Fragment {
    //fragment that loads when u press home
    TextView welcomeMessage;
    Button oweDetailsButton;
    Button owedDetailsButton;
    Button groupButton;
    Button joinGroupButton;
    Button createGroupButton;
    ConstraintLayout oweLayout;
    DialogFragment_Owe oweDialogFragment;
    DialogFragment_Owe owedDialogFragment;

    DialogFragment_CreateGroup createGroupFragment;
    DialogFragment_CreateGroup joinGroupFragment;
    String Username;
    String Email;
    String id;


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        PreferenceManager preferenceManager = new PreferenceManager(getContext());

        User savedUser = preferenceManager.getUser();
        if (savedUser != null) {
            id = savedUser.getId();
            Username = savedUser.getUsername();
            Email = savedUser.getEmail();
            Log.d("Usersaved",id + Username +Email);
        }

        welcomeMessage = rootView.findViewById(R.id.welcome_message);
        welcomeMessage.setText("Hello "+ Username+"!");

        //view owe details lead to owe details page
        oweLayout = rootView.findViewById(R.id.owe_layout);
        oweDetailsButton = oweLayout.findViewById(R.id.owe_more_details);
        oweDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oweDialogFragment = new DialogFragment_Owe();
                oweDialogFragment.show(getActivity().getSupportFragmentManager(), "DialogFragment");
            }
        });
        //view owed details lead to owed details page
        owedDetailsButton = oweLayout.findViewById(R.id.owed_more_details);
        owedDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owedDialogFragment = new DialogFragment_Owe();
                owedDialogFragment.show(getActivity().getSupportFragmentManager(), "DialogFragment");
            }
        });

        //press join group will join a group
        joinGroupButton = rootView.findViewById(R.id.join_group);
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroupFragment = new DialogFragment_CreateGroup();
                joinGroupFragment.show(getActivity().getSupportFragmentManager(), "DialogFragment_CreateGroup");
            }
        });

        //press create group will create a group
        createGroupButton = rootView.findViewById(R.id.create_group);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupFragment = new DialogFragment_CreateGroup();
                createGroupFragment.show(getActivity().getSupportFragmentManager(), "DialogFragment_CreateGroup");
            }
        });



        //press group button to group homepage
        groupButton = rootView.findViewById(R.id.group_homepage2);
        groupButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), GroupHomepage.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            getActivity().finish();
        });


        return rootView;
    }
}

// <!-- TODO: 1. change "hello username" text to user's name  -->

// <!-- TODO: 2. change date text to update today's date -->
// <!-- TODO: 3. change owe_amount and owed_amount from db  -->
// <!-- TODO: 4. catch name of group data in both create group and join group button, then instantiate a new group object for that user -->
// <!-- TODO: 5. add them to user's db and update group recycleview  -->
// <!-- TODO: 6. figure out how to isolate a certain group button to lead to the correct group homepage  -->
// <!-- TODO: 7. (for iris) change scrolllayout to reycleview with the usual shit  -->