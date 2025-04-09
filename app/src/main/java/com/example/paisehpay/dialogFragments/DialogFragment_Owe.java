package com.example.paisehpay.dialogFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.blueprints.Owe;
import com.example.paisehpay.R;
import com.example.paisehpay.computation.HeapSortHelper;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Owe;
import com.example.paisehpay.tabBar.SpinnerAdapter;

import java.util.ArrayList;
import com.example.paisehpay.computation.FilterListener;

public class DialogFragment_Owe extends androidx.fragment.app.DialogFragment implements FilterListener{
    //the popup that displays when you press owe details on home fragment

    Spinner oweFilterSpinner;
    View rootView;
    RecyclerView oweView;
    ArrayList<Owe> oweArray = new ArrayList<>();

    private RecycleViewAdapter_Owe adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_owe, container, false);
        //show owelist
        oweView = rootView.findViewById(R.id.owe_recycle);
        oweArray = new ArrayList<>();
        adapter = new RecycleViewAdapter_Owe(getActivity(),oweArray);
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
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), oweFilterList,oweFilterSpinner, this);
        oweFilterSpinner.setAdapter(spinnerAdapter);

        //show owelist
        //oweView = rootView.findViewById(R.id.owe_recycle);
        showOweList();
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
        String[] groupList = getResources().getStringArray(R.array.dummy_group_name_list);
        String[] personList = getResources().getStringArray(R.array.dummy_person_name_list);
        String[] amountList = getResources().getStringArray(R.array.dummy_expense_amount_list);

        for (int i = 0; i<groupList.length; i++){
            oweArray.add(new Owe(groupList[i],personList[i],amountList[i]));
            adapter.updateOweArray(oweArray);

        }
    }

    @Override
    public void onFilterSelected(String filterType) {
        Log.e("filter","selected: "+ filterType);
        switch(filterType){
            case "Earliest":
                HeapSortHelper.sortByDateEarliest(oweArray);
                break;
            case "Latest":
                HeapSortHelper.sortByDateLatest(oweArray);
                break;
            case "Amount":
                HeapSortHelper.sortByAmount(oweArray);
                break;
        }
        //requireActivity().runOnUiThread(() ->adapter.notifyDataSetChanged());
        adapter.updateOweArray(oweArray);

    }

}
