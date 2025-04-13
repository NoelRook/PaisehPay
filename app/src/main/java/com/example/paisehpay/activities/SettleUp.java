package com.example.paisehpay.activities;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
    AtomicReference<Double> totalAmount = new AtomicReference<>(0.0);



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
        singleton = ExpenseSingleton.getInstance();
        itemAdapter = new itemAdapter();


        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.expenses);
        totalLayout = findViewById(R.id.total_layout);
        overallPriceText = totalLayout.findViewById(R.id.final_amount);
        overallPersonText = totalLayout.findViewById(R.id.to_person);


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
                settleAllExpensesForFriend(friendId);
            }
        });


        //show item list
        //we will link the adapter used for the item scroll bar
        expenseView = findViewById(R.id.recycle_view_items);
        adapter = new RecycleViewAdapter_Expense(this,filteredArray,"SettleUp",null);
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
                            expense.setExpenseOwed(value);
                            double updatedTotal = totalAmount.get() + value;
                            totalAmount.set(updatedTotal);
                            updateTotalDisplay(updatedTotal);
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
                            expense.setExpenseOwed(value);
                            double updatedTotal = totalAmount.get() - value;
                            totalAmount.set(updatedTotal);
                            updateTotalDisplay(updatedTotal);
                            adapter.notifyDataSetChanged();
                            Log.d("AmountOwed", "you owe friend: " + value);
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("AmountOwed", "Error: " + error.getMessage());
                    }
                });
            }                          

        }
        double finalAmount = totalAmount.get();
        if (finalAmount == 0.0){
            overallPersonText.setVisibility(GONE);
        } else if (finalAmount > 0.0){
            overallPersonText.setText("To " + preferenceManager.getUser().getUsername());
            BigDecimal bigDecimal = new BigDecimal(finalAmount).setScale(2, RoundingMode.HALF_UP);
            overallPriceText.setText(String.format(Locale.ENGLISH,"$ %s",bigDecimal));
        } else if (finalAmount < 0.0){
            overallPersonText.setText(R.string.to_friend);
            finalAmount = -finalAmount;
            BigDecimal bigDecimal = new BigDecimal(finalAmount).setScale(2, RoundingMode.HALF_UP);
            overallPriceText.setText(String.format(Locale.ENGLISH,"$ %s",bigDecimal));
        }
    }

    private void updateTotalDisplay(double amount) {
        if (amount == 0.0){
            overallPersonText.setVisibility(GONE);
        } else {
            overallPersonText.setVisibility(View.VISIBLE);
            BigDecimal bigDecimal = BigDecimal.valueOf(Math.abs(amount)).setScale(2, RoundingMode.HALF_UP);
            overallPriceText.setText(String.format(Locale.ENGLISH,"$ %s", bigDecimal));

            if (amount > 0.0){
                overallPersonText.setText("To " + preferenceManager.getUser().getUsername());
            } else {
                overallPersonText.setText(R.string.to_friend);
            }
        }
        adapter.notifyDataSetChanged();

    }

    private void settleAllExpensesForFriend(String friendId) {
        String myId = preferenceManager.getUser().getId();
        AtomicInteger updatesCompleted = new AtomicInteger(0);
        List<Expense> expensesToSettle = new ArrayList<>();

        for (Expense expense : singleton.getExpenseArrayList()) {
            if (expense.getExpensePaidBy().equals(myId)) {
                itemAdapter.getTotalAmountOwedByUser(friendId, expense.getExpenseId(), new BaseDatabase.ValueCallback<Double>() {
                    @Override
                    public void onValueLoaded(Double value) {
                        if (value > 0) {
                            synchronized (expensesToSettle) {
                                expensesToSettle.add(expense);
                            }

                            itemAdapter.updateSettledUser(myId, friendId, expense.getExpenseId(), new BaseDatabase.OperationCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.d("SettleAll", "Settled expense: " + expense.getExpenseId());
                                    checkIfAllUpdatesDone(expensesToSettle, updatesCompleted);
                                }

                                @Override
                                public void onError(DatabaseError error) {
                                    Log.e("SettleAll", "Failed for " + expense.getExpenseId() + ": " + error.getMessage());
                                    checkIfAllUpdatesDone(expensesToSettle, updatesCompleted);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("SettleAll", "Error checking total owed: " + error.getMessage());
                    }
                });
            } else if (expense.getExpensePaidBy().equals(friendId)) {
                itemAdapter.getTotalAmountOwedByUser(myId, expense.getExpenseId(), new BaseDatabase.ValueCallback<Double>() {
                    @Override
                    public void onValueLoaded(Double value) {
                        if (value > 0) {
                            synchronized (expensesToSettle) {
                                expensesToSettle.add(expense);
                            }

                            itemAdapter.updateSettledUser(friendId, myId, expense.getExpenseId(), new BaseDatabase.OperationCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.d("SettleAll", "Settled expense: " + expense.getExpenseId());
                                    checkIfAllUpdatesDone(expensesToSettle, updatesCompleted);
                                }

                                @Override
                                public void onError(DatabaseError error) {
                                    Log.e("SettleAll", "Failed for " + expense.getExpenseId() + ": " + error.getMessage());
                                    checkIfAllUpdatesDone(expensesToSettle, updatesCompleted);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("SettleAll", "Error checking total owed: " + error.getMessage());
                    }
                });
            }
        }
    }

    private void checkIfAllUpdatesDone(List<Expense> expensesToSettle, AtomicInteger updatesCompleted) {
        int done = updatesCompleted.incrementAndGet();
        if (done == expensesToSettle.size()) {
            runOnUiThread(() -> {
                Log.d("SettleAll", "All settlements done");
                showExpenseList();                      // Refresh data
                expenseView.setVisibility(View.GONE);
                settleExpenseButton.setVisibility(View.GONE);// Hide RecyclerView
                toolbarTitleText.setText(R.string.all_settled);
            });
        }
    }






}