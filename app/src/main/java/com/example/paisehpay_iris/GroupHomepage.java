package com.example.paisehpay_iris;

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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GroupHomepage extends AppCompatActivity {
    TextView toolbarTitleText;
    ImageView backArrow;
    Button groupSettingButton;

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

        TabLayout tabLayout = findViewById(R.id.expense_tab_layout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);

        // Add fragments to adapter
        adapter.addFragment(new ExpenseFragment(), getString(R.string.overall));
        adapter.addFragment(new ExpenseFragment(), getString(R.string.food));
        adapter.addFragment(new ExpenseFragment(), getString(R.string.transport));
        adapter.addFragment(new ExpenseFragment(), getString(R.string.shopping));
        adapter.addFragment(new ExpenseFragment(),getString(R.string.utilities));
        adapter.addFragment(new ExpenseFragment(), getString(R.string.entertainment));
        adapter.addFragment(new ExpenseFragment(), getString(R.string.rent));
        adapter.addFragment(new ExpenseFragment(), getString(R.string.health));
        viewPager.setAdapter(adapter);

        // Connect TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(adapter.getTitle(position));

            // Load the corresponding fragment in the FrameLayout
            Fragment fragment = new ExpenseFragment(); // Use your actual filter fragment
            Bundle args = new Bundle();
            args.putString("category", adapter.getTitle(position)); // Pass category to fragment
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.expense_filter_fragment, fragment)
                    .commit();
        }).attach();


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

        // <!-- TODO: 1. (iris will do this) bind add category button, create layout for add category  -->
        // <!-- TODO: 2. after user selects category, add it into the horizontal scroll bar as one of the clickable tabs  -->
        // <!-- TODO: 3. after clicking any category, send category name to expensefragment so that data can be filtered and queried by db  -->


    }
}