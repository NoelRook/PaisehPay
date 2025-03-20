package com.example.paisehpay_iris;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    Spinner oweFilterSpinner;
    View rootView;

    RecyclerView oweView;

    ArrayList<Owe> oweArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_dialog, container, false);

        //create spinner
        oweFilterSpinner = rootView.findViewById(R.id.owe_filter_spinner);
        oweFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int itemPosition, long l) {
                String item = adapterView.getItemAtPosition(itemPosition).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> oweFilterList = new ArrayList<>();
        oweFilterList.add(getString(R.string.earliest));
        oweFilterList.add(getString(R.string.latest));
        oweFilterList.add(getString(R.string.amount));
        oweFilterList.add(getString(R.string.home));
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), oweFilterList,oweFilterSpinner);
        oweFilterSpinner.setAdapter(spinnerAdapter);

        //show owelist
        oweView = rootView.findViewById(R.id.owe_recycle);
        showOweList();
        RecycleViewAdapter_Owe adapter = new RecycleViewAdapter_Owe(getActivity(),oweArray);
        oweView.setLayoutManager(new LinearLayoutManager(getActivity()));
        oweView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showOweList() {
        String[] groupList = getResources().getStringArray(R.array.dummy_owe_group_name_list);
        String[] personList = getResources().getStringArray(R.array.dummy_owe_person_name_list);
        String[] amountList = getResources().getStringArray(R.array.dummy_owe_amount_list);

        for (int i = 0; i<groupList.length; i++){
            oweArray.add(new Owe(groupList[i],personList[i],amountList[i]));

        }
    }

}
