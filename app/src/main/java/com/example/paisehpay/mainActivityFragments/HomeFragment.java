package com.example.paisehpay.mainActivityFragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.example.paisehpay.databaseHandler.friendAdapter;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.dialogFragments.DialogFragment_CreateGroup;
import com.example.paisehpay.dialogFragments.DialogFragment_Owe;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Group;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.widget.TextView;
import android.widget.Toast;

import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;


public class HomeFragment extends Fragment implements DialogFragmentListener<Group> {
    //fragment that loads when u press home
    TextView welcomeMessage;
    Button oweDetailsButton;
    Button owedDetailsButton;
    Button createGroupButton;
    ConstraintLayout oweLayout;
    DialogFragment_Owe oweDialogFragment;
    DialogFragment_Owe owedDialogFragment;

    DialogFragment_CreateGroup createGroupFragment;
    String Username;
    String Email;
    String id;

    RecyclerView groupView;
    ArrayList<User> userArray = new ArrayList<>();
    ArrayList<Group> groupArray = new ArrayList<>();
    RecycleViewAdapter_Group adapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    PreferenceManager preferenceManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        preferenceManager = new PreferenceManager(getContext());

        friendAdapter friendadapter = new friendAdapter();

        User savedUser = preferenceManager.getUser();
        if (savedUser != null) {
            getFriendsList();
            id = savedUser.getId();
            Username = savedUser.getUsername();
            Email = savedUser.getEmail();
            Log.d("Usersaved",id + Username +Email);
        }

        welcomeMessage = rootView.findViewById(R.id.welcome_message);
        welcomeMessage.setText("Hello "+ Username+"!");

        //view owe details lead to owe details page
        oweLayout = rootView.findViewById(R.id.owe_layout);
        oweDetailsButton = oweLayout.findViewById(R.id.owe_more_details);
        oweDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oweDialogFragment = new DialogFragment_Owe();
                oweDialogFragment.show(getChildFragmentManager(), "DialogFragment");
            }
        });
        //view owed details lead to owed details page
        owedDetailsButton = oweLayout.findViewById(R.id.owed_more_details);
        owedDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owedDialogFragment = new DialogFragment_Owe();
                owedDialogFragment.show(getChildFragmentManager(), "DialogFragment");
            }
        });


        //press create group will create a group
        createGroupButton = rootView.findViewById(R.id.create_group);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupFragment = DialogFragment_CreateGroup.newInstance(getString(R.string.create_group));
                createGroupFragment.show(getChildFragmentManager(), "DialogFragment_CreateGroup");
            }
        });

        //show groups
        groupView = rootView.findViewById(R.id.recycle_view_group);;
        adapter = new RecycleViewAdapter_Group(getActivity(), groupArray);
        groupView.setAdapter(adapter);
        groupView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //todo add caching for groupArray here
        showGroupList();
        return rootView;
    }

    private void showGroupList() {
        // Clear existing data and show loading state
        mainHandler.post(() -> {
            groupArray.clear();
            adapter.notifyDataSetChanged();
            // Optionally show loading indicator
        });

        PreferenceManager pref = new PreferenceManager(getContext());
        User currentUser = pref.getUser();

        // Check if user is logged in
        if (currentUser == null || currentUser.getId() == null) {
            mainHandler.post(() -> {
                Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        executorService.execute(() -> {
            GroupAdapter grpAdapter = new GroupAdapter();

            // Get groups where user is either creator or member
            grpAdapter.getGroupsForUser(currentUser.getId(), new OperationCallbacks.ListCallback<Group>() {
                @Override
                public void onListLoaded(List<Group> groups) {
                    ArrayList<Group> tempList = new ArrayList<>();

                    for (Group group : groups) {
                        Log.d("GroupData",
                                "ID: " + group.getGroupId() +
                                        ", Name: " + group.getGroupName() +
                                        ", Date: " + group.getGroupCreatedDate() +
                                        ", Amount: " + group.getGroupAmount());

                        // Create new Group object with formatted date
                        tempList.add(new Group(
                                group.getGroupId(),
                                group.getGroupName(),
                                "Created " + group.getGroupCreatedDate(),
                                group.getGroupAmount(),
                                group.getCreatedBy(),
                                group.getMembers()
                        ));
                    }

                    // Update UI on main thread
                    mainHandler.post(() -> {
                        groupArray.clear();
                        if (tempList.isEmpty()) {
                            // Show empty state if no groups found
                            // textEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            groupArray.addAll(tempList);
                            // textEmptyState.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onError(DatabaseError error) {
                    mainHandler.post(() -> {
                        Log.e("FirebaseError", error.getMessage());
                        Toast.makeText(getContext(),
                                "Failed to load groups: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }


    //create group's data is sent back and a new group is created
    @Override
    public void onDataSelected(int position, Group data) {
        Log.d("MainActivity", "Received new group: " + data);
        groupArray.add(data);
        adapter.notifyItemInserted(groupArray.size() - 1);

    }



    private void getFriendsList() {
        // Clear the existing list (if any)
        userArray.clear();

        friendAdapter friendAdapter = new friendAdapter();

        // Get the current user's ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create callback for handling the friend list data
        OperationCallbacks.ListCallback friendsCallback = new OperationCallbacks.ListCallback<User>() {
            @Override
            public void onListLoaded(List<User> friends) {
                // Add all friends to your array
                userArray.addAll(friends);
                preferenceManager.mapManyFriends(userArray);
                Log.d("friends", friends.toString());

                // Notify your adapter that data has changed
                if (friendAdapter != null) {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(DatabaseError error) {
                // Show error message
                Toast.makeText(getContext(), "Failed to load friends: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("FriendsList", "Error loading friends", error.toException());
            }
        };

        // Call the database function to get real friend data
        friendAdapter.getFriendsForUser(currentUserId, friendsCallback);

    }

}

// <!-- TODO: 1. change "hello username" text to user's name  -->
// <!-- TODO: 2. change date text to update today's date -->
// <!-- TODO: 3. change owe_amount and owed_amount from db  -->
// <!-- TODO: 4. catch name of group data in both create group and join group button, then instantiate a new group object for that user -->
// <!-- TODO: 5. add them to user's db and update group recycleview  -->
// <!-- TODO: 6. figure out how to isolate a certain group button to lead to the correct group homepage  -->
// <!-- TODO: 7. (for iris) change scrolllayout to reycleview with the usual shit  -->