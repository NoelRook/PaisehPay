package com.example.paisehpay.databaseHandler;

import com.example.paisehpay.blueprints.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserAdapter extends BaseDatabase  {
    private DatabaseReference userRef;
    private  static final String USER_TABLE = "Users";

    public UserAdapter(){
        super(USER_TABLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USER_TABLE);
    }


    @Override
    public void create(User user, final OperationCallback callback) {
        if (user == null) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot empty")));
            return;
        }

        String userId = userRef.push().getKey();
        if (userId == null) {
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate user ID")));
            return;
        }

        userRef.child(userId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError(DatabaseError.fromException(task.getException()));
                        }
                    }
                });
    }
    // Get all users
    @Override
    public void get(final ListCallback callback) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setUserId(userSnapshot.getKey()); // Set the Firebase-generated ID
                        users.add(user);
                    }
                }
                callback.onListLoaded(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    // Update a user
    @Override
    public void update(String userId, User user, final OperationCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot be null or empty")));
            return;
        }

        userRef.child(userId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError(DatabaseError.fromException(task.getException()));
                        }
                    }
                });
    }

    // Delete a user
    @Override
    public void delete(String userId, final OperationCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot be null or empty")));
            return;
        }

        userRef.child(userId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError(DatabaseError.fromException(task.getException()));
                        }
                    }
                });
    }

    public User getUserFromSnapshot(DataSnapshot snapshot) {
        User user = snapshot.getValue(User.class);
        if (user != null) {
            user.setId(snapshot.getKey());  // Set ID from snapshot key
        }
        return user;
    }

}
