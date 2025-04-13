package com.example.paisehpay.activities;

import static android.icu.text.DisplayOptions.DisplayLength.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    ConstraintLayout signupLayout;
    Button signInButton;
    Button signUpButton;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_up), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        signupLayout = findViewById(R.id.sign_up_layout);

        //accidentally sign up button, return to sign in page
        signInButton = signupLayout.findViewById(R.id.accidentally_sign_up_button);
        signInButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        });
        //get reference to widgets
        EditText usernameinp = findViewById(R.id.username_input);
        //String username = usernameinp.getText().toString();
        EditText emailinp = findViewById(R.id.email_input);

        EditText passwordinp = findViewById(R.id.password_input);
        //String password = passwordinp.getText().toString();
        EditText passwordreinp = findViewById(R.id.password_reinput);
        //String passwordre = passwordreinp.getText().toString();



        //sign up button lead to sign in page
        signUpButton = signupLayout.findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(view -> {
            String username = usernameinp.getText().toString();
            String email = emailinp.getText().toString().trim().replaceAll("\\p{Cf}", "");
            String password = passwordinp.getText().toString();
            String passwordre = passwordreinp.getText().toString();
            //if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            if (email.isEmpty() || !isValidEmail(email)){
                emailinp.setError("Valid email required");
                String message = Boolean.toString(isValidEmail(email));
                Log.e("SIGNUP_DEBUG", message);
                Log.e("SIGNUP_DEBUG", Boolean.toString(email.isEmpty()));
                return;
            }
            if (username.isEmpty()){
                usernameinp.setError("Username cannot be empty");
                return;
            }
            if (password.length() < 8){
                passwordinp.setError("Password must be at least 8 characters");
                return;
            }
            if (!password.equals(passwordre)){
                passwordreinp.setError("Passwords do not match");
                return;
            }

            checkUsernameUnique(username, isUnique -> {
                /*if (!isUnique) {
                    usernameinp.setError("Username already exists");
                    return;
                }*/
                //Create user in Firebase Auth
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //save additional user data to Realtime DB
                    saveUserToDatabase(username, email);
                } else{
                    Toast.makeText(SignUp.this, "Sign up failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });


            //after user clicks sign up button, they are redirected to the log in screen
            //Intent intent = new Intent(SignUp.this, SignIn.class);
            //startActivity(intent);
            //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            //finish();
            });
        });
    }

    private boolean isValidEmail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void checkUsernameUnique(String username, Consumer<Boolean> callback){
        FirebaseDatabase.getInstance().getReference("Username").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                callback.accept(!snapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SignUp.this, "Database error", Toast.LENGTH_SHORT).show();
                callback.accept(false);
            }
        });
    }

    private void saveUserToDatabase(String username, String email){


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null) return;
        UserAdapter adapter = new UserAdapter();
        //Store under "Users/{uid}"
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        User newUser = new User(
                user.getUid(),  // Set the Firebase UID as the user ID
                email,
                username,
                adapter.genFriendKey(email),  // Generate friend key
                null  // Initialize friends list as null
        );



        usersRef.child(user.getUid()).setValue(newUser).addOnSuccessListener(aVoid -> {
            //also store a reverse mapping for username uniqueness checks
            DatabaseReference usernamesRef = FirebaseDatabase.getInstance().getReference("Username");

            usernamesRef.child(user.getUid()).setValue(username).addOnSuccessListener(aVoid2 -> {

                //navigate to login screen
                startActivity(new Intent(SignUp.this, SignIn.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            });
        })
        .addOnFailureListener(e -> {
            Toast.makeText(SignUp.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
            user.delete(); //Rollback auth creation
        });

    }

    }
