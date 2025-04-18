package com.example.paisehpay.activities;


import static android.content.ContentValues.TAG;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.UserAdapter;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SignIn extends AppCompatActivity {

    //import all ur buttons and ui used
    ImageView appIcon;

    ConstraintLayout loginLayout;

    EditText usernameText;
    EditText passwordText;

    Button loginButton;
    Button signupButton;
    Button changePasswordButton;
    CheckBox rememberMeBox;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize firebase
        // get preference manager to save the user preferenace
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preferenceManager = new PreferenceManager(this);
        // all the animation
        appIcon = findViewById(R.id.app_icon);
        ObjectAnimator moveLogoUp = ObjectAnimator.ofFloat(appIcon,"translationY",0f,-600f);
        moveLogoUp.setStartDelay(500);
        moveLogoUp.setDuration(1500);
        moveLogoUp.setInterpolator(new DecelerateInterpolator());
        moveLogoUp.start();

        loginLayout = findViewById(R.id.login_layout);
        loginLayout.setTranslationY(1000f);
        ObjectAnimator moveLoginUp = ObjectAnimator.ofFloat(loginLayout,"translationY",1000f,-300f);
        moveLoginUp.setStartDelay(500);
        moveLoginUp.setDuration(1500);
        moveLoginUp.setInterpolator(new DecelerateInterpolator());
        moveLoginUp.start();


        //motion listeners for username and password input
        usernameText = loginLayout.findViewById(R.id.username_input);
        passwordText = loginLayout.findViewById(R.id.password_input);
        String usernameString = usernameText.getText().toString();
        String passwordString = passwordText.getText().toString();
        Log.i(usernameString,passwordString);

        loginButton = loginLayout.findViewById(R.id.login_button); // find login button on activity
        // Login button click listener
        loginButton.setOnClickListener(view -> {
            String email = usernameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignIn.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }


            // Authenticate with Firebase
            userLogin(email, password);
        }); // end login functionality

        //sign up button to lead to sign up page
        signupButton = loginLayout.findViewById(R.id.sign_up_button);
        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        });

        //change password button to lead to change password page
        changePasswordButton = loginLayout.findViewById(R.id.forget_password_button);
        changePasswordButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
            builder.setTitle("Reset Password");
            builder.setMessage("Enter your email to receive a password reset link");

            // Set up the input
            final EditText input = new EditText(SignIn.this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            input.setHint("Email");
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Send", (dialog, which) -> {
                String email = input.getText().toString().trim();
                if (!email.isEmpty()) {
                    sendPasswordResetEmail(email);
                } else {
                    Toast.makeText(SignIn.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();
        });



        // remember me function
        rememberMeBox = findViewById(R.id.remember_me_check_box);

        rememberMeBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.rememberMe(isChecked);
        });

        User savedUser = preferenceManager.getUser();
        if (savedUser != null && preferenceManager.getRememberMe()) {
            Intent intent = new Intent(SignIn.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }






        //JK testing get all users
        // the reason this is here is because i have not figured out how to start unit testing for database yet. to do in future build
        // pls do not @ me
        UserAdapter adapter = new UserAdapter();
        adapter.get(new OperationCallbacks.ListCallback<User>() {
            @Override
            public void onListLoaded(List<User> users) {
                // Handle the list of users
                for (User user : users) {
                    Log.d("User", user.getUsername() + " - " + user.getEmail());
                }
            }
            @Override
            public void onError(DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", error.getMessage());
            }
        });



    }



    // todo. seperate login functionality to invoke remember me functionality

    // Helper method to send password reset email
    private void sendPasswordResetEmail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignIn.this, "Password reset email sent to " + email, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Email sent.");
                    } else {
                        Toast.makeText(SignIn.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error sending email", task.getException());
                    }
                });
    }

    private void userLogin(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("cur user", user.getUid());
                        if (user != null) {
                            // Get additional user data from Realtime Database
                            mDatabase
                                    .child("Users")
                                    .child(user.getUid()).
                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    User userData = dataSnapshot.getValue(User.class);
                                    Log.d("asdasd",dataSnapshot.toString());
                                    userData.setId(dataSnapshot.getKey());


                                    if (userData != null) {
                                        // Successful login with user data
                                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                                        Log.d("userData", userData.toString());

                                        preferenceManager.saveUser(userData);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("DATA",databaseError.toString());

                                    Toast.makeText(SignIn.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignIn.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }


}