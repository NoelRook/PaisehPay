package com.example.paisehpay.dialogFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.paisehpay.R;
import com.example.paisehpay.activities.AddPeople;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupSelect;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DialogFragment_SelectGroup extends androidx.fragment.app.DialogFragment implements RecycleViewInterface {
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView groupView;
    ArrayList<Group> groupArray = new ArrayList<>();
    RecycleViewAdapter_GroupSelect adapter;
    PreferenceManager pref;
    User CurUser;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_select_group, container, false);
        groupView = rootView.findViewById(R.id.select_group_recycle);
        showGroupList();
        pref = new PreferenceManager(getContext());
        CurUser = pref.getUser();

        adapter = new RecycleViewAdapter_GroupSelect(getActivity(),groupArray,this);
        groupView.setAdapter(adapter);
        groupView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    private void showGroupList() {
        groupArray.clear();
        executorService.execute(() -> {
            GroupAdapter grpAdapter = new GroupAdapter();

            // Get groups where user is either creator or member
            grpAdapter.getGroupsForUser(CurUser.getId(), new BaseDatabase.ListCallback<Group>() {
                @Override
                public HashMap<String, Date> onListLoaded(List<Group> groups) {
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
                                 group.getGroupCreatedDate(),
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
                    return null;
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
        @Override
        public void onButtonClick (int position){
            groupArray.get(position).setSelected(true);
            groupView.getAdapter().notifyItemChanged(position);
            Group selectedGroupName = groupArray.get(position);
            AddPeople addpeoplePage = (AddPeople) getActivity();
            if (addpeoplePage != null) {
                addpeoplePage.selectGroup(selectedGroupName);
            }
            new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 500);
        }
    }
