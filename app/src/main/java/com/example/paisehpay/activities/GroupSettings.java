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
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.dialogFragments.DialogFragment_AddMembers;
import com.example.paisehpay.dialogFragments.DialogFragment_AddPeople;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupMember;
import com.example.paisehpay.blueprints.GroupMember;

import java.util.ArrayList;

public class GroupSettings extends AppCompatActivity implements DialogFragmentListener<User> {

    //ur group settings page accessible by group_homepage

    RecyclerView groupMemberView;
    ArrayList<User> groupMemberArray = new ArrayList<>();
    TextView toolbarTitleText;
    ImageView backArrow;
    Button deleteGroupButton;
    Button addMemebersButton;
    DialogFragment_AddMembers addMembersFragment;
    RecycleViewAdapter_GroupMember adapter;

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

        Intent intent = getIntent();
        String groupId = intent.getStringExtra("GROUP_ID");

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

        addMemebersButton= findViewById(R.id.add_members);
        addMemebersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMembersFragment = DialogFragment_AddMembers.newInstance(groupId);
                addMembersFragment.show(getSupportFragmentManager(), "DialogFragment_AddMembers");
            }
        });

        //show groupMember list
        groupMemberView = findViewById(R.id.recycle_view_members);
        showGroupMemberList();
        adapter = new RecycleViewAdapter_GroupMember(this,groupMemberArray);
        groupMemberView.setAdapter(adapter);
        groupMemberView.setLayoutManager(new LinearLayoutManager(this));


    }

    //we will change this later with actual data
    private void showGroupMemberList() {
        String[] nameList = getResources().getStringArray(R.array.dummy_person_name_list);
        String[] emailList = getResources().getStringArray(R.array.dummy_email_list);

        for (int i = 0; i<nameList.length; i++){
            groupMemberArray.add(new User(null,emailList[i],nameList[i],null,null));

        }
    }

    @Override
    public void onDataSelected(int position, User data) {
        groupMemberArray.add(new User(data.getId(),data.getEmail(), data.getUsername(),data.getFriendKey(),null));
        adapter.notifyDataSetChanged();
    }

}