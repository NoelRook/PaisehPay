package com.example.paisehpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.ExpenseSingleton;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.itemAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Expense;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_ExpenseDescription;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettleUp extends AppCompatActivity {
    TextView toolbarTitleText;
    ImageView backArrow;
    RecyclerView expenseView;
    ArrayList<Expense> expenseArray = new ArrayList<>();
    Button settleExpenseButton;
    RecycleViewAdapter_Expense adapter;
    ExpenseSingleton singleton;
    String groupId;
    String friendId;
    itemAdapter itemAdapter;
    ArrayList<Expense> filteredArray = new ArrayList<>();
    PreferenceManager preferenceManager;
    ConstraintLayout totalLayout;
    TextView overallPriceText;
    TextView overallPersonText;



    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settle_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        groupId = intent.getStringExtra("GROUP_ID");
        friendId = intent.getStringExtra("FRIEND_ID");
        preferenceManager = new PreferenceManager(this);


        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);

        //if (expense_is_settled){
            toolbarTitleText.setText(R.string.all_settled);
        //}else{
        //    toolbarTitleText.setText(R.string.settle_expense);
        //}


        //press back arrow lead back to home fragment
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });


        settleExpenseButton = findViewById(R.id.settle_up);
        settleExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        singleton = ExpenseSingleton.getInstance();
        itemAdapter = new itemAdapter();


        //show item list
        //we will link the adapter used for the item scroll bar
        expenseView = findViewById(R.id.recycle_view_items);
        adapter = new RecycleViewAdapter_Expense(this,expenseArray);
        showExpenseList();
        expenseView.setAdapter(adapter);
        expenseView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void showExpenseList() {
        filteredArray.clear();
        String yourID = preferenceManager.getUser().getId();

        for (Expense expense : singleton.getExpenseArrayList()) {
            boolean paidByYou = expense.getExpensePaidBy().equals(yourID);
            boolean paidByFriend = expense.getExpensePaidBy().equals(friendId);

            if (paidByYou) {
                itemAdapter.getTotalAmountOwedByUser(friendId, expense.getExpenseId(), new BaseDatabase.ValueCallback<Double>() {
                    @Override
                    public void onValueLoaded(Double value) {
                        if (value > 0) {
                            filteredArray.add(expense);
                            adapter.updateData(filteredArray);
                            adapter.notifyDataSetChanged();
                            Log.d("AmountOwed", "Friend: " + friendId + " owes " + value + " in expense " + expense.getExpenseId());
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("FriendOwesYou", "Error: " + error.getMessage());
                    }
                });
            } else if (paidByFriend) {
                itemAdapter.getTotalAmountOwedByUser(yourID, expense.getExpenseId(), new BaseDatabase.ValueCallback<Double>() {
                    @Override
                    public void onValueLoaded(Double value) {
                        if (value > 0) {
                            filteredArray.add(expense);
                            adapter.updateData(filteredArray);
                            adapter.notifyDataSetChanged();
                            Log.d("uowefriend", "you owe friend: " + value);
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("uowefriend", "Error: " + error.getMessage());
                    }
                });
            }
        }
    }

}