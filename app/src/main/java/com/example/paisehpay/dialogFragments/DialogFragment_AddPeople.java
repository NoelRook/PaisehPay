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
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.Person;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Person;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;

import java.util.ArrayList;

public class DialogFragment_AddPeople extends androidx.fragment.app.DialogFragment implements RecycleViewInterface {
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView personView;
    ArrayList<Person> personArray = new ArrayList<>();
    ArrayList<String> selectedPeople = new ArrayList<>();
    Button confirmButton;
    private static final String DATA_TO_QUERY = "data_to_query";
    private int query_from;
    private DialogFragmentListener listener;
    private static final String POSITION = "position";
    private int pos;

    private static final String ITEM = "item";
    private Item item;



    //since we are instantiating the fragment from different locations, we need to be able to differentiate where it is from
    public static DialogFragment_AddPeople newInstance(int query_from,Integer pos, Item item){
        DialogFragment_AddPeople fragment = new DialogFragment_AddPeople();
        Bundle args = new Bundle();
        args.putInt(DATA_TO_QUERY,query_from);
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

        if (getArguments() != null){
            //we will need the pos argument as we are populating RecycleView
            query_from = getArguments().getInt(DATA_TO_QUERY);
            pos = getArguments().getInt(POSITION);
            item = getArguments().getParcelable(ITEM);
        }

        personView = rootView.findViewById(R.id.select_group_recycle);
        showPersonList();
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
    private void showPersonList() {
        personArray.clear();
        String[] nameList = getResources().getStringArray(R.array.dummy_person_name_list);
        for (String s : nameList) {
            personArray.add(new Person(s));
            }
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
        personArray.get(position).setSelected(!personArray.get(position).isSelected());
        personView.getAdapter().notifyItemChanged(position);
        confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setVisibility(VISIBLE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSelectedPeopleList();
                Log.d("DialogFragment",selectedPeople.toString());
                if (listener != null){
                    item.setItemPeopleString(formatSelectedPeople());
                    item.setItemPeopleArray();
                    listener.onDataSelected(pos,item);
                }
                dismiss();
            }
        });
    }


    private void updateSelectedPeopleList() {
        selectedPeople.clear();
        for (Person person : personArray) {
            if (person.isSelected()) {
                selectedPeople.add(person.getPersonName());
            }
        }
    }

    private String formatSelectedPeople(){
        return String.join(",",selectedPeople);
    }

}
