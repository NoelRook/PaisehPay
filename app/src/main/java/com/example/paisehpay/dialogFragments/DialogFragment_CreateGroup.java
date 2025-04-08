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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
                String groupName = groupNameText.getText().toString().trim();
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                if (listener != null && !groupName.isEmpty() ){
                    //if the DialogFragmentListener is attached to the HomeFragment, data can be sent back
                    Log.d("DialogFragment", "Sending new group: " + groupName);
                    //we instantiate a new Group Object
                    Group group = new Group("someid",
                            "someid",
                            "Created " + "someid",
                            "someid",
                            new HashMap<>());
                    //we override the DialogFragmentListener method in HomeFragment to be able to add groups
                    //create group here
                    createGroup(group);
                    listener.onDataSelected(0,group);
                }
                //dismiss fragment after data is sent
                dismiss();

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



}

