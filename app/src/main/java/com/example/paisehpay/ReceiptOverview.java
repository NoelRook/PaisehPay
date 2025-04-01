package com.example.paisehpay;

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

import java.util.ArrayList;

public class ReceiptOverview extends AppCompatActivity {
    //shows receipt details after camera ocr
    ImageView backArrow;
    Button addPeopleButton;
    TextView toolbarTitleText;
    RecyclerView receiptView;
    ArrayList<Receipt> receiptArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receipt_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.receipt_overview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //press add button to add people layout
        addPeopleButton = findViewById(R.id.add_to_split);
        addPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open SecondActivity
                Intent intent = new Intent(ReceiptOverview.this, AddPeople.class);
                startActivity(intent);
            }
        });

        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.receipt_details);
        //press back arrow lead back to home fragment

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceiptOverview.this, MainActivity.class);
                intent.putExtra("fragmentToLoad","HomeFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });

        //show item list
        receiptView = findViewById(R.id.recycle_view_receipt);
        showReceiptList();
        RecycleViewAdapter_Receipt adapter = new RecycleViewAdapter_Receipt(this,receiptArray);
        receiptView.setAdapter(adapter);
        receiptView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void showReceiptList() {
        String[] numberList = getResources().getStringArray(R.array.dummy_item_number_list);
        String[] nameList = getResources().getStringArray(R.array.dummy_item_name_list);
        String[] amountList = getResources().getStringArray(R.array.dummy_expense_amount_list);


        for (int i = 0; i<nameList.length; i++){
            receiptArray.add(new Receipt(numberList[i],nameList[i],amountList[i]));

        }
    }
    // <!-- TODO: 2. do ocr json magic, output a list of all items and do math  -->
}