package com.example.paisehpay.databaseHandler;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class friendAdapter extends UserAdapter {
    private final DatabaseReference databaseRef;
    private  static final String FRIENDS_TABLE = "Users";
    public friendAdapter() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(FRIENDS_TABLE);
    }

    @Override
    public <T> void create(T object, BaseDatabase.OperationCallback callback) {
        //todo 1. add in create group
        if (!(object instanceof User)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }
        if (object == null) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot empty")));
            return;
        }
    }

    @Override
    public void get(BaseDatabase.ListCallback callback) {
        //todo 1. add in get group based on user
    }

    @Override
    public <T> void update(String Id, T object, BaseDatabase.OperationCallback callback) {
        //todo 1. add in update group based on user friend ID
        // find friend based on other person friend id
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void delete(String Id, BaseDatabase.OperationCallback callback) {
        //todo 1. add in delete group
    }


    public void addFriendBasedOnKey(User user, String friendKey, BaseDatabase.OperationCallback callback) {
        String userId= user.getId();
        String username = user.getUsername();
        // First find the user who owns this friendKey
        databaseRef.orderByChild("friendKey").equalTo(friendKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            callback.onError(DatabaseError.fromException(
                                    new Exception("No user found with this friend key")));
                            return;
                        }

                        // Get the found user (friend to add)
                        DataSnapshot friendSnapshot = snapshot.getChildren().iterator().next();
                        String friendId = friendSnapshot.getKey();
                        String friendUsername = friendSnapshot.child("username").getValue(String.class);

                        // Now update both users' friend lists
                        DatabaseReference currentUserRef = databaseRef.child(userId).child("friends");
                        DatabaseReference friendUserRef = databaseRef.child(friendId).child("friends");

                        // Create update map for atomic operation
                        Map<String, Object> updates = new HashMap<>();

                        // Add friend to current user's list

                        updates.put(userId + "/friends/" + friendId, friendUsername);
                        Log.d("adding friend to user",userId + "/friends/" + friendId);
                        // Add current user to friend's list
                        updates.put(friendId + "/friends/" + userId, username);
                        Log.d("adding user to friend",friendId + "/friends/" + userId);

                        // Perform atomic update
                        databaseRef.updateChildren(updates)
                                .addOnSuccessListener(aVoid -> callback.onSuccess())
                                .addOnFailureListener(e -> callback.onError(DatabaseError.fromException(e)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error);
                    }
                });
    }

    public boolean ifAlreadyfriends(String userId, String friendId){
        final boolean[] alreadyFriends = {false};
        databaseRef.child(userId).child("friends").child(friendId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot friendCheckSnapshot) {
                        if (friendCheckSnapshot.exists()) {
                            alreadyFriends[0] = true;
                        }

                        // Check the reverse relationship too (optional but thorough)
                        databaseRef.child(friendId).child("friends").child(userId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot reverseCheckSnapshot) {
                                        if (reverseCheckSnapshot.exists()) {
                                            alreadyFriends[0] = true;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return alreadyFriends[0];
    }


}
