package com.example.paisehpay.dialogFragments;

import android.content.Context;
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
import com.example.paisehpay.computation.FilterListener;
import com.example.paisehpay.computation.HeapSortHelper;
import com.example.paisehpay.computation.OwedCalculator;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.mainActivityFragments.HomeFragment;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Owe;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.example.paisehpay.tabBar.SpinnerAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DialogFragment_Owe extends androidx.fragment.app.DialogFragment implements FilterListener {
    private Spinner oweFilterSpinner;
    private View rootView;
    private RecyclerView oweView;
    private ArrayList<Owe> oweArray = new ArrayList<>();
    private RecycleViewAdapter_Owe adapter;
    //private TextView loadingText;

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
        rootView = inflater.inflate(R.layout.fragment_owe, container, false);

        // Initialize views
        oweView = rootView.findViewById(R.id.owe_recycle);
        oweFilterSpinner = rootView.findViewById(R.id.owe_filter_spinner);
        //loadingText = rootView.findViewById(R.id.loading_text);

        // Setup RecyclerView
        oweArray = new ArrayList<>();
        adapter = new RecycleViewAdapter_Owe(getActivity(), oweArray);
        oweView.setLayoutManager(new LinearLayoutManager(getActivity()));
        oweView.setAdapter(adapter);

        // Setup spinner
        setupSpinner();

        // Load data
        loadOweData();

        return rootView;
    }

    private void setupSpinner() {
        ArrayList<String> oweFilterList = new ArrayList<>();
        oweFilterList.add(getString(R.string.earliest));
        oweFilterList.add(getString(R.string.latest));
        oweFilterList.add(getString(R.string.amount));
        oweFilterList.add(getString(R.string.home));

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), oweFilterList,oweFilterSpinner, this);
        oweFilterSpinner.setAdapter(spinnerAdapter);

        oweFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String filter = adapterView.getItemAtPosition(position).toString();
                onFilterSelected(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void loadOweData() {
        //loadingText.setVisibility(View.VISIBLE);
        oweView.setVisibility(View.GONE);

        PreferenceManager preferenceManager = new PreferenceManager(getActivity());
        User currentUser = preferenceManager.getUser();
        String queryType = getArguments().getString(DATA_TO_QUERY);

        if ("Who do you owe?".equals(queryType)) {
            loadDebtData(currentUser);
        } else if ("Who owes you?".equals(queryType)) {
            loadOwedData(currentUser);
        } else {
            Log.e("DialogFragment_Owe", "Unknown query type: " + queryType);
            //loadingText.setVisibility(View.GONE);
        }
    }

    private void loadDebtData(User currentUser) {
        DateDebt dateDebt = new DateDebt();
        dateDebt.peopleYouOwe(currentUser.getId(), new BaseDatabase.DateCallback(){
            @Override
            public void onDateLoaded(HashMap<String, Date> dateMap) {
                DebtCalculator debtCalc = new DebtCalculator(currentUser.getId());
                debtCalc.calculateTotalDebt(new BaseDatabase.DebtCallback() {
                    @Override
                    public void onDebtCalculated(HashMap<String, Double> debtMap) {
                        mergeAndDisplayData(dateMap, debtMap);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        handleError(error);
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private void loadOwedData(User currentUser) {
        DateOwed dateOwed = new DateOwed();
        dateOwed.peopleOweYouLatest(currentUser.getId(), new BaseDatabase.DateCallback() {
            @Override
            public void onDateLoaded(HashMap<String, Date> dateMap) {
                OwedCalculator oweCalc = new OwedCalculator(currentUser.getId());
                oweCalc.calculateTotalOwed(new BaseDatabase.OwedCallback() {
                    @Override
                    public void onOwedCalculated(HashMap<String, Double> owedMap) {
                        mergeAndDisplayData(dateMap, owedMap);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        handleError(error);
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                handleError(error);
            }
        });
    }

    private void mergeAndDisplayData(HashMap<String, Date> dateMap, HashMap<String, Double> amountMap) {
        oweArray.clear();
        PreferenceManager pref = new PreferenceManager(requireActivity());
        // Merge data from both maps
        for (Map.Entry<String, Date> entry : dateMap.entrySet()) {
            String userId = entry.getKey();
            Date date = entry.getValue();
            Double amount = amountMap.get(userId);


            if (amount != null && amount > 0 && !Objects.equals(userId, pref.getUser().getId())) {
                oweArray.add(new Owe("", pref.getOneFriend(userId), amount, date));
            }
        }

        // Update UI on main thread
        requireActivity().runOnUiThread(() -> {
            adapter.updateOweArray(oweArray);
            //loadingText.setVisibility(View.GONE);
            oweView.setVisibility(View.VISIBLE);
        });
    }

    private void handleError(DatabaseError error) {
        Log.e("DialogFragment_Owe", "Error: " + error.getMessage(), error.toException());
        requireActivity().runOnUiThread(() -> {
            //loadingText.setText("Error loading data. Please try again.");
        });
    }

    @Override
    public void onFilterSelected(String filterType) {
        Log.d("Filter", "Selected: " + filterType);

        switch(filterType) {
            case "Earliest":
                HeapSortHelper.sortByDateEarliest(oweArray);
                break;
            case "Latest":
                HeapSortHelper.sortByDateLatest(oweArray);
                break;
            case "Amount":
                HeapSortHelper.sortByAmount(oweArray);
                break;
            case "Home":
                // Default sorting
                break;
        }

        adapter.updateOweArray(oweArray);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}