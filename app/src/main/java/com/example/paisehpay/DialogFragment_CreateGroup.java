package com.example.paisehpay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class DialogFragment_CreateGroup extends androidx.fragment.app.DialogFragment {
    //popup class when u press create group

    View rootView;
    EditText groupNameText;
    Button confirmButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        groupNameText = rootView.findViewById(R.id.group_name_input);
        confirmButton = rootView.findViewById(R.id.confirm_button);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    // <!-- TODO: 1. when press confirm button need instantiate a new Group Object with group.id, group.name and wtv else-->
    // <!-- TODO: 2. add new group into group recyclerview and instantiate a new homepage for them  -->

}
