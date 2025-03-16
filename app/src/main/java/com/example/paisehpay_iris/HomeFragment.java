package com.example.paisehpay_iris;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Button oweDetailsButton;
    Button owedDetailsButton;
    TextView owedAmountText;
    TextView oweAmountText;
    RecyclerView notificationView;
    ConstraintLayout oweLayout;
    ConstraintLayout owedLayout;

    ArrayList<Notification> notificationArray = new ArrayList<>();






    public HomeFragment() {
        // launches the fragment
    }

    //for all non ui related things
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //for all ui related things
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //view owe details lead to owe details page
        oweLayout = rootView.findViewById(R.id.owe_layout);
        oweDetailsButton = oweLayout.findViewById(R.id.owe_more_details);
        oweDetailsButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MoneyOwe.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            getActivity().finish();
        });

        //view owed details lead to owed details page
        owedLayout = rootView.findViewById(R.id.owed_layout);
        owedDetailsButton = owedLayout.findViewById(R.id.owed_more_details);
        owedDetailsButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MoneyOwed.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            getActivity().finish();
        });

        //show notification
        notificationView = rootView.findViewById(R.id.recycle_view_notifications);
        showNotificationList();
        RecycleViewAdapter adapter = new RecycleViewAdapter(getActivity(),notificationArray);
        notificationView.setAdapter(adapter);
        notificationView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;

    }

    private void showNotificationList() {
        String[] groupList = getResources().getStringArray(R.array.dummy_owe_group_name_list);
        String[] notificationList = getResources().getStringArray(R.array.dummy_owe_description_list);

        for (int i = 0; i<groupList.length; i++){
            notificationArray.add(new Notification(groupList[i],notificationList[i]));

        }
    }
}