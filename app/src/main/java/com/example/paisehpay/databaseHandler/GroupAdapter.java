package com.example.paisehpay.databaseHandler;

import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends BaseDatabase{

    private final DatabaseReference databaseRef;
    private  static final String GROUP_TABLE = "Groups";
    public GroupAdapter() {
        super(GROUP_TABLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(GROUP_TABLE);
    }

    @Override
    public <T>void create(T object, OperationCallback callback) {
        //todo 1. add in create group
        if (!(object instanceof Group)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
        }
        if (object == null) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Group ID cannot empty")));
            return;
        }

        String groupId = databaseRef.push().getKey();
        if (groupId == null) {
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate user ID")));
            return;
        }

        databaseRef.child(groupId).setValue(object)
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

    @Override
    public void get(ListCallback callback) {
        //todo 1. add in get group based on user
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Group> groups = new ArrayList<>();
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);
                    if (group != null) {
                        group.setGroupId(groupSnapshot.getKey()); // Set the Firebase-generated ID
                        groups.add(group);
                    }
                }
                callback.onListLoaded(groups);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    @Override
    public <T> void update(String Id, T object, OperationCallback callback) {
        if (!(object instanceof Group)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
        }
        //todo 1. add in update group based on user
    }

    @Override
    public void delete(String Id, OperationCallback callback) {
        //todo 1. add in delete group
    }
}
