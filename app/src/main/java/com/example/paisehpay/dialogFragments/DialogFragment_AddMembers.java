package com.example.paisehpay.dialogFragments;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.example.paisehpay.databaseHandler.friendAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_UserSelect;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DialogFragment_AddMembers extends androidx.fragment.app.DialogFragment implements RecycleViewInterface{
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView userView;
    ArrayList<User> userArray = new ArrayList<>();
    Button confirmButton;
    private DialogFragmentListener listener;
    RecycleViewAdapter_UserSelect adapter;

    GroupAdapter grpAdapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    PreferenceManager pref;
    private static final String GROUP_ID =  "group_id";
    String groupId;


    //since we are instantiating the fragment from different locations, we need to be able to differentiate where it is from
    public static DialogFragment_AddMembers newInstance(String groupId){
        DialogFragment_AddMembers fragment = new DialogFragment_AddMembers();
        Bundle args = new Bundle();
        args.putString(GROUP_ID,groupId);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_select_group, container, false);

        if (getArguments() != null){
            //we will need the pos argument as we are populating RecycleView
            groupId = getArguments().getString(GROUP_ID);
        }

        grpAdapter = new GroupAdapter();

        userView = rootView.findViewById(R.id.select_group_recycle);
        showPersonList();
        adapter = new RecycleViewAdapter_UserSelect(getActivity(),userArray,this);
        userView.setAdapter(adapter);
        userView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }


    //put custom layout
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    //currently populating with fake data
    private void showPersonList() {
        // Clear the existing list (if any)
        userArray.clear();

        friendAdapter friendAdapter = new friendAdapter();

        // Get the current user's ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create callback for handling the friend list data
        // Call the database function to get real friend data
        friendAdapter.getFriendsForUser(currentUserId, new OperationCallbacks.ListCallback<User>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<User> friends) {
                // Add all friends to your array
                userArray.addAll(friends);
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
        });

    }




    //we need to ensure that AddPeople implements the interface so that we can pass data over
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DialogFragmentListener) {
            listener = (DialogFragmentListener) context;
        } else {
            throw new RuntimeException(context + " must implement DialogFragmentListener");
        }
    }

    @Override
    public void onButtonClick(int position) {

        userArray.get(position).setSelected(!userArray.get(position).isSelected());
        userView.getAdapter().notifyItemChanged(position);
        confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setVisibility(VISIBLE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    //add person here

                    //listener.onDataSelected(0,getSelectedPerson());
                    addMultiplePeople(groupId);
                    // reload the user list here
                }
                // add person here
                dismiss();
            }
        });
    }


    private ArrayList<User> getSelectedPerson() {
        ArrayList<User> selectedUser = new ArrayList<>();
        for (User user : userArray) {
            if (user.isSelected()) {
                selectedUser.add(user);
            }
        }
        return selectedUser;
    }
    private void addPerson(User user, String groupId) {
        // add person to the group
//        PreferenceManager pref = new PreferenceManager(requireContext());
//        User curUser = pref.getUser();

        Log.d("test",groupId + user.toString());

        if (user== null) throw new NullPointerException("Current User is null");
        grpAdapter.addMemberToGroup(groupId,user.getId(),user.getUsername(),new OperationCallbacks.OperationCallback(){
            @Override
            public void onSuccess() {
                //Toast.makeText(getContext(), "Group created successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(DatabaseError error) {
                //Toast.makeText(getContext(), "Failed to add Users to group: " + error.getMessage(),
               //         Toast.LENGTH_LONG).show();
                Log.e("CreateGroup", "Error in adding users", error.toException());

            }
        });
    }

    private void addMultiplePeople(String GROUP_ID){
        ArrayList<User> selectedUser = getSelectedPerson();
        Log.d("users to be added", selectedUser.toString());
        for (User user : selectedUser){
            addPerson(user,GROUP_ID);
            if (listener != null){
                listener.onDataSelected(0,user);
            }
        }
    }



}
