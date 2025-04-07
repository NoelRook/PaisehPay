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
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupMember;
import com.example.paisehpay.blueprints.GroupMember;

import java.util.ArrayList;

public class GroupSettings extends AppCompatActivity {

    //ur group settings page accessible by group_homepage

    RecyclerView groupMemberView;
    ArrayList<GroupMember> groupMemberArray = new ArrayList<>();
    TextView toolbarTitleText;
    ImageView backArrow;
    Button deleteGroupButton;
    Button addMemebersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.group_settings);


        //press back arrow lead back to home fragment
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupSettings.this, GroupHomepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });

        //show groupMember list
        groupMemberView = findViewById(R.id.recycle_view_members);
        showGroupMemberList();
        RecycleViewAdapter_GroupMember adapter = new RecycleViewAdapter_GroupMember(this,groupMemberArray);
        groupMemberView.setAdapter(adapter);
        groupMemberView.setLayoutManager(new LinearLayoutManager(this));

    }

    //we will change this later with actual data
    private void showGroupMemberList() {
        String[] nameList = getResources().getStringArray(R.array.dummy_person_name_list);
        String[] emailList = getResources().getStringArray(R.array.dummy_email_list);

        for (int i = 0; i<nameList.length; i++){
            groupMemberArray.add(new GroupMember(nameList[i],emailList[i]));

        }
    }

    // <!-- TODO: 1. query data from db  -->
    // <!-- TODO: 2. (iris will prob do) press add members button then lead to popup asking to add friend -->
    // <!-- TODO: 3. query added friend from friend db, add them into this group -->
    // <!-- TODO: 4. figure out a way to isolate a certain button data in the recycleview (ie u remove a friend) how to catch that data for that particular user?-->
    // <!-- TODO: 5. afterwards update group db and remove user from group  -->
    // <!-- TODO: 6. bind delete group button so that the group is deleted from db, removing this groups' homepage and group setting page as well as their box in the homefragment  -->

}