package com.example.paisehpay.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.paisehpay.dialogFragments.DialogFragment_SelectGroup;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.dialogFragments.DialogFragment_AddItem;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;
import com.example.paisehpay.blueprints.Category;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.R;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Category;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddPeople extends AppCompatActivity implements RecycleViewInterface, DialogFragmentListener<Item> {
    //ie the expense overview page
    RecyclerView categoryView;
    ArrayList<Category> categoryArray = new ArrayList<>();

    RecyclerView itemView;
    ArrayList<Item> itemArray = new ArrayList<>();

    TextView toolbarTitleText;
    ImageView backArrow;
    ImageButton selectGroupButton;
    DialogFragment_SelectGroup selectGroupFragment;
    Button splitBillButton;
    TextView selectedGroupText;
    private RecycleViewAdapter_Item adapter_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_people);
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
                Intent intent = new Intent(AddPeople.this, ReceiptOverview.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });


        //select group button instantiates popup
        selectGroupButton = findViewById(R.id.select_group_button);
        selectGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGroupFragment = DialogFragment_SelectGroup.newInstance(0,null);
                selectGroupFragment.show(getSupportFragmentManager(), "DialogFragment_SelectGroup");

            }
        });

        //click confirm button to finalise bill and lead to split bill page
        splitBillButton = findViewById(R.id.confirm_button);
        splitBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPeople.this, BillSplit.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });


        //show category list
        categoryView = findViewById(R.id.recycle_view_category);
        showCategoryList();
        RecycleViewAdapter_Category adapter_category = new RecycleViewAdapter_Category(this,categoryArray);
        categoryView.setAdapter(adapter_category);
        categoryView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //show item list
        itemView = findViewById(R.id.recycle_view_items);
        adapter_items = new RecycleViewAdapter_Item(this,itemArray,this);
        itemView.setAdapter(adapter_items);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        showItemList();

    }



    //populate item reycleview
    private void showItemList() {
        //dummy data
        String[] nameList = getResources().getStringArray(R.array.dummy_item_name_list);
        String[] priceList = getResources().getStringArray(R.array.dummy_expense_amount_list);

        for (int i = 0; i<nameList.length; i++){
            itemArray.add(new Item(nameList[i],priceList[i],"add people to item"));
        }
        adapter_items.notifyDataSetChanged();
    }


    //populates category recycleview
    private void showCategoryList() {
        String[] nameList = getResources().getStringArray(R.array.category_name_array);
        TypedArray imageArray = getResources().obtainTypedArray(R.array.category_item_array);

        for (int i = 0; i < nameList.length; i++) {
            int imageResId = imageArray.getResourceId(i, 0);

            Log.d("CategoryList", "Category: " + nameList[i] + " → Image ID: " + imageResId);

            categoryArray.add(new Category(nameList[i], imageResId));
        }

        imageArray.recycle();
        }



    //for the recycleview objects
    @Override
    public void onButtonClick(int position) {
        selectGroupFragment = DialogFragment_SelectGroup.newInstance(1,position);
        selectGroupFragment.show(getSupportFragmentManager(), "DialogFragment_CreateGroup");

    }

    @Override
    public void onDataSelected(int position, Item data) {

        if (position>=0){
            String selectedNames = data.getItemPeople();
            itemArray.get(position).setItemPeople(selectedNames);
            adapter_items.notifyItemChanged(position);
        } //else {
            //itemArray.add(data);
            //adapter_items.notifyItemInserted(itemArray.size() - 1);
        //}

    }


    public void selectGroup(String groupName){
        selectedGroupText = findViewById(R.id.group_selected);
        selectedGroupText.setText(groupName);

    }

    //general
    // <!-- TODO: 4. store expense name, category, group, and item info in db after pressing confirm button  -->
    // <!-- TODO: 8. query item name, price from db -->


    //if manual input
}