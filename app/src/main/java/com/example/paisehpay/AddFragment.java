package com.example.paisehpay;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AddFragment extends Fragment {
    //this is the fragment that loads when you press the Add Expense Button;

    Button button; //currently this is the page that is a temporary for the camera

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //used for computation

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);


        //click button lead to reciept overview
        button = rootView.findViewById(R.id.test_button); //temp button
        button.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ReceiptOverview.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            getActivity().finish();
        });
        return rootView;

    }

    // <!-- TODO: 1. add camera in, take pic, process pic  -->
    // <!-- TODO: 2. use shared preferences to save data to auto fill in reciept overview if scanning -->

}