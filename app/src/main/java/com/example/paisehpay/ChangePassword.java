package com.example.paisehpay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChangePassword extends AppCompatActivity {
    //all your imports
    EditText digit1Text;
    EditText digit2Text;
    EditText digit3Text;
    EditText digit4Text;
    EditText emailText;
    EditText passwordText;
    EditText passwordReenterText;

    TextView changePasswordText;
    Button resendCodeButton;

    ConstraintLayout changePasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.change_password), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //set change password prompt
        changePasswordLayout = findViewById(R.id.change_password_layout);

        String emailDatabase = "xyz@gmail.com"; //will get from db
        emailText = changePasswordLayout.findViewById(R.id.email_input);
        changePasswordText = changePasswordLayout.findViewById(R.id.change_password_prompt);
        resendCodeButton = changePasswordLayout.findViewById(R.id.resend_code);

        //key in correct code
        digit1Text = changePasswordLayout.findViewById(R.id.digit_input_1st);
        digit2Text = changePasswordLayout.findViewById(R.id.digit_input_2nd);
        digit3Text = changePasswordLayout.findViewById(R.id.digit_input_3rd);
        digit4Text = changePasswordLayout.findViewById(R.id.digit_input_4th);
        int digit1Correct = 1; //db
        int digit2Correct = 1; //db
        int digit3Correct = 1; //db
        int digit4Correct = 1; //db

        //change password
        passwordText = changePasswordLayout.findViewById(R.id.new_password);
        passwordReenterText = changePasswordLayout.findViewById(R.id.new_password_reenter);


        //if email doesn't exist in db, we prompt the user to create a new account
        //else prompt user to create a new text

        //checks for email changed after user taps or keys in text in textview
        emailText.setOnFocusChangeListener((view, hasFocus) -> { //there should be a better way to write this
            if (!hasFocus){
                String emailString = emailText.getText().toString().trim();
                if (emailString.equals(emailString) & !emailString.equals(R.string.blank)){ //will change with db
                    changePasswordText.setText(getText(R.string.change_password_prompt)+ emailString); //some formatting issues
                    digit1Text.setVisibility(View.VISIBLE);
                    digit2Text.setVisibility(View.VISIBLE);
                    digit3Text.setVisibility(View.VISIBLE);
                    digit4Text.setVisibility(View.VISIBLE);
                    resendCodeButton.setVisibility(View.VISIBLE);
                } else {
                    changePasswordText.setText(R.string.invalid_email);
                }
            }
        });

        //if user finishes keying in email, they will naturally tap outside the box and it will then process the text
        changePasswordLayout.setOnClickListener(view -> emailText.clearFocus());

        //process last button input
        digit4Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String digit4String = digit4Text.getText().toString();
                passwordText.setVisibility(View.VISIBLE);
                passwordReenterText.setVisibility(View.VISIBLE);

            }
        });

        passwordReenterText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus){
                String passwordInput = passwordText.getText().toString();
                String passwordReinput = passwordReenterText.getText().toString();
                if (checkPasswordCorrect(passwordInput,passwordReinput)) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(ChangePassword.this, SignIn.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        finish();
                    }, 2000); // 3 seconds delay

                }

            }
        });

    }

    private boolean checkPasswordCorrect(String passwordInput, String passwordReinput) {
        if (!passwordInput.equals(passwordReinput)) {
            Toast.makeText(ChangePassword.this,R.string.password_dont_match, Toast.LENGTH_LONG).show();
            return false;
        } else {
            Toast.makeText(ChangePassword.this,R.string.password_match, Toast.LENGTH_LONG).show();
            return true;

        }
    }

    // <!-- TODO: 1. check code correct (all 4 digits with db) then after that trigger user to key in new password - digit4text.addtextchangedlistener is a substitute for now-->


}