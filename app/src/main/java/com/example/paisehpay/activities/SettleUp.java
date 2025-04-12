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
import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.ExpenseSingleton;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.itemAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_ExpenseDescription;
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

    Button settleExpenseButton;

    RecycleViewAdapter_ExpenseDescription adapter;
    String expenseId;
    ExpenseAdapter expAdapter;
    itemAdapter itemAdapter;
    PreferenceManager preferenceManager;
    private ExpenseSingleton expenseSaver;

    String groupId;
    String friendId;




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
        String groupId = intent.getStringExtra("GROUP_ID");
        String friendId = intent.getStringExtra("FRIEND_ID");


        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);

        //if (expense_is_settled){
            toolbarTitleText.setText(R.string.all_settled);
        //}else{
        //    toolbarTitleText.setText(R.string.settle_expense);
        //}




        //adapter
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


        //get pref
        preferenceManager = new PreferenceManager(this);
        expenseSaver = ExpenseSingleton.getInstance();

        settleExpenseButton = findViewById(R.id.settle_up);
        settleExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //show item list
        //we will link the adapter used for the item scroll bar
        itemView = findViewById(R.id.recycle_view_items);
        adapter = new RecycleViewAdapter_ExpenseDescription(this,itemArray);
        itemView.setAdapter(adapter);
        itemView.setLayoutManager(new LinearLayoutManager(this));

        showItemList(friendId);
    }


    private void showItemList(String friendId) {

    }
}