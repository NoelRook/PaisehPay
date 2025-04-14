package com.example.paisehpay.dialogFragments;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_UserSelect;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DialogFragment_AddPeople extends androidx.fragment.app.DialogFragment implements RecycleViewInterface {
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView userView;
    ArrayList<User> userArray = new ArrayList<>();

    ArrayList<User> selectedUserArray = new ArrayList<>();
    Button confirmButton;
    private static final String DATA_TO_QUERY = "data_to_query";
    private DialogFragmentListener listener;
    private static final String POSITION = "position";
    private int pos;
    private static String groupId;
    String expenseId;
    private static String GROUP_ID = "group_id";

    private static final String ITEM = "item";
    private Item item;
    RecycleViewAdapter_UserSelect adapter;
    GroupAdapter grpAdapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    //since we are instantiating the fragment from different locations, we need to be able to differentiate where it is from
    public static DialogFragment_AddPeople newInstance(int query_from,String groupId,Integer pos, Item item){
        DialogFragment_AddPeople fragment = new DialogFragment_AddPeople();
        Bundle args = new Bundle();
        args.putInt(DATA_TO_QUERY,query_from);
        args.putString(GROUP_ID,groupId);
        args.putInt(POSITION,pos);
        args.putParcelable(ITEM,item);
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

        //init group adapter
        grpAdapter = new GroupAdapter();

        if (getArguments() != null){
            //we will need the pos argument as we are populating RecycleView
            pos = getArguments().getInt(POSITION);
            item = getArguments().getParcelable(ITEM);
            groupId = getArguments().getString(GROUP_ID);
        }

        userView = rootView.findViewById(R.id.select_group_recycle);

        showPersonList(groupId);
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

    private void showPersonList(String groupId) {
        userArray.clear();


        executorService.execute(()->{
            grpAdapter.getGroupMates(groupId, new OperationCallbacks.ListCallback<Map<String, String>>() {
                @Override
                public void onListLoaded(List<Map<String, String>> membersList) {
                    if (membersList != null && !membersList.isEmpty()) {
                        Map<String, String> members = membersList.get(0);
                        userArray.clear(); // Clear again in case dummy data was added

                        // Convert Firebase members to User objects
                        for (Map.Entry<String, String> entry : members.entrySet()) {
                            userArray.add(new User(
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
                            Toast.makeText(getContext(),
                                    "Error loading members: " + error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                }
            });
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
                updateSelectedPeopleList();
                if (listener != null){
                    item.setItemPeopleArray(selectedUserArray);
                    listener.onDataSelected(pos,item);
                }
                dismiss();
            }
        });
    }


    private void updateSelectedPeopleList() {
        selectedUserArray.clear();
        for (User user : userArray) {
            if (user.isSelected()) {
                selectedUserArray.add(user);
            }
        }
    }


    //get members from group


}
