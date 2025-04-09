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
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.paisehpay.tabBar.ExpenseFragment;
import com.example.paisehpay.R;
import com.example.paisehpay.tabBar.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class GroupHomepage extends AppCompatActivity {
    //this is the page when user clicks group hompage
    TextView toolbarTitleText;
    ImageView backArrow;
    Button groupSettingButton;

    ArrayList<String> categoryArray = new ArrayList<>();

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

        //this is to link the Viewpager adapter which allows users to access scrollable tab
        TabLayout tabLayout = findViewById(R.id.expense_tab_layout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
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
            adapter.addFragment(ExpenseFragment.newInstance(category),category);
        }
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(adapter.getTitle(position));
        }).attach();

        adapter.notifyDataSetChanged();




        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.group_homepage);

        //press back arrow lead back to home fragment
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupHomepage.this, MainActivity.class);
                intent.putExtra("fragmentToLoad","HomeFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });

        //press group settings button lead to group settings page
        groupSettingButton = findViewById(R.id.group_settings);
        groupSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupHomepage.this, GroupSettings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();

            }
        });

    }
}