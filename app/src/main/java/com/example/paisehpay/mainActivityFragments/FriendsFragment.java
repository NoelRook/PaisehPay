package com.example.paisehpay.mainActivityFragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paisehpay.blueprints.GroupMember;
import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.friendAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupMember;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FriendsFragment extends Fragment {

    RecyclerView friendsView;
    Button addFriendButton;
    EditText friendEmail;
    ArrayList<GroupMember> friendsArray = new ArrayList<>();

    ConstraintLayout friendsLayout;
    friendAdapter adapter;
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

        adapter = new friendAdapter();
        pref = new PreferenceManager(getContext());
        curUser = pref.getUser();

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        //show notification
        friendsView = rootView.findViewById(R.id.recycle_view_friends);
        showFriendList();
        RecycleViewAdapter_GroupMember adapter = new RecycleViewAdapter_GroupMember(getActivity(),friendsArray);
        friendsView.setAdapter(adapter);
        friendsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button addFriendButton = rootView.findViewById(R.id.add_friend);
        EditText friendEmail = rootView.findViewById(R.id.search_friend);

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
        String[] personList = getResources().getStringArray(R.array.dummy_person_name_list);
        String[] emailList = getResources().getStringArray(R.array.dummy_email_list);

        for (int i = 0; i<personList.length; i++){
            friendsArray.add(new GroupMember(personList[i],emailList[i]));

        }
    }

    private void addFriend(String friendId){

        executorService.execute(() -> {
            adapter.addFriendBasedOnKey(curUser, friendId, new BaseDatabase.OperationCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "Friend added successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}