package com.example.paisehpay.databaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.databaseHandler.Interfaces.FirebaseDatabaseAdapter;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAdapter extends FirebaseDatabaseAdapter<Group> {
    public GroupAdapter() {
        super("Groups");
    }

    @Override
    public void create(Group object, OperationCallbacks.OperationCallback callback) throws IllegalArgumentException {
        if (!(object instanceof Group)) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }

        Group group = (Group) object;
        String groupId = databaseRef.push().getKey();
        if (groupId == null) {
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate group ID")));
            return;
        }

        group.setGroupId(groupId);
        databaseRef.child(groupId).setValue(group.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    @Override
    public void get(OperationCallbacks.ListCallback<Group> callback) {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Group> groups = new ArrayList<>();
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);
                    if (group != null) {
                        group.setGroupId(groupSnapshot.getKey());
                        groups.add(group);
                    }
                }
                callback.onListLoaded(groups);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    @Override
    public void update(String id, Group object, OperationCallbacks.OperationCallback callback) {
        if (!(object instanceof Group)) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }

        Group group = (Group) object;
        databaseRef.child(id).setValue(group.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    @Override
    public void delete(String id, OperationCallbacks.OperationCallback callback) {
        databaseRef.child(id).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    // get groups for the current user
    public void getGroupsForUser(String userId, OperationCallbacks.ListCallback callback) {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Group> userGroups = new ArrayList<>();
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);
                    if (group != null) {
                        boolean isCreatedByUser = group.getCreatedBy() != null && group.getCreatedBy().equals(userId);
                        boolean isMember = group.getMembers() != null && group.getMembers().containsKey(userId);

                        if (isCreatedByUser || isMember) {
                            group.setGroupId(groupSnapshot.getKey());
                            userGroups.add(group);
                        }
                    }
                }
                callback.onListLoaded(userGroups);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    // adding specific members to group
    public void addMemberToGroup(String groupId, String userId, String userName, OperationCallbacks.OperationCallback callback) {
        Log.d("databaseside", groupId+" "+userId+" "+userName);
        databaseRef.child(groupId).child("members").child(userId).setValue(userName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }
    // remove member from group based on the userid and groupid
    public void removeMemberFromGroup(String groupId, String userId, OperationCallbacks.OperationCallback callback) {
        databaseRef.child(groupId).child("members").child(userId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    // Get groups created by a specific user (may not need)
    public void getGroupsCreatedByUser(String userId, OperationCallbacks.ListCallback callback) {
        databaseRef.orderByChild("createdBy").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Group> groups = new ArrayList<>();
                        for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                            Group group = groupSnapshot.getValue(Group.class);
                            if (group != null) {
                                group.setGroupId(groupSnapshot.getKey());
                                groups.add(group);
                            }
                        }
                        callback.onListLoaded(groups);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(databaseError);
                    }
                });
    }
    // return all members in the group
    public void getGroupMates(String groupId , OperationCallbacks.ListCallback<Map<String, String>> callback){
        databaseRef
                .child(groupId)
                .child("members")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> members = new HashMap<>();
                for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                    String userId = memberSnapshot.getKey();
                    String userName = memberSnapshot.getValue(String.class);
                    members.put(userId, userName);
                }
                callback.onListLoaded(Collections.singletonList(members));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }


}