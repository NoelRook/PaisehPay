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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.itemAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Item;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Settle;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettleUp extends AppCompatActivity {
    TextView toolbarTitleText;
    ImageView backArrow;
    RecyclerView itemView;
    ArrayList<Item> itemArray = new ArrayList<>();
    Button editExpenseButton;
    Button settleExpenseButton;
    Button deleteExpenseButton;
    //RecycleViewAdapter_Item adapter;

    RecycleViewAdapter_Settle adapter;
    String expenseId;
    ExpenseAdapter expAdapter;
    itemAdapter itemAdapter;

    PreferenceManager preferenceManager;




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
        expenseId = intent.getStringExtra("EXPENSE_ID");
        Log.d("test",expenseId);


        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.settle_expense);
        expAdapter = new ExpenseAdapter();


        //press back arrow lead back to home fragment
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        //press edit expense button needs
        editExpenseButton = findViewById(R.id.edit_expense);
        editExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettleUp.this, ReceiptOverview.class);
                intent.putExtra("Items",itemArray);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();

            }
        });



        //get pref
        preferenceManager = new PreferenceManager(this);


        //press settle all button
        settleExpenseButton = findViewById(R.id.settle_all);
        settleExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemAdapter = new itemAdapter();
                itemAdapter.updateSettledUser(preferenceManager.getUser().getId(), expenseId, new BaseDatabase.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("UpdateSettled", "User debt updated successfully");
                        itemArray.clear();
                        showItemList(expenseId);
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("UpdateSettled", "Error updating user debt: " + error.getMessage());                        }
                });
            }
        });

        //press delete expense button
        deleteExpenseButton = findViewById(R.id.delete_expense);
        deleteExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expAdapter.delete(expenseId, new BaseDatabase.OperationCallback(){
                    @Override
                    public void onSuccess() {
                        // User deleted successfully
                        Log.d("Success", "expense deleted");
                        finish();

                    }

                    @Override
                    public void onError(DatabaseError error) {
                        // Handle error
                        Log.e("FirebaseError", error.getMessage());
                    }
                });
            }
        });


        //show item list
        //we will link the adapter used for the item scroll bar
        itemView = findViewById(R.id.recycle_view_items);
        adapter = new RecycleViewAdapter_Settle(this,itemArray);
        itemView.setAdapter(adapter);
        itemView.setLayoutManager(new LinearLayoutManager(this));

        showItemList(expenseId);
    }


    private void showItemList(String expenseId) {
        itemAdapter itemadapter = new itemAdapter();

        executorService.execute(()->{
            itemadapter.getItemByExpense(expenseId, new BaseDatabase.ListCallback<Item>() {
                @Override
                public void onListLoaded(List<Item> object) {
                    //itemArray.addAll(object);
                   // Log.d("friends", object.toString());

                    // Notify your adapter that data has changed
                    //if (itemadapter != null) {
                    //    adapter.notifyDataSetChanged();
                    //}

                    runOnUiThread(() ->{
                        itemArray.clear();
                        itemArray.addAll(object);
                        adapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        });
    }
}