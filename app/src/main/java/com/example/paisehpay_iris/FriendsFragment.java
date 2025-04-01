package com.example.paisehpay_iris;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    RecyclerView friendsView;
    ArrayList<GroupMember> friendsArray = new ArrayList<>();

    ConstraintLayout friendsLayout;

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

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        //show notification
        friendsView = rootView.findViewById(R.id.recycle_view_friends);
        showFriendList();
        RecycleViewAdapter_GroupMember adapter = new RecycleViewAdapter_GroupMember(getActivity(),friendsArray);
        friendsView.setAdapter(adapter);
        friendsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    private void showFriendList() {
        String[] personList = getResources().getStringArray(R.array.dummy_person_name_list);
        String[] emailList = getResources().getStringArray(R.array.dummy_email_list);

        for (int i = 0; i<personList.length; i++){
            friendsArray.add(new GroupMember(personList[i],emailList[i]));

        }
    }

}