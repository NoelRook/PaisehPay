package com.example.paisehpay;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.paisehpay.blueprints.User;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


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

        String usernameStringCorrect = "username"; //take from db
        String passwordStringCorrect = "password"; //take from db

        //login button to lead to home page
        /*loginButton = loginLayout.findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            if (usernameString.equals(usernameString) & passwordString.equals(passwordString)) { //change when got db
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else {
                Toast.makeText(SignIn.this,R.string.wrong_login_details,Toast.LENGTH_LONG).show();
                //show warning msg to re-enter details
            }
        }); */
        loginButton = loginLayout.findViewById(R.id.login_button);
        // Login button click listener
        loginButton.setOnClickListener(view -> {
            String email = usernameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignIn.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Authenticate with Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Check if email is verified (optional)
                                /*if (!user.isEmailVerified()) {
                                    Toast.makeText(SignIn.this, "Please verify your email first.", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    return;
                                }*/

                                // Get additional user data from Realtime Database
                                mDatabase.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User userData = dataSnapshot.getValue(User.class);
                                        if (userData != null) {
                                            // Successful login with user data
                                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                                            Log.d("userData", userData.toString());
                                            //intent.putExtra("USER_DATA", (CharSequence) userData);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
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
        });

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
        changePasswordButton.setOnClickListener(view ->{
            Intent intent = new Intent(SignIn.this, ChangePassword.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        });


        //JK testing get all users
        // the reason this is here is because i have not figured out how to start unit testing for database yet. to do in future build
        // pls do not @ me
        UserAdapter adapter = new UserAdapter();
        adapter.get(new BaseDatabase.ListCallback<User>() {
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


        /*User newUser = new User("new id","John Doe", "john@example.com");
        adapter.createUser(newUser, new FirebaseAdapter.OperationCallback() {
            @Override
            public void onSuccess() {
                // User created successfully
                Log.d("Success", "User created");
            }

            @Override
            public void onError(DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", error.getMessage());
            }
        });

        User updatedUser = new User("id here","John Updated", "john.updated@example.com");
        adapter.updateUser("existingUserId", updatedUser, new FirebaseAdapter.OperationCallback() {
            @Override
            public void onSuccess() {
                // User updated successfully
                Log.d("Success", "User updated");
            }

            @Override
            public void onError(DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", error.getMessage());
            }
        });

        //delete
        adapter.deleteUser(updatedUser, new FirebaseAdapter.UserOperationCallback() {
            @Override
            public void onSuccess() {
                // User deleted successfully
                Log.d("Success", "User deleted");
            }

            @Override
            public void onError(DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", error.getMessage());
            }
        });*/
        // end testing

    }

    // <!-- TODO: 1. function when press login page, check db for credentials  -->


}