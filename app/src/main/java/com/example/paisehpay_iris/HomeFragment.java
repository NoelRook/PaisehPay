package com.example.paisehpay_iris;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class HomeFragment extends Fragment {
    Button oweDetailsButton;
    Button owedDetailsButton;
    Button groupButton;
    TextView owedAmountText;
    TextView oweAmountText;
    ConstraintLayout oweLayout;
    DialogFragment oweDialogFragment;
    DialogFragment owedDialogFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //view owe details lead to owe details page
        oweLayout = rootView.findViewById(R.id.owe_layout);
        oweDetailsButton = oweLayout.findViewById(R.id.owe_more_details);
        oweDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oweDialogFragment = new DialogFragment();
                oweDialogFragment.show(getActivity().getSupportFragmentManager(), "DialogFragment");
            }
        });
        //view owed details lead to owed details page
        owedDetailsButton = oweLayout.findViewById(R.id.owed_more_details);
        owedDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owedDialogFragment = new DialogFragment();
                owedDialogFragment.show(getActivity().getSupportFragmentManager(), "DialogFragment");
            }
        });

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