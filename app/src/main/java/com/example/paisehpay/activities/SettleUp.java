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
import com.example.paisehpay.databaseHandler.itemAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Item;
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
    RecycleViewAdapter_Item adapter;
    String groupId;

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


        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.settle_expense);


        //press back arrow lead back to home fragment
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettleUp.this, GroupHomepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });

        //press edit expense button needs
        editExpenseButton = findViewById(R.id.edit_expense);
        editExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettleUp.this, ReceiptOverview.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();

            }
        });


        //press settle all button
        settleExpenseButton = findViewById(R.id.settle_all);
        settleExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Item item: itemArray){
                    item.setSettled(true);
                    //Log.d("SettleUp", "Item: " + item.getItemName() + " selected? " + item.isSelected());
                }
                adapter.notifyDataSetChanged();
            }
        });

        //press delete expense button
        deleteExpenseButton = findViewById(R.id.delete_expense);
        deleteExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //show item list
        //we will link the adapter used for the item scroll bar
        itemView = findViewById(R.id.recycle_view_items);
        adapter = new RecycleViewAdapter_Item(this,itemArray,null, "SettleUp");
        itemView.setAdapter(adapter);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        showItemList(groupId);
    }


    //currently doesn't allow the item to save whether settled or not, need see db implementation
    @Override
    protected void onResume(){
        super.onResume();
        for (Item item : itemArray) {
            Log.d("ResumeState", "Item: " + item.getItemName() + " selected: " + item.isSettled());
        }
    }

    private void showItemList(String groupId) { //need figure out how to call expense item from db
        itemAdapter itemadapter = new itemAdapter();

        executorService.execute(()->{
            itemadapter.getItemByExpense(groupId, new BaseDatabase.ListCallback<Item>() {
                @Override
                public void onListLoaded(List<Item> object) {
                    itemArray.addAll(object);
                    Log.d("friends", object.toString());

                    // Notify your adapter that data has changed
                    if (itemadapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        });




        adapter.notifyDataSetChanged();
    }

}