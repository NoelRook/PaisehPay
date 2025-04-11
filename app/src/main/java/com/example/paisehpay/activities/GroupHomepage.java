// GroupHomepage.java - Modified to load data first
package com.example.paisehpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.ExpenseSingleton;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.tabBar.ExpenseFragment;
import com.example.paisehpay.R;
import com.example.paisehpay.tabBar.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupHomepage extends AppCompatActivity {
    TextView toolbarTitleText;
    ImageView backArrow;
    Button groupSettingButton;

    ArrayList<String> categoryArray = new ArrayList<>();
    ExpenseSingleton singleExpense;
    private String groupID;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.group_homepage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        groupID = intent.getStringExtra("GROUP_ID");

        singleExpense = ExpenseSingleton.getInstance();

        // Initialize UI components
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.group_homepage);
        backArrow = findViewById(R.id.back_arrow);
        groupSettingButton = findViewById(R.id.group_settings);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.expense_tab_layout);

        // Load expenses first
        loadExpenses();
    }

    private void loadExpenses() {
        singleExpense.getExpensesByGroupId(groupID, new BaseDatabase.ListCallback<Expense>() {
            @Override
            public void onListLoaded(List<Expense> expenses) {
                // Data loaded, now setup the ViewPager and fragments
                setupViewPager();
                setupClickListeners();
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(GroupHomepage.this, "Failed to load expenses", Toast.LENGTH_SHORT).show();
                // Still setup UI but with empty data
                setupViewPager();
                setupClickListeners();
            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);

        //add categories
        categoryArray.add(getString(R.string.overall));
        categoryArray.add(getString(R.string.food));
        categoryArray.add(getString(R.string.transport));
        categoryArray.add(getString(R.string.shopping));
        categoryArray.add(getString(R.string.utilities));
        categoryArray.add(getString(R.string.entertainment));
        categoryArray.add(getString(R.string.rent));
        categoryArray.add(getString(R.string.health));

        for (String category: categoryArray){
            adapter.addFragment(ExpenseFragment.newInstance(category, groupID), category);
        }
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(adapter.getTitle(position));
        }).attach();

        adapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        backArrow.setOnClickListener(view -> {
            Intent intent = new Intent(GroupHomepage.this, MainActivity.class);
            intent.putExtra("fragmentToLoad","HomeFragment");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        });

        groupSettingButton.setOnClickListener(view -> {
            Intent intent = new Intent(GroupHomepage.this, GroupSettings.class);
            intent.putExtra("GROUP_ID", groupID);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        });
    }
}