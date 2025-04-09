package com.example.paisehpay.databaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;

public class UserAdapter extends BaseDatabase  {
    private DatabaseReference databaseRef;
    private  static final String USER_TABLE = "Users";

    public UserAdapter(){
        super(USER_TABLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(USER_TABLE);
    }


    @Override
    public <T> void create(T object, final OperationCallback callback) {
        if (!(object instanceof User)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }
        if (object == null) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot empty")));
            return;
        }
        User user = (User) object;
        String userId = databaseRef.push().getKey();
        if (userId == null) {
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate user ID")));
            return;
        }
        //user.setUserId(userId);

        user.setFriendKey(genFriendKey(user.getEmail()));
        databaseRef.child(userId).setValue(user.toMap())
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
        databaseRef.addValueEventListener(new ValueEventListener() {
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
    public <T> void update(String userId, T object, final OperationCallback callback) {
        if (!(object instanceof User)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }
        if (userId == null || userId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot be null or empty")));
            return;
        }
        User user = (User) object;
        databaseRef.child(userId).setValue(user.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override @NonNull
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

        databaseRef.child(userId).removeValue()
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
    public String genFriendKey(String email){
        Date now = Calendar.getInstance().getTime();
        String key = now.getTime() + email;

        CRC32 crc = new CRC32();
        crc.update(key.getBytes());
        long hash = crc.getValue();

        // Convert to base36 (0-9, a-z) to make it alphanumeric
        String base36 = Long.toString(hash, 36);

        // Ensure exactly 8 characters (pad with zeros or truncate)
        if (base36.length() > 8) {
            Log.d("genKey",base36.substring(0, 8));

            return base36.substring(0, 8);
        } else {
            Log.d("genKey",String.format("%8s", base36).replace(' ', '0'));
            return String.format("%8s", base36).replace(' ', '0');
        }
    }

    //get single user
}
