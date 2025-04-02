package com.example.paisehpay.mainActivityFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paisehpay.R;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Notification;
import com.example.paisehpay.blueprints.Notification;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.FirebaseAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    //notification fragment when u bottom nav bar notification page
    FirebaseAdapter adapter = new FirebaseAdapter();
    RecyclerView notificationView;

    ArrayList<Notification> notificationArray = new ArrayList<>();



    public NotificationFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        //show notification
        notificationView = rootView.findViewById(R.id.recycle_view_notifications);
        showNotificationList();
        RecycleViewAdapter_Notification adapter = new RecycleViewAdapter_Notification(getActivity(),notificationArray);
        notificationView.setAdapter(adapter);
        notificationView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;

    }

    private void showNotificationList() {

        adapter.getUsers(new FirebaseAdapter.UserListCallback() {
            @Override
            public void onUserListLoaded(List<User> users) {
                // Handle the list of users
                for (User user : users){
                    notificationArray.add(new Notification(user.getUsername(),user.getEmail()));

                }
                for (User user : users) {
                    Log.d("User", user.getUsername() + " - " + user.getEmail());
                }
            }

            @Override
            public void onError(DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", error.getMessage());
            }
        });

        String[] groupList = getResources().getStringArray(R.array.dummy_group_name_list);
        String[] notificationList = getResources().getStringArray(R.array.dummy_notifications_list);

        for (int i = 0; i<groupList.length; i++){
            notificationArray.add(new Notification(groupList[i],notificationList[i]));

        }
    }

    // <!-- TODO: 1. query from db  -->
}