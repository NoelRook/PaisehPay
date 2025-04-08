package com.example.paisehpay.databaseHandler;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends BaseDatabase {

    private final DatabaseReference databaseRef;
    private static final String GROUP_TABLE = "Groups";

    public GroupAdapter() {
        super(GROUP_TABLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(GROUP_TABLE);
    }

    @Override
    public <T> void create(T object, OperationCallback callback) {
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
    public void get(ListCallback callback) {
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
    public <T> void update(String Id, T object, OperationCallback callback) {
        if (!(object instanceof Group)) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }

        Group group = (Group) object;
        databaseRef.child(Id).setValue(group.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    @Override
    public void delete(String Id, OperationCallback callback) {
        databaseRef.child(Id).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    public void getGroupsForUser(String userId, ListCallback callback) {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Group> userGroups = new ArrayList<>();
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);
                    if (group != null && (group.getCreatedBy().equals(userId) ||
                            (group.getMembers() != null && group.getMembers().containsKey(userId)))) {
                        group.setGroupId(groupSnapshot.getKey());
                        userGroups.add(group);
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

    public void addMemberToGroup(String groupId, String userId, String userName, OperationCallback callback) {
        databaseRef.child(groupId).child("members").child(userId).setValue(userName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    public void removeMemberFromGroup(String groupId, String userId, OperationCallback callback) {
        databaseRef.child(groupId).child("members").child(userId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    // Get groups created by a specific user
    public void getGroupsCreatedByUser(String userId, ListCallback callback) {
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

}