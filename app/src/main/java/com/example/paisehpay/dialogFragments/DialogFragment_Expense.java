package com.example.paisehpay.dialogFragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.activities.AddPeople;
import com.example.paisehpay.activities.GroupHomepage;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Expense;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupSelect;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_ItemSelect;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.example.paisehpay.tabBar.ExpenseFragment;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DialogFragment_Expense extends androidx.fragment.app.DialogFragment implements RecycleViewInterface {
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView itemView;
    ArrayList<Item> itemArray = new ArrayList<>();
    RecycleViewAdapter_ItemSelect adapter;
    PreferenceManager pref;
    User CurUser;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_settle_up, container, false);
        itemView = rootView.findViewById(R.id.settle_item_recycle);
        showGroupList();
        pref = new PreferenceManager(getContext());
        CurUser = pref.getUser();

        adapter = new RecycleViewAdapter_ItemSelect(getActivity(),itemArray,this);
        itemView.setAdapter(adapter);
        itemView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }


    //put custom layout
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showGroupList() {
        itemArray.clear();

    }
    @Override
    public void onButtonClick (int position){
        itemArray.get(position).setSelected(true);
        itemView.getAdapter().notifyItemChanged(position);
        String selectedGroupName = itemArray.get(position).getItemName();
        ExpenseFragment expenseFragment = (ExpenseFragment) getParentFragmentManager().findFragmentByTag("Expense_Fragment");
        if (expenseFragment != null) {
            expenseFragment.selectGroup(selectedGroupName);
        }
        new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 500);
    }
}

