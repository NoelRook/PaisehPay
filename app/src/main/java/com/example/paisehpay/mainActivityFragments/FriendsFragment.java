package com.example.paisehpay.mainActivityFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.friendAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupMember;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FriendsFragment extends Fragment {

    RecyclerView friendsView;
    Button addFriendButton;
    EditText friendEmail;
    ArrayList<User> friendsArray = new ArrayList<>();
    RecycleViewAdapter_GroupMember adapter;
    friendAdapter friendAdapter;
    PreferenceManager pref;
    User curUser;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public FriendsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        friendAdapter = new friendAdapter();
        pref = new PreferenceManager(getContext());
        curUser = pref.getUser();

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        //show notification
        friendsView = rootView.findViewById(R.id.recycle_view_friends);
        showFriendList();
        adapter = new RecycleViewAdapter_GroupMember(getActivity(),friendsArray,"FriendsFragment",null);
        friendsView.setAdapter(adapter);
        friendsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addFriendButton = rootView.findViewById(R.id.add_friend);
        friendEmail = rootView.findViewById(R.id.search_friend);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something here
                addFriend(friendEmail.getText().toString().trim());
            }
        });


        return rootView;
    }

    private void showFriendList() {
        // Clear the existing list (if any)
        friendsArray.clear();

        // Get the current user's ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create callback for handling the friend list data
        OperationCallbacks.ListCallback friendsCallback = new OperationCallbacks.ListCallback<User>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<User> friends) {
                // Add all friends to your array
                friendsArray.addAll(friends);
                Log.d("friends", friends.toString());

                // Notify your adapter that data has changed
                if (friendAdapter != null) {
                    adapter.notifyDataSetChanged();
                }

                return null;
            }

            @Override
            public void onError(DatabaseError error) {
                // Show error message
                Toast.makeText(getContext(), "Failed to load friends: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("FriendsList", "Error loading friends", error.toException());

                // You might want to show the dummy data as fallback
                // showDummyDataAsFallback();
            }
        };

        // Call the database function to get real friend data
        friendAdapter.getFriendsForUser(currentUserId, friendsCallback);

    }

    private void addFriend(String friendId){

        executorService.execute(() -> {
            friendAdapter.addFriendBasedOnKey(curUser, friendId, new OperationCallbacks.OperationCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "Friend added successfully", Toast.LENGTH_SHORT).show();
                    showFriendList();
                }

                @Override
                public void onError(DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}