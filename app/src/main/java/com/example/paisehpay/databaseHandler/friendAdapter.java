package com.example.paisehpay.databaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class friendAdapter extends UserAdapter{
    public friendAdapter() {

    }


    public void addFriendBasedOnKey(User user, String friendKey, OperationCallbacks.OperationCallback callback) {
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


    public void twoWayfriendRemoval(String curUserId, String friendId){
        removeFriends(curUserId,friendId, new com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks.OperationCallback() {
            @Override
            public void onSuccess() {
                // User deleted successfully
                Log.d("Success", "friend 1 deleted");
            }

            @Override
            public void onError(DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", error.getMessage());
            }
        });
        removeFriends(friendId,curUserId, new com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks.OperationCallback() {
            @Override
            public void onSuccess() {
                // User deleted successfully
                Log.d("Success", "friend 2 deleted");
            }

            @Override
            public void onError(DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", error.getMessage());
            }
        });


    }


    //remove friend
    private void removeFriends(String curUserId, String friendId, final com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks.OperationCallback callback){
        databaseRef.child(curUserId).child("friends").child(friendId).removeValue()
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

    //get all friends
    public void getFriendsForUser(String curUserId, OperationCallbacks.ListCallback<User> callback) {
        databaseRef.child(curUserId).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot friendsSnapshot) {
                if (!friendsSnapshot.exists()) {
                    // User has no friends, return empty list
                    callback.onListLoaded(new ArrayList<>());
                    return;
                }
                // Get all friend IDs
                Map<String, String> friendIds = (Map<String, String>) friendsSnapshot.getValue();
                if (friendIds == null || friendIds.isEmpty()) {
                    callback.onListLoaded(new ArrayList<>());
                    return;
                }

                // Create list to store friend User objects
                List<User> friendsList = new ArrayList<>();

                // Counter to track how many friends we've fetched
                final int[] friendsFetched = {0};
                final int totalFriends = friendIds.size();

                for (String friendId : friendIds.keySet()) {
                    // Get each friend's full user data
                    databaseRef.child(friendId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            User friend = userSnapshot.getValue(User.class);
                            if (friend != null) {
                                friend.setUserId(userSnapshot.getKey()); // Set the Firebase-generated ID
                                friendsList.add(friend);
                            }

                            friendsFetched[0]++;
                            // When all friends are fetched, return the list
                            if (friendsFetched[0] == totalFriends) {
                                callback.onListLoaded(friendsList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            friendsFetched[0]++;
                            // If any fetch fails, we'll still return what we have when all attempts complete
                            if (friendsFetched[0] == totalFriends && !friendsList.isEmpty()) {
                                callback.onListLoaded(friendsList);
                            } else if (friendsFetched[0] == totalFriends) {
                                callback.onError(error);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    public void getSingleUser(String userId, final OperationCallbacks.SingleObjectCallback callback) {
        databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setUserId(dataSnapshot.getKey()); // Set the Firebase-generated ID
                        callback.onObjectLoaded(user);
                    } else {
                        callback.onError(DatabaseError.fromException(new Exception("User data corrupted")));
                    }
                } else {
                    callback.onError(DatabaseError.fromException(new Exception("User not found")));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }


}
