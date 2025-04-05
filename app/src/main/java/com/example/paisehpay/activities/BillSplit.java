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

        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.expense_details);

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
        String[] nameList = getResources().getStringArray(R.array.dummy_person_name_list); //can hover to see what dummy data it using
        String[] amountList = getResources().getStringArray(R.array.dummy_expense_amount_list);
        String[] itemList = getResources().getStringArray(R.array.dummy_item_name_list);


        for (int i = 0; i<nameList.length; i++){
            summaryArray.add(new Summary(String.format("%s owes you %s for %s",nameList[i],amountList[i],itemList[i])));

        }
    }

    // <!-- TODO: 1. do math to figure out who owes you -->
    // <!-- TODO: 2. assign expense data to various users in db  -->
    // <!-- TODO: 3. replace data from db, call avatar of user that owes you -->
    // <!-- TODO: 4. reformat string with user data  -->
}