package com.example.paisehpay.dialogFragments;

import android.content.Context;
import android.os.Bundle;
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

public class DialogFragment_CreateGroup extends androidx.fragment.app.DialogFragment{
    //popup class when u press create group
    private String dialogTitle;
    TextView titleText;

    View rootView;
    EditText groupNameText;
    Button confirmButton;
    DialogFragmentListener listener;

    private static final String DATA_TO_QUERY = "data_to_query";


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
            dialogTitle = getArguments().getString(DATA_TO_QUERY);
            titleText.setText(dialogTitle);

        }

        groupNameText = rootView.findViewById(R.id.group_name_input);
        confirmButton = rootView.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = groupNameText.getText().toString().trim();
                if (listener != null && !groupName.isEmpty() ){
                    Log.d("DialogFragment", "Sending new group: " + groupName);
                    listener.onDataSelected(-2,groupName);
                }
                dismiss();

            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() instanceof DialogFragmentListener) {
                listener = (DialogFragmentListener) getParentFragment();
            } else {
                throw new ClassCastException("Bruh ur HomeFragment never implement the class");
            }
        } catch (Exception e) {
            Log.e("DialogFragment", "listener got error attaching: " + e.getMessage());
        }

    }

    // <!-- TODO: 1. need error checking, whether group alr exists in user's groups, or the group code doesn't exist so far  -->

}

