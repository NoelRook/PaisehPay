package com.example.paisehpay.dialogFragments;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
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
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_UserSelect;
import com.example.paisehpay.recycleviewAdapters.RecycleViewInterface;

import java.util.ArrayList;

public class DialogFragment_AddMembers extends androidx.fragment.app.DialogFragment implements RecycleViewInterface{
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView userView;
    ArrayList<User> userArray = new ArrayList<>();
    Button confirmButton;
    private DialogFragmentListener listener;
    RecycleViewAdapter_UserSelect adapter;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_select_group, container, false);


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
        userArray.clear();
        String[] nameList = getResources().getStringArray(R.array.dummy_person_name_list);//since we now populating fake data
        String[] emailList = getResources().getStringArray(R.array.dummy_email_list);
        for (int i = 0; i < nameList.length; i++) {
            userArray.add(new User(null,emailList[i],nameList[i]));
            }
        userArray.add(new User(null,"test@gmail.com","Leanne")); //we select this ah if not rv also see no change
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
                    listener.onDataSelected(0,getSelectedPerson());
                }
                dismiss();
            }
        });
    }


    private User getSelectedPerson() {
        for (User user : userArray) {
            if (user.isSelected()) {
                return user;
            }
        }
        return null;
    }


}
