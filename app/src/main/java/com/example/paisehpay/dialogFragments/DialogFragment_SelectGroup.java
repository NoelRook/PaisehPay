package com.example.paisehpay.dialogFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.paisehpay.R;
import com.example.paisehpay.activities.AddPeople;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Group;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupSelect;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.GroupAdapter;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DialogFragment_SelectGroup extends androidx.fragment.app.DialogFragment implements RecycleViewInterface {
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView groupView;
    ArrayList<Group> groupArray = new ArrayList<>();
    RecycleViewAdapter_GroupSelect adapter;


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
            grpAdapter.get(new BaseDatabase.ListCallback<Group>() {
                @Override
                public void onListLoaded(List<Group> groups) {
                    ArrayList<Group> tempList = new ArrayList<>();

                    for (Group group : groups) {
                        Log.d("group", group.getGroupId() + " - " + group.getGroupName()+ " - " +group.getGroupCreatedDate() + " - " +group.getGroupAmount() );
                        tempList.add(new Group(
                                group.getGroupId(),
                                group.getGroupName(),
                                "Created " + group.getGroupCreatedDate(),
                                group.getGroupAmount(),
                                group.getCreatedBy(),
                                group.getMembers()
                        ));
                    }

                    // Update UI on the main thread
                    mainHandler.post(() -> {
                        groupArray.clear();
                        groupArray.addAll(tempList);
                        adapter.notifyDataSetChanged();
                    });

                    Log.d("notification", groupArray.toString());
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("FirebaseError", error.getMessage());
                }
            });
        });
}
        @Override
        public void onButtonClick (int position){
            groupArray.get(position).setSelected(true);
            groupView.getAdapter().notifyItemChanged(position);
            String selectedGroupName = groupArray.get(position).getGroupName();
            AddPeople addpeoplePage = (AddPeople) getActivity();
            if (addpeoplePage != null) {
                addpeoplePage.selectGroup(selectedGroupName);
            }
            new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 500);
        }
    }
