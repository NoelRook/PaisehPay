package com.example.paisehpay;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.UserAdapter;
import com.example.paisehpay.sessionHandler.fragmentPopulator;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationFragment extends Fragment {
    //notification fragment when u bottom nav bar notification page

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    RecyclerView notificationView;
    ArrayList<Notification> notificationArray = new ArrayList<>();
    fragmentPopulator frag = new fragmentPopulator();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationView = rootView.findViewById(R.id.recycle_view_notifications);
        notificationView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set empty adapter before Firebase query starts
        RecycleViewAdapter_Notification adapter = new RecycleViewAdapter_Notification(getActivity(), notificationArray);
        notificationView.setAdapter(adapter);

        // Fetch data from Firebase in the background
        showNotificationList();

        return rootView;
    }

    private void showNotificationList() {
        frag.showList(executorService,mainHandler, notificationArray,notificationView);
    }

    /*
    * executorService.execute(() -> {
            UserAdapter adapter = new UserAdapter();
            adapter.get(new BaseDatabase.ListCallback<User>() {
                @Override
                public void onListLoaded(List<User> users) {
                    ArrayList<Notification> tempList = new ArrayList<>();

                    for (User user : users) {
                        Log.d("User", user.getUsername() + " - " + user.getEmail());
                        tempList.add(new Notification(user.getUsername(), user.getEmail()));
                    }

                    // Update UI on the main thread
                    mainHandler.post(() -> {
                        notificationArray.clear();
                        notificationArray.addAll(tempList);
                        notificationView.getAdapter().notifyDataSetChanged();
                    });

                    Log.d("notification", notificationArray.toString());
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("FirebaseError", error.getMessage());
                }
            });
        });
    * */


    // <!-- TODO: 1. query from db  -->


}