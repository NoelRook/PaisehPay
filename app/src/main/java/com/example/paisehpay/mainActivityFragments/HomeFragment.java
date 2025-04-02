package com.example.paisehpay.mainActivityFragments;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.dialogFragments.DialogFragment_CreateGroup;
import com.example.paisehpay.dialogFragments.DialogFragment_Owe;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Group;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HomeFragment extends Fragment implements DialogFragmentListener<Group> {
    //fragment that loads when u press home
    Button oweDetailsButton;
    Button owedDetailsButton;
    Button joinGroupButton;
    Button createGroupButton;
    ConstraintLayout oweLayout;
    DialogFragment_Owe oweDialogFragment;
    DialogFragment_Owe owedDialogFragment;

    DialogFragment_CreateGroup createGroupFragment;
    DialogFragment_CreateGroup joinGroupFragment;

    RecyclerView groupView;
    ArrayList<Group> groupArray = new ArrayList<>();
    RecycleViewAdapter_Group adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        //view owe details lead to owe details page
        oweLayout = rootView.findViewById(R.id.owe_layout);
        oweDetailsButton = oweLayout.findViewById(R.id.owe_more_details);
        oweDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oweDialogFragment = new DialogFragment_Owe();
                oweDialogFragment.show(getChildFragmentManager(), "DialogFragment");
            }
        });
        //view owed details lead to owed details page
        owedDetailsButton = oweLayout.findViewById(R.id.owed_more_details);
        owedDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owedDialogFragment = new DialogFragment_Owe();
                owedDialogFragment.show(getChildFragmentManager(), "DialogFragment");
            }
        });

        //press join group will join a group
        joinGroupButton = rootView.findViewById(R.id.join_group);
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroupFragment = DialogFragment_CreateGroup.newInstance(getString(R.string.join_group));
                joinGroupFragment.show(getChildFragmentManager(), "DialogFragment_CreateGroup");
            }
        });

        //press create group will create a group
        createGroupButton = rootView.findViewById(R.id.create_group);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupFragment = DialogFragment_CreateGroup.newInstance(getString(R.string.create_group));
                createGroupFragment.show(getChildFragmentManager(), "DialogFragment_CreateGroup");
            }
        });

        //show groups
        groupView = rootView.findViewById(R.id.recycle_view_group);;
        adapter = new RecycleViewAdapter_Group(getActivity(), groupArray);
        groupView.setAdapter(adapter);
        groupView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        showGroupList();


        return rootView;
    }

    private void showGroupList() {

        //populated by user create data alr
        //String[] nameList = getResources().getStringArray(R.array.dummy_group_name_list);
        //String[] dateList = getResources().getStringArray(R.array.dummy_expense_date_list);
        //String[] amountList = getResources().getStringArray(R.array.dummy_expense_amount_list);


        //for (int i = 0; i < nameList.length; i++) {
        //    groupArray.add(new Group(nameList[i], "Created " + dateList[i]));
        //}
        adapter.notifyDataSetChanged();

    }


    //create group's data is sent back and a new group is created
    @Override
    public void onDataSelected(int position, Group data) {
        Log.d("MainActivity", "Received new group: " + data);
        groupArray.add(data);
        adapter.notifyItemInserted(groupArray.size() - 1);

    }

}

// <!-- TODO: 1. change "hello username" text to user's name  -->
// <!-- TODO: 2. change date text to update today's date -->
// <!-- TODO: 3. change owe_amount and owed_amount from db  -->
// <!-- TODO: 4. catch name of group data in both create group and join group button, then instantiate a new group object for that user -->
// <!-- TODO: 5. add them to user's db and update group recycleview  -->