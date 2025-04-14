package com.example.paisehpay.dialogFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.blueprints.Owe;
import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.computation.DateDebt;
import com.example.paisehpay.computation.DateOwed;
import com.example.paisehpay.computation.DebtCalculator;
import com.example.paisehpay.computation.HeapSortHelper;
import com.example.paisehpay.computation.OwedCalculator;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Owe;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.example.paisehpay.tabBar.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.example.paisehpay.computation.FilterListener;
import com.google.firebase.database.DatabaseError;

public class DialogFragment_Owe extends androidx.fragment.app.DialogFragment implements FilterListener{
    //the popup that displays when you press owe details on home fragment

    Spinner oweFilterSpinner;
    View rootView;
    RecyclerView oweView;
    ArrayList<Owe> oweArray = new ArrayList<>();
    TextView titleText;
    private RecycleViewAdapter_Owe adapter;

    private static final String DATA_TO_QUERY = "data_to_query";

    public static DialogFragment_Owe newInstance(String query_from) {
        DialogFragment_Owe fragment = new DialogFragment_Owe();
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
        //String[] groupList = getResources().getStringArray(R.array.dummy_group_name_list);
        //String[] personList = getResources().getStringArray(R.array.dummy_person_name_list);
        //String[] amountList = getResources().getStringArray(R.array.dummy_expense_amount_list);
        //List<Date> dateList = new ArrayList<>()
        ArrayList<String> personList = new ArrayList<>();
        ArrayList<Date> dateList = new ArrayList<>();
        ArrayList<Double> amountList = new ArrayList<>();

        PreferenceManager preferenceManager = new PreferenceManager(getActivity());
        User currentUser = preferenceManager.getUser();
        if (getArguments().getString(DATA_TO_QUERY).equals("Who do you owe?")){
            DateDebt dateDebt = new DateDebt();
            HashMap<String, Date> dateDebthash = dateDebt.peopleYouOwe(currentUser.getId());
            personList = new ArrayList<>(dateDebthash.keySet());
            dateList = new ArrayList<>(dateDebthash.values());

            DebtCalculator debtcalc = new DebtCalculator(currentUser.getUsername());
            HashMap<String, Double> debthashmap = debtcalc.calculateTotalDebt();
            //amountList = new ArrayList<Double>();
            for (int i=0; i<personList.size(); i++){
                String key = personList.get(i);
                amountList.add(i, debthashmap.get(key));
            }

        }
        else if (getArguments().getString(DATA_TO_QUERY).equals("Who owes you?")){
            Log.d("Who owes you?", "working");
            DateOwed dateOwed = new DateOwed();
            HashMap<String, Date> dateOwedhash = dateOwed.peopleOweYouLatest(currentUser.getId(), new BaseDatabase.DateCallback() {
                @Override
                public void onDateLoaded(HashMap<String, Date> userIdToDateMap) {
                    Log.d("mapping date", userIdToDateMap.toString());
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("mapping date", "error getting owed date data:"+ error.getMessage());
                }
            });
            Log.d("testingHere",dateOwedhash.toString()+"not here");
            personList = new ArrayList<>(dateOwedhash.keySet());
            dateList = new ArrayList<>(dateOwedhash.values());

            OwedCalculator owecalc = new OwedCalculator(currentUser.getId());
            HashMap<String, Double> owedhashmap = owecalc.calculateTotalOwed();
            Log.d("amounttesting", owedhashmap.toString());
            for (int i=0; i<personList.size(); i++){
                String key = personList.get(i);
                amountList.add(i, owedhashmap.get(key));
            }

        }
        else{Log.d("DialogFragOwe", getArguments().getString(DATA_TO_QUERY));}

        String[] groupList = new String[personList.size()];


        for (int i = 0; i<groupList.length; i++){
            oweArray.add(new Owe(groupList[i],personList.get(i),amountList.get(i), dateList.get(i)));
            adapter.updateOweArray(oweArray);

        }
    }

    /*private void showOweList() {
        PreferenceManager preferenceManager = new PreferenceManager(requireActivity());
        User currentUser = preferenceManager.getUser();

        // Clear existing data
        oweArray.clear();

        assert getArguments() != null;
        if (Objects.equals(getArguments().getString(DATA_TO_QUERY), "Who do you owe?")) {
            // Calculate debts (what you owe others)
            DateDebt dateDebt = new DateDebt();
            HashMap<String, Date> dateDebtHash = dateDebt.peopleYouOwe(currentUser.getId());

            DebtCalculator debtCalc = new DebtCalculator(currentUser.getId());
            HashMap<String, Double> debtHashMap = debtCalc.calculateTotalDebt();
            Log.d("debtHashMap",debtHashMap.toString());

            // Add debt entries to the list
            for (String userId : dateDebtHash.keySet()) {
                Date date = dateDebtHash.get(userId);
                Double amount = debtHashMap.get(userId);
                if (amount != null && amount > 0) {
                    oweArray.add(new Owe("", userId, amount, date));
                }
            }

            //titleText.setText("People You Owe");
        }
        else if (Objects.equals(getArguments().getString(DATA_TO_QUERY), "Who owes you?")) {
            // Calculate amounts owed to you
            DateOwed dateOwed = new DateOwed();
            dateOwed.peopleOweYouLatest(currentUser.getId(), new BaseDatabase.DateCallback() {
                @Override
                public void onDateLoaded(HashMap<String, Date> userIdToDateMap) {
                    OwedCalculator oweCalc = new OwedCalculator(currentUser.getId());
                    HashMap<String, Double> owedHashMap = oweCalc.calculateTotalOwed();

                    // Add owed entries to the list
                    for (String userId : userIdToDateMap.keySet()) {
                        Date date = userIdToDateMap.get(userId);
                        Double amount = owedHashMap.get(userId);
                        if (amount != null && amount > 0) {
                            oweArray.add(new Owe("", userId, amount, date));
                        }
                    }

                    // Update UI on main thread
                    requireActivity().runOnUiThread(() -> {
                        adapter.updateOweArray(oweArray);
                        //titleText.setText("People Who Owe You");
                    });
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("DialogFragment_Owe", "Error loading owed data: " + error.getMessage());
                }
            });
        } else {
            Log.d("DialogFragment_Owe", "Unknown query type: " + getArguments().getString(DATA_TO_QUERY));
        }

        // Update the adapter
        adapter.updateOweArray(oweArray);
    }*/

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
