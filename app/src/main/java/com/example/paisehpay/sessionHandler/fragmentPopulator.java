package com.example.paisehpay.sessionHandler;

import android.util.Log;

import com.example.paisehpay.Notification;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.UserAdapter;
import com.google.firebase.database.DatabaseError;
import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class fragmentPopulator {

    public void showList(Executor executorService, Handler mainHandler, ArrayList notificationArray, RecyclerView notificationView) {
        executorService.execute(() -> {
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
    }
}
