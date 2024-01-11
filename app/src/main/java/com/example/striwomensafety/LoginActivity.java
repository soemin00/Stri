package com.example.striwomensafety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;

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

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // Finding Views
        signInTextView = findViewById(R.id.signin);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbutton);
        forgotPasswordTextView = findViewById(R.id.forgotPassword);

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

        // Check if the user is already logged in
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, navigate to the main activity
            navigateToEmergencyButton();
        }
    }

    // Method to handle user login
    private void loginUser(String username, String password) {
        auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            showToast("Login successful. Welcome, " + user.getEmail());
                            navigateToEmergencyButton();
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                showToast("Login failed: " + exception.getMessage());
                            } else {
                                showToast("Login failed. Please check your credentials.");
                            }
                        }
                    }
                });
    }

    private void navigateToEmergencyButton() {
        Intent intent = new Intent(LoginActivity.this, EmergencyButton.class);
        startActivity(intent);
        finish();
    }

    // Method to handle "Forgot Password" action
    public void forgotPasswordClicked(View view) {
        showToast("Forgot Password clicked");

        // TODO: Add your forgot password logic here

    }

    public void signUpClicked(View view) {
        showToast("Sign Up clicked");
        Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(signUpIntent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
