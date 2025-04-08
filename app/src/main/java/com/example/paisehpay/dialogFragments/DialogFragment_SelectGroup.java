package com.example.paisehpay.dialogFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.paisehpay.R;
import com.example.paisehpay.activities.AddPeople;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.Person;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Group;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_GroupSelect;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;

import java.util.ArrayList;

public class DialogFragment_SelectGroup extends androidx.fragment.app.DialogFragment implements RecycleViewInterface {
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView groupView;
    ArrayList<Group> groupArray = new ArrayList<>();
    RecycleViewAdapter_GroupSelect adapter;





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

    //currently populating with fake data
    private void showGroupList() {
        groupArray.clear();
        String[] nameList = getResources().getStringArray(R.array.dummy_group_name_list);
        for (String s : nameList) {
            groupArray.add(new Group(null,s,null));
        }
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
