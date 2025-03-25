package com.example.paisehpay_iris;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ExpenseFragment extends Fragment {

    //expense fragment that loads below the horizontal scroll bar on grouphomepage
    private static final String c = "category";
    private String categoryToLoad;
    View rootView;
    RecyclerView expenseView;
    ArrayList<Expense> expenseArray = new ArrayList<>();


    public static ExpenseFragment newInstance(String category) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(c, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryToLoad = getArguments().getString(c);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        //show expense list
        expenseView = rootView.findViewById(R.id.recycle_view_expense);
        showExpenseList();
        RecycleViewAdapter_Expense adapter = new RecycleViewAdapter_Expense(getActivity(),expenseArray);
        expenseView.setAdapter(adapter);
        expenseView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;

    }

    private void showExpenseList() {
        String[] expenseTitleList = getResources().getStringArray(R.array.dummy_expense_title_list);
        String[] expenseDateList = getResources().getStringArray(R.array.dummy_expense_date_list);
        String[] expensePaidByList = getResources().getStringArray(R.array.dummy_expense_paid_by_list);
        String[] expenseActionList = getResources().getStringArray(R.array.dummy_expense_action_list);
        String[] expenseAmountList = getResources().getStringArray(R.array.dummy_expense_amount_list);

        for (int i = 0; i<expenseAmountList.length; i++){
            expenseArray.add(new Expense(expenseTitleList[i],expenseDateList[i],expensePaidByList[i],expenseActionList[i],expenseAmountList[i]));

        }
    }

    // <!-- TODO: 1. when user clicks horizontal tab bar with categories, pass category name data into this fragment-->
    // <!-- TODO: 2. queries data from database with all expenses for the category selected and update showExpenseList() based on the data queried  -->
}