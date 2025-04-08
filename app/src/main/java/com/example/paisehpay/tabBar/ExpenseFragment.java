package com.example.paisehpay.tabBar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Expense;

import java.util.ArrayList;

public class ExpenseFragment extends Fragment {

    private static final String CATEGORY = "category";
    private String categoryToLoad;
    private RecyclerView expenseView;
    private ArrayList<Expense> expenseArray = new ArrayList<>();
    private RecycleViewAdapter_Expense adapter;

    public static ExpenseFragment newInstance(String category) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);  // Pass category to the fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryToLoad = getArguments().getString(CATEGORY);  // Retrieve category
        }
        Log.d("Expense",categoryToLoad);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);
        expenseView = rootView.findViewById(R.id.recycle_view_expense);
        showExpenseList();
        Log.d("ExpenseDebug", "Loaded expense items: " + expenseArray.size());
        expenseArray.add(new Expense("Test title", "Today", "Iris", "You paid", "$123", "Food", null, null));
        adapter = new RecycleViewAdapter_Expense(getActivity(), expenseArray);
        expenseView.setAdapter(adapter);
        expenseView.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseView.scrollToPosition(0);

        // Load data when fragment is created
        adapter.notifyDataSetChanged();
        return rootView;
    }

    // Load data based on category selection
    private void showExpenseList() {
        // Dummy data arrays (use actual data in production)
        String[] expenseTitleList = getResources().getStringArray(R.array.dummy_expense_title_list);
        String[] expenseDateList = getResources().getStringArray(R.array.dummy_expense_date_list);
        String[] expensePaidByList = getResources().getStringArray(R.array.dummy_expense_paid_by_list);
        String[] expenseActionList = getResources().getStringArray(R.array.dummy_expense_action_list);
        String[] expenseAmountList = getResources().getStringArray(R.array.dummy_expense_amount_list);
        String[] expenseCategoryList = getResources().getStringArray(R.array.category_name_array);

        for (int i =0; i<expenseCategoryList.length;i++){
            if (expenseCategoryList[i].equals(categoryToLoad)){
                Log.d("CategoryCompare", "Comparing " + expenseCategoryList[i] + " with " + categoryToLoad);
                Expense expense = new Expense(expenseTitleList[i],expenseDateList[i],expensePaidByList[i],expenseActionList[i],expenseAmountList[i],categoryToLoad,null,null);
                Log.d("Expense",expense.getDescription()+expense.getExpenseCategory());
                expenseArray.add(expense);
            } else if (categoryToLoad.equals("Overall")){
                Expense expense = new Expense(expenseTitleList[i],expenseDateList[i],expensePaidByList[i],expenseActionList[i],expenseAmountList[i],expenseCategoryList[i],null,null);
                Log.d("Expense",expense.getDescription()+expense.getExpenseCategory());
                expenseArray.add(expense);
            }
        }

    }
}
