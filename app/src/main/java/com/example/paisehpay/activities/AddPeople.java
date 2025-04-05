package com.example.paisehpay.activities;

import static android.util.Log.d;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
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


import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.computation.ReceiptInstance;
import com.example.paisehpay.computation.Receipts;
import com.example.paisehpay.dialogFragments.DialogFragment_SelectGroup;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.R;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Category;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Item;

import java.util.ArrayList;

//add people is the expense overview page
public class AddPeople extends AppCompatActivity implements RecycleViewInterface, DialogFragmentListener<Item> {
    RecyclerView categoryView;
    ArrayList<Expense> categoryArray = new ArrayList<>();

    RecyclerView itemView;
    ArrayList<Item> itemArray = new ArrayList<>();
    TextView toolbarTitleText;
    ImageView backArrow;
    ImageButton selectGroupButton;
    DialogFragment_SelectGroup selectGroupFragment;
    Button splitBillButton;
    TextView selectedGroupText;
    RecycleViewAdapter_Item adapter_items;
    Receipts instance;


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


        //select group button instantiates popup, which opens a DialogFragment_SelectGroup
        //since we are reusing it multiple times and populating with different data depending on where we open it,
        //we pass the query_from = 0(meaning selectGroupButton) and selectGroupButton is not in a recycleview, pos = null
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


        //show category list.
        //we will link the adapter used for the category scroll bar
        categoryView = findViewById(R.id.recycle_view_category);
        showCategoryList();
        RecycleViewAdapter_Category adapter_category = new RecycleViewAdapter_Category(this,categoryArray);
        categoryView.setAdapter(adapter_category);
        categoryView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //show item list
        //we will link the adapter used for the item scroll bar
        itemView = findViewById(R.id.recycle_view_items);
        adapter_items = new RecycleViewAdapter_Item(this,itemArray,this);
        itemView.setAdapter(adapter_items);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        showItemList();

    }



    //populate item RecycleView
    //ReceiptInstance(of Receipt) stores the user's items entered in the ReceiptOverview page, then since using Singleton design pattern,
    //we can call the instance's items and prices, which we would populate the RecycleView
    //once we add a item, we need tell the adapter that our dataset changed
    private void showItemList() {
        instance = ReceiptInstance.getInstance();
        ArrayList<String> itemNameArray = instance.getArray_of_items();
        ArrayList<Double> itemPriceArray = instance.getArray_of_item_prices();

        for (int i = 0; i<itemPriceArray.size();i++){
            itemArray.add(new Item(null,itemNameArray.get(i),itemPriceArray.get(i),"Add people to item"));
        }
        adapter_items.notifyDataSetChanged();
    }


    //populates category RecycleView
    //we will get the category names and icons from the arrays.xml
    //since expense has categories, we only care about the category name and icon so we set the rest to null
    private void showCategoryList() {
        String[] nameList = getResources().getStringArray(R.array.category_name_array);
        TypedArray imageArray = getResources().obtainTypedArray(R.array.category_item_array);

        for (int i = 0; i < nameList.length; i++) {
            int imageResId = imageArray.getResourceId(i, 0);


            categoryArray.add(new Expense(null, null, null, null, null, nameList[i], imageResId,null));
        }

        imageArray.recycle();
        }



    //used to open up the DialogFragment_SelectGroup when user clicks on an item in the RecycleView
    //it will display out all the available users from the group to assign to items
    //since we are querying from the RecycleView, we need its position, and "1" to differentiate it from populating from SelectGroupButton
    //we are overriding the RecycleViewInterface.onButtonClick method, which allows us to map that particular postion on the RecycleView to a DialogFragment
    @Override
    public void onButtonClick(int position) {
        selectGroupFragment = DialogFragment_SelectGroup.newInstance(1,position);
        selectGroupFragment.show(getSupportFragmentManager(), "DialogFragment_SelectGroup");

    }

    //used to populate RecycleView Items peopleSelected, where selectedNames is the names chosen in the DialogFragment
    //we are overriding the DialogFragmentListener.onDataSelected method, which attaches our DialogFragment back to the particular position on the RecycleView
    //thru this, we can send data, receive data from both the RecycleView and DialogFragment
    @Override
    public void onDataSelected(int position, Item data) {

        if (position>=0){
            String selectedNames = data.getItemPeople();
            itemArray.get(position).setItemPeople(selectedNames);
            adapter_items.notifyItemChanged(position);
        }
    }


    //after user selects group in DialogFragment, our textView will display out the user's selected group
    public void selectGroup(String groupName){
        selectedGroupText = findViewById(R.id.group_selected);
        selectedGroupText.setText(groupName);

    }

    //general
    // <!-- TODO: 4. store expense name, category, group, and item info in db after pressing confirm button  -->
    // <!-- TODO: 8. query item name, price from db -->

}