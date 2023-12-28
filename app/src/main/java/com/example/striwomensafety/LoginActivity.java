package com.example.striwomensafety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    // Variable Declarations
    private TextView signInTextView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private MaterialButton loginButton;
    private TextView forgotPasswordTextView;
    private TextView orSignInWithTextView;
    private ImageView facebookImageView;
    private ImageView twitterImageView;
    private ImageView googleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Finding Views
        signInTextView = findViewById(R.id.signin);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbutton);
        forgotPasswordTextView = findViewById(R.id.forgotPassword);
        orSignInWithTextView = findViewById(R.id.orSignInWith);
        facebookImageView = findViewById(R.id.facebookImage);
        twitterImageView = findViewById(R.id.whatsappImage);
        googleImageView = findViewById(R.id.googleImage);

        // Setting Click Listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(username, password);
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordClicked(view);
            }
        });

        // Adding click listener for "Sign Up" link
        TextView signUpTextView = findViewById(R.id.signUpMessage);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpClicked(view);
            }
        });
    }

    // Method to handle user login
    private void loginUser(String username, String password) {
        showToast("Logging in with Username: " + username + ", Password: " + password);
        // Your login logic goes here

        // Assuming login is successful, you can finish the LoginActivity
        finish();
    }

    // Method to handle "Forgot Password" action
    public void forgotPasswordClicked(View view) {
        showToast("Forgot Password clicked");

        // TODO: Add your forgot password logic here
        // For example, you can launch a new activity for password recovery or show a dialog.
        // Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        // startActivity(forgotPasswordIntent);
    }

    // Method to handle "Sign Up" action
    public void signUpClicked(View view) {
        showToast("Sign Up clicked");

        // TODO: Add your sign-up logic here
        // For example, you can launch the SignUpActivity
        Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(signUpIntent);
    }

    // Utility method to display Toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
