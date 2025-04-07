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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.paisehpay.R;
import com.example.paisehpay.activities.AddPeople;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.Person;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Person;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;

import java.util.ArrayList;

public class DialogFragment_SelectGroup extends androidx.fragment.app.DialogFragment implements RecycleViewInterface {
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView personView;
    ArrayList<Person> personArray = new ArrayList<>();
    ArrayList<String> selectedPeople = new ArrayList<>();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_select_group, container, false);


        personView = rootView.findViewById(R.id.select_group_recycle);
        showGroupList();
        RecycleViewAdapter_Person adapter = new RecycleViewAdapter_Person(getActivity(),personArray,this);
        personView.setAdapter(adapter);
        personView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        personArray.clear();
        String[] nameList = getResources().getStringArray(R.array.dummy_group_name_list);
        for (String s : nameList) {
            personArray.add(new Person(s));
        }
    }

        @Override
        public void onButtonClick (int position){
            personArray.get(position).setSelected(true);
            personView.getAdapter().notifyItemChanged(position);
            String selectedGroupName = personArray.get(position).getPersonName();
            AddPeople addpeoplePage = (AddPeople) getActivity();
            if (addpeoplePage != null) {
                addpeoplePage.selectGroup(selectedGroupName);
            }
            new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 500);
        }
    }
