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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);
        expenseView = rootView.findViewById(R.id.recycle_view_expense);
        adapter = new RecycleViewAdapter_Expense(getActivity(), expenseArray);
        expenseView.setAdapter(adapter);
        expenseView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load data when fragment is created
        showExpenseList();
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

        // Clear previous data before filtering
        expenseArray.clear();

        Log.d("ExpenseFragment", "Category to load: " + categoryToLoad);  // Log category being loaded

        // If "Overall" category is selected, show all expenses
        if ("Overall".equals(categoryToLoad)) {
            for (int i = 0; i < expenseCategoryList.length; i++) {
                expenseArray.add(new Expense(expenseTitleList[i], expenseDateList[i], expensePaidByList[i], expenseActionList[i], expenseAmountList[i], expenseCategoryList[i], null, null));
            }
        } else {
            // Filter expenses based on the selected category
            for (int i = 0; i < expenseCategoryList.length; i++) {
                if (expenseCategoryList[i].equals(categoryToLoad)) {
                    Log.d("ExpenseFragment", "Filtering expense: " + expenseTitleList[i]); // Log filtered expense
                    expenseArray.add(new Expense(expenseTitleList[i], expenseDateList[i], expensePaidByList[i], expenseActionList[i], expenseAmountList[i], expenseCategoryList[i], null, null));
                }
            }
        }
        Log.d("ExpenseFragment", "Filtered expenses count: " + expenseArray.size());  // Log how many expenses were filtered

        // Log adapter data just before notifying it
        Log.d("ExpenseFragment", "Adapter data updated, notifying adapter...");

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();

    }
}
