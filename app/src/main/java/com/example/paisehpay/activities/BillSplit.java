package com.example.paisehpay.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Summary;
import com.example.paisehpay.blueprints.Summary;

import java.util.ArrayList;

public class BillSplit extends AppCompatActivity {
    //page seen after u split the bill

    TextView toolbarTitleText;
    ImageView backArrow;
    RecyclerView summaryView;
    ArrayList<Summary> summaryArray = new ArrayList<>();
    Button homeButton;
    Expense expense;
    ArrayList<Item> itemArray;
    TextView expenseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_split);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //catch the intent from AddPeople.java
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Expense")){
            expense = intent.getParcelableExtra("Expense");
        }

        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.expense_details);

        expenseName = findViewById(R.id.expense_name);
        expenseName.setText(expense.getDescription());

        //press back arrow lead back to home fragment
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillSplit.this, AddPeople.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });

        //press home button lead back to main activity with toolbar
        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillSplit.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });

        //show the RecycleView of all who owe u
        summaryView = findViewById(R.id.summary_recycle_view);
        showSummaryList();
        RecycleViewAdapter_Summary adapter = new RecycleViewAdapter_Summary(this,summaryArray);
        summaryView.setAdapter(adapter);
        summaryView.setLayoutManager(new LinearLayoutManager(this));
    }

    //will be changed later
    private void showSummaryList() {
        itemArray = expense.getExpenseItems();


        for (Item item: itemArray){
            for (String user: item.getItemPeopleArray()){
                summaryArray.add(new Summary(String.format("%s owes you %s for %s",user,item.getItemIndividualPrice(),item.getItemName())));
            }
        }
    }
}