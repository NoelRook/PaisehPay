package com.example.paisehpay.activities;

import android.app.DialogFragment;
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
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.dialogFragments.DialogFragment_AddItem;
import com.example.paisehpay.dialogFragments.DialogFragment_SelectGroup;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Item;

import java.util.ArrayList;

public class ReceiptOverview extends AppCompatActivity implements DialogFragmentListener<Item> {
    //shows receipt details after camera ocr
    ImageView backArrow;
    Button addPeopleButton;
    TextView toolbarTitleText;
    RecyclerView itemView;
    ArrayList<Item> itemArray = new ArrayList<>();
    Button addItemButton;
    DialogFragment_SelectGroup selectGroupFragment;
    DialogFragment_AddItem addItemFragment;
    private RecycleViewAdapter_Item adapter_items;

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

        //press add button add new item to reycleview
        addItemButton = findViewById(R.id.add_item);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemFragment = new DialogFragment_AddItem();
                addItemFragment.show(getSupportFragmentManager(),"DialogFragment_AddItem");

            }
        });

        //show item list
        itemView = findViewById(R.id.recycle_view_items);
        adapter_items = new RecycleViewAdapter_Item(this,itemArray,null);
        itemView.setAdapter(adapter_items);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        showItemList();

    }

    private void showItemList() {
        //dummy data
        //String[] nameList = getResources().getStringArray(R.array.dummy_item_name_list);
        //String[] priceList = getResources().getStringArray(R.array.dummy_expense_amount_list);

        //for (int i = 0; i<nameList.length; i++){
        //    itemArray.add(new Item(nameList[i],priceList[i],"add people to item"));
        //}
        adapter_items.notifyDataSetChanged();
    }

    @Override
    public void onDataSelected(int position, Item data) {
        itemArray.add(data);
        adapter_items.notifyItemInserted(itemArray.size() - 1);

    }


    // <!-- TODO: 2. do ocr json magic, output a list of all items and do math  -->
}