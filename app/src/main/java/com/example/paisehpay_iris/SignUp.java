package com.example.paisehpay_iris;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {

    ConstraintLayout signupLayout;
    Button signInButton;
    Button signUpButton;

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


        //sign up button lead to sign in page
        signUpButton = signupLayout.findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        });

    }

    // <!-- TODO: 1. checks if data is all filled  -->
    // <!-- TODO: 2. check if data is correct type  -->
    // <!-- TODO: 3. check if password matches  -->
    // <!-- TODO: 4. stuff info in db  -->
    // <!-- TODO: 5. hen all gd, change text on sign up button to say 'you have successfully signed up!' then intent after a time delay  -->

}