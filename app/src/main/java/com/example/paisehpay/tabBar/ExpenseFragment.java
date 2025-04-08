package com.example.paisehpay.tabBar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Expense;

import java.util.ArrayList;

public class ExpenseFragment extends Fragment {

    //expense fragment that loads below the horizontal scroll bar on grouphomepage
    private static final String CATEGORY = "category";
    String categoryToLoad;
    View rootView;
    RecyclerView expenseView;
    ArrayList<Expense> expenseArray = new ArrayList<>();
    RecycleViewAdapter_Expense adapter;


    public static ExpenseFragment newInstance(String category) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryToLoad = getArguments().getString(CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        //show expense list
        expenseView = rootView.findViewById(R.id.recycle_view_expense);
        adapter = new RecycleViewAdapter_Expense(getActivity(),expenseArray);
        expenseView.setAdapter(adapter);
        expenseView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showExpenseList();

        return rootView;

    }

    private void showExpenseList() {
        String[] expenseTitleList = getResources().getStringArray(R.array.dummy_expense_title_list);
        String[] expenseDateList = getResources().getStringArray(R.array.dummy_expense_date_list);
        String[] expensePaidByList = getResources().getStringArray(R.array.dummy_expense_paid_by_list);
        String[] expenseActionList = getResources().getStringArray(R.array.dummy_expense_action_list);
        String[] expenseAmountList = getResources().getStringArray(R.array.dummy_expense_amount_list);
        String[] expenseCategoryList = getResources().getStringArray(R.array.category_name_array);


        for (int i = 0; i<expenseCategoryList.length; i++){
            String category = expenseCategoryList[i];
            if (category.equals(categoryToLoad)){
                Expense expense = new Expense(expenseTitleList[i],expenseDateList[i],expensePaidByList[i],expenseActionList[i],expenseAmountList[i],category, null,null);
                expenseArray.add(expense);
            }
        }
        adapter.notifyDataSetChanged();

        //call from db all expense items
    }

    // <!-- TODO: 1. when user clicks horizontal tab bar with categories, pass category name data into this fragment-->
    // <!-- TODO: 2. queries data from database with all expenses for the category selected and update showExpenseList() based on the data queried  -->
}