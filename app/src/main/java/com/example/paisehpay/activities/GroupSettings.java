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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.dialogFragments.DialogFragment_AddMembers;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupMember;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupSettings extends AppCompatActivity implements DialogFragmentListener<User> {

    //ur group settings page accessible by group_homepage

    RecyclerView groupMemberView;
    ArrayList<User> groupMemberArray = new ArrayList<>();
    TextView toolbarTitleText;
    ImageView backArrow;
    Button deleteGroupButton;
    Button addMembersButton;
    DialogFragment_AddMembers addMembersFragment;
    RecycleViewAdapter_GroupMember adapter;
    GroupAdapter groupAdapter;
    String groupId;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        groupId = intent.getStringExtra("GROUP_ID");
        groupAdapter = new GroupAdapter();

        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.group_settings);


        //press back arrow lead back to home fragment
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        //press add members button leads to being able to add members
        addMembersButton= findViewById(R.id.add_members);
        addMembersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMembersFragment = DialogFragment_AddMembers.newInstance(groupId);
                addMembersFragment.show(getSupportFragmentManager(), "DialogFragment_AddMembers");
            }
        });

        //press delete group button
        deleteGroupButton = findViewById(R.id.delete_group);
        deleteGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //groupAdapter.delete(groupId,);
                deleteGroup(groupId);

                Intent intent = new Intent(GroupSettings.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });


        //show groupMember list
        groupMemberView = findViewById(R.id.recycle_view_members);
        showGroupMemberList();
        adapter = new RecycleViewAdapter_GroupMember(this,groupMemberArray);
        groupMemberView.setAdapter(adapter);
        groupMemberView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void deleteGroup(String groupId) {
        groupAdapter.delete(groupId, new BaseDatabase.OperationCallback(){
            @Override
            public void onSuccess() {
                // User deleted successfully
                Log.d("Success", "expense deleted");
                Intent intent= new Intent(GroupSettings.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void showGroupMemberList() {
        //gotta call from db nvm jin can do
        executorService.execute(()->{
            groupAdapter.getGroupMates(groupId, new BaseDatabase.ListCallback<Map<String, String>>() {
                @Override
                public void onListLoaded(List<Map<String, String>> membersList) {
                    if (membersList != null && !membersList.isEmpty()) {
                        Map<String, String> members = membersList.get(0);
                        groupMemberArray.clear(); // Clear again in case dummy data was added

                        // Convert Firebase members to User objects
                        for (Map.Entry<String, String> entry : members.entrySet()) {
                            groupMemberArray.add(new User(
                                    entry.getKey(),    // userId
                                    null,            // email (if not available)
                                    entry.getValue(), // username
                                    null,            // phone (if not available)
                                    null             // other fields
                            ));

                            //Log.d("UserID",entry.getKey());
                        }
                        // Update UI on main thread
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    Toast.makeText(GroupSettings.this,
                            "Error loading members: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onDataSelected(int position, User data) {
        groupMemberArray.add(new User(data.getId(),data.getEmail(), data.getUsername(),data.getFriendKey(),null));


        adapter.notifyDataSetChanged();
    }

}