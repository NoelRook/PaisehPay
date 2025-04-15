package com.example.paisehpay.dialogFragments;

import static android.view.View.VISIBLE;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

public class DialogFragment_AddMembers extends androidx.fragment.app.DialogFragment implements RecycleViewInterface{
    //DialogFragment that allows user to add members in GroupSettings.java

    private View rootView;
    private RecyclerView userView;
    private ArrayList<User> userArray = new ArrayList<>();
    private Button confirmButton;
    private DialogFragmentListener<User> listener;
    private RecycleViewAdapter_UserSelect adapter;
    private GroupAdapter grpAdapter;
    private static final String GROUP_ID =  "group_id";
    private String groupId;
    private TextView titleText;
    private DialogFragmentListener<User> dialogFragmentListener;


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
        titleText = rootView.findViewById(R.id.select_group);
        titleText.setText(R.string.add_members_text);
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

    private void showPersonList() {
        userArray.clear();

        friendAdapter friendAdapter = new friendAdapter();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendAdapter.getFriendsForUser(currentUserId, new OperationCallbacks.ListCallback<User>() {
            @Override
            public void onListLoaded(List<User> friends) {
                // Add all friends to your array
                userArray.addAll(friends);
                Log.d("friends", friends.toString());

                // Notify your adapter that data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load friends: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("FriendsList", "Error loading friends", error.toException());
            }
        });

    }


    //we need to ensure that AddPeople implements the interface so that we can pass data over
    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DialogFragmentListener) {
            listener = (DialogFragmentListener<User>) context;
        } else {
            throw new RuntimeException(context + " must implement DialogFragmentListener");
        }
    }


    //function to catch the users selected from RecycleViewAdapter_GroupMember
    //this function allows us to add members to group
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
                    addMultiplePeople(groupId);
                }
                dismiss();
            }
        });
    }


    //function to get all selected people
    private ArrayList<User> getSelectedPerson() {
        ArrayList<User> selectedUser = new ArrayList<>();
        for (User user : userArray) {
            if (user.isSelected()) {
                selectedUser.add(user);
            }
        }
        return selectedUser;
    }

    //add selected person to group
    private void addPerson(User user, String groupId) {
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

    //function to add multiple people to group
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
