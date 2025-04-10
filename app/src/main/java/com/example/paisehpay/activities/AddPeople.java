package com.example.paisehpay.activities;

import static android.util.Log.d;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.blueprints.Category;
import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.computation.EqualSplit;
import com.example.paisehpay.computation.ReceiptInstance;
import com.example.paisehpay.computation.Receipts;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.itemAdapter;
import com.example.paisehpay.dialogFragments.DialogFragment_AddPeople;
import com.example.paisehpay.dialogFragments.DialogFragment_SelectGroup;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.R;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Category;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Item;
import com.example.paisehpay.recycleviewAdapters.RecycleViewListener;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//add people is the expense overview page
public class AddPeople extends AppCompatActivity implements RecycleViewInterface, DialogFragmentListener<Item>, RecycleViewListener {
    RecyclerView categoryView;
    ArrayList<Category> categoryArray = new ArrayList<>();

    RecyclerView itemView;
    ArrayList<Item> itemArray = new ArrayList<>();
    TextView toolbarTitleText;
    ImageView backArrow;
    ImageButton selectGroupButton;
    DialogFragment_SelectGroup selectGroupFragment;

    DialogFragment_AddPeople addPeopleFragment;
    Button splitBillButton;
    TextView selectedGroupText;
    RecycleViewAdapter_Item adapter_items;
    Receipts instance;
    EditText expenseNameText;
    String expenseName;
    String expenseGroup;
    String expenseDate;
    String expenseCategory;
    String expenseAmount;
    PreferenceManager preferenceManager;
    String Username;
    String Email;
    String id;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    ExpenseAdapter expAdapter;
    private String expenseId;

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

        //get current user
        preferenceManager = new PreferenceManager(AddPeople.this);
        expAdapter = new ExpenseAdapter();
        User user = preferenceManager.getUser();
        Email = user.getEmail();
        Username = user.getUsername();
        id = user.getId();

        //press back arrow lead back to receipt overview page
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPeople.this, ReceiptOverview.class);

                instance.clearAll();
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
                selectGroupFragment = new DialogFragment_SelectGroup();
                selectGroupFragment.show(getSupportFragmentManager(), "DialogFragment_SelectGroup");

            }
        });

        expenseNameText = findViewById(R.id.add_expense_name);








        //click confirm button to finalise bill and lead to split bill page
        splitBillButton = findViewById(R.id.confirm_button);
        // In your splitBillButton onClick listener:
        splitBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseName = expenseNameText.getText().toString();
                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = formatter.format(today);
                expenseDate = formattedDate;

                Receipts instance = ReceiptInstance.getInstance();
                expenseAmount = instance.getTotal();

                if (expenseNameText == null) {
                    Toast.makeText(AddPeople.this, "Please enter expense name", Toast.LENGTH_LONG).show();
                } else if (!(checkItemHasUsers())) {
                    Toast.makeText(AddPeople.this, "Please add people to your items", Toast.LENGTH_LONG).show();
                } else {
                    Expense expense = new Expense(expenseName, expenseDate, id, null, expenseAmount, expenseCategory, expenseGroup);

                    // Store expense first
                    StoreExpenseAndItems(expense, itemArray);
                }
            }
        });





        //show category list.
        //we will link the adapter used for the category scroll bar
        categoryView = findViewById(R.id.recycle_view_category);
        showCategoryList();
        RecycleViewAdapter_Category adapter_category = new RecycleViewAdapter_Category(this,categoryArray,this);
        categoryView.setAdapter(adapter_category);
        categoryView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //show item list
        //we will link the adapter used for the item scroll bar
        itemView = findViewById(R.id.recycle_view_items);
        adapter_items = new RecycleViewAdapter_Item(this,itemArray,this,"AddPeople");
        itemView.setAdapter(adapter_items);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        showItemList();

    }



    //populate item RecycleView
    //ReceiptInstance(of Receipt) stores the user's items entered in the ReceiptOverview page, then since using Singleton design pattern,
    //we can call the instance's items and prices, which we would populate the RecycleView
    //once we add a item, we need tell the adapter that our dataset changed
    private void showItemList() { // get the item array here
        instance = ReceiptInstance.getInstance();
        instance.loadItemWithGST();
        ArrayList<String> itemNameArray = instance.getArray_of_items();
        ArrayList<Double> itemPriceArray = instance.getArray_of_item_prices();
        //hashmap should contain the name: price owed


        for (int i = 0; i<itemPriceArray.size();i++){
            itemArray.add(new Item(null,itemNameArray.get(i),itemPriceArray.get(i),null,"Add People to Item", null));
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
            categoryArray.add(new Category(nameList[i], imageResId));
        }
        imageArray.recycle();
        }



    //used to open up the DialogFragment_SelectGroup when user clicks on an item in the RecycleView
    //it will display out all the available users from the group to assign to items
    //since we are querying from the RecycleView, we need its position, and "1" to differentiate it from populating from SelectGroupButton
    //we are overriding the RecycleViewInterface.onButtonClick method, which allows us to map that particular position on the RecycleView to a DialogFragment
    @Override
    public void onButtonClick(int position) {
        if (expenseGroup == null){
            Toast.makeText(AddPeople.this, "Please select a group", Toast.LENGTH_LONG).show();
        }else {
            Item item = itemArray.get(position); //we are getting the actual item and its position
            addPeopleFragment = DialogFragment_AddPeople.newInstance(1,expenseGroup,position,item);
            addPeopleFragment.show(getSupportFragmentManager(), "DialogFragment_AddPeople");
        }

    }

    //used to populate RecycleView Items peopleSelected, where selectedNames is the names chosen in the DialogFragment
    //we are overriding the DialogFragmentListener.onDataSelected method, which attaches our DialogFragment back to the particular position on the RecycleView
    //thru this, we can send data, receive data from both the RecycleView and DialogFragment
    @Override
    public void onDataSelected(int position, Item item) {
        //Receipts instance = ReceiptInstance.getInstance();
        item.getItemPeopleArray();
        EqualSplit split = new EqualSplit();
        split.splitTotal(item);
        adapter_items.notifyItemChanged(position);
    }


    //after user selects group in DialogFragment, our textView will display out the user's selected group
    public void selectGroup(Group groupName){
        selectedGroupText = findViewById(R.id.group_selected);
        selectedGroupText.setText(groupName.getGroupName());
        expenseGroup = groupName.getGroupId();
    }

    public boolean checkItemHasUsers(){
        for (Item item : itemArray) {
            String peopleStr = item.getItemPeopleString();
            if (peopleStr == null || peopleStr.trim().isEmpty()) {
                return false;
            }
            ArrayList<User> peopleArray = item.getItemPeopleArray();
            if (peopleArray == null || peopleArray.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSelected(String name) {
        expenseCategory = name;
        Log.d("Category", "User selected: " + name);
    }

    // New method to handle the sequential operations
    private void StoreExpenseAndItems(Expense expense, ArrayList<Item> items) {
        executorService.execute(() -> {
            expAdapter.create(expense, new ExpenseAdapter.OperationCallback() {
                @Override
                public void onSuccess() {
                    String expenseId = expense.getexpenseId(); // Make sure this gets the generated ID
                    Log.d("Saved expense", "ID: " + expenseId);

                    // Now store items with this expenseId
                    storeExpenseItems(items, expenseId);

                    // Only navigate after all items are stored
                    runOnUiThread(() -> {
                        Intent intent = new Intent(AddPeople.this, BillSplit.class);
                       // intent.putExtra("Items", items);
                        intent.putExtra("Expense",expense);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    });
                }

                @Override
                public void onError(DatabaseError error) {
                    runOnUiThread(() ->
                            Toast.makeText(AddPeople.this,
                                    "Failed to save expense: " + error.getMessage(),
                                    Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    // Modified storeExpenseItems method
    private void storeExpenseItems(ArrayList<Item> items, String expenseId) {
        itemAdapter itemadapter = new itemAdapter();
        for (Item item : items) {
            item.calculateDebts();
            item.setExpenseId(expenseId); // Set the expense ID for each item

            executorService.execute(() -> {
                itemadapter.create(item, new ExpenseAdapter.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Saved item", item.getItemName() + " for expense " + expenseId);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Save item error", error.getMessage());
                    }
                });
            });
        }
    }


}