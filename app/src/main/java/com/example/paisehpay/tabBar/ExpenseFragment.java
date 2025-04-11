// ExpenseFragment.java - Modified to handle data better
package com.example.paisehpay.tabBar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.ExpenseSingleton;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Expense;

import java.util.ArrayList;

public class ExpenseFragment extends Fragment {
    private static final String CATEGORY = "category";
    private static final String GROUP_ID = "group_id";

    private String categoryToLoad;
    private String groupId;
    private RecyclerView expenseView;
    private RecycleViewAdapter_Expense adapter;
    private ExpenseSingleton expenseSaver;

    public static ExpenseFragment newInstance(String category, String groupId) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        args.putString(GROUP_ID, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryToLoad = getArguments().getString(CATEGORY);
            groupId = getArguments().getString(GROUP_ID);
        }
        expenseSaver = ExpenseSingleton.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);
        expenseView = rootView.findViewById(R.id.recycle_view_expense);

        // Initialize with empty list, will be updated in onResume
        adapter = new RecycleViewAdapter_Expense(getActivity(), new ArrayList<>());
        expenseView.setAdapter(adapter);
        expenseView.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseView.setHasFixedSize(true);
        updateExpenseList();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateExpenseList();
    }

    private void updateExpenseList() {
        ArrayList<Expense> filteredExpenses = new ArrayList<>();
        ArrayList<Expense> allExpenses = expenseSaver.getExpenseArrayList();
        Log.d("expense",allExpenses.toString());

        if (allExpenses == null) {
            allExpenses = new ArrayList<>(); // Ensure we don't get NPE
        }

        if (categoryToLoad.equals("Overall")) {
            filteredExpenses.addAll(allExpenses);
        } else {
            for (Expense expense : allExpenses) {
                String expenseCategory = expense.getExpenseCategory();
                Log.d("category", expenseCategory + categoryToLoad);
                // Handle null category - you might want to skip or include these
                if (expenseCategory != null && expenseCategory.equalsIgnoreCase(categoryToLoad)) {
                    filteredExpenses.add(expense);
                }
            }
        }
        adapter.updateData(filteredExpenses);
        adapter.notifyDataSetChanged();
    }


}