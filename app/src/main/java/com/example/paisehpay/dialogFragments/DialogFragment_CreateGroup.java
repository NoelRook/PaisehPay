package com.example.paisehpay.dialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DialogFragment_CreateGroup extends androidx.fragment.app.DialogFragment{
    TextView titleText;

    View rootView;
    EditText groupNameText;
    Button confirmButton;
    DialogFragmentListener<Group> listener;

    private static final String DATA_TO_QUERY = "data_to_query";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    //since we use this fragment multiple times with different data, we need to have ways to differentiate them
    public static DialogFragment_CreateGroup newInstance(String query_from) {
        DialogFragment_CreateGroup fragment = new DialogFragment_CreateGroup();
        Bundle args = new Bundle();
        args.putString(DATA_TO_QUERY, query_from);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        titleText = rootView.findViewById(R.id.create_group);


        if (getArguments() != null) {
            //changes title text based on where we instantiate it from
            String dialogTitle = getArguments().getString(DATA_TO_QUERY);
            titleText.setText(dialogTitle);

        }

        groupNameText = rootView.findViewById(R.id.group_name_input);
        confirmButton = rootView.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });
        return rootView;
    }

    //set custom layout
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }



    //ensures the HomeFragment implements the interface so that data can be sent
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() instanceof DialogFragmentListener) {
                listener = (DialogFragmentListener<Group>) getParentFragment();
            } else {
                throw new ClassCastException("Bruh ur HomeFragment never implement the interface sia how to work");
            }
        } catch (Exception e) {
            Log.e("DialogFragment", "listener got error attaching: " + e.getMessage());
        }

    }

    // <!-- TODO: 1. need error checking, whether group alr exists in user's groups, or the group code doesn't exist so far  -->
    private void createGroup(Group group){
        //todo 1. add in create group
        executorService.execute(() ->{
            GroupAdapter adapter = new GroupAdapter();
            adapter.create(group, new GroupAdapter.OperationCallback() {
                @Override
                public void onSuccess() {
                    // User created successfully
                    Log.d("Success", "User created");
                }

                @Override
                public void onError(DatabaseError error) {
                    // Handle error
                    Log.e("FirebaseError", error.getMessage());
                }
            });
        });
    };

    //create group
    public void createGroup() {
        // Get group name from input
        String groupName = groupNameText.getText().toString().trim();

        // Validate group name
        if (groupName.isEmpty()) {
            groupNameText.setError("Group name cannot be empty");
            groupNameText.requestFocus();
            return;
        }

        // Get current user
        PreferenceManager pref = new PreferenceManager(getContext());
        User currentUser = pref.getUser();
        if (currentUser == null || currentUser.getId() == null) {
            showError("User not logged in");
            return;
        }

        // Prepare group data
        String currentDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
                .format(new Date());
        HashMap<String, String> members = new HashMap<>();
        members.put(currentUser.getId(), currentUser.getUsername()); // Add creator as first member

        // Create new group
        Group newGroup = new Group(
                "", // Empty ID will be generated by Firebase
                groupName,
                "created on " + currentDate,
                "No expenses recorded yet",
                currentUser.getId(),
                members
        );

        // Save to database
        GroupAdapter groupAdapter = new GroupAdapter();
        groupAdapter.create(newGroup, new BaseDatabase.OperationCallback() {
            @Override
            public void onSuccess() {
                // Show success message
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Group created successfully", Toast.LENGTH_SHORT).show();
                        // Dismiss fragment after successful creation
                        dismiss();
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                // Show error message
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failed to create group: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("CreateGroup", "Error creating group", error.toException());
                    });
                }
            }
        });
    }

    private void showError(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show());
        }
    }



}

