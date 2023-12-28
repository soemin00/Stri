package com.example.striwomensafety;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // Variable Declarations
    private TextView signUpTextView;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private TextView orSignUpWithTextView;
    private ImageView facebookImageView;
    private ImageView googleImageView;
    private ImageView whatsappImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Finding Views
        signUpTextView = findViewById(R.id.signup);
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.registerBtn);
        orSignUpWithTextView = findViewById(R.id.others);

        // Finding the social icons container
        View socialIconsContainer = findViewById(R.id.social_icons);

        // Finding social icons within the container
        facebookImageView = socialIconsContainer.findViewById(R.id.fb);
        googleImageView = socialIconsContainer.findViewById(R.id.google_new);
        whatsappImageView = socialIconsContainer.findViewById(R.id.whatsapp);

        // Setting Click Listeners
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                registerUser(username, email, password, confirmPassword);
            }
        });

        // You can add click listeners for social icons here if needed
    }

    // Method to handle user registration
    private void registerUser(String username, String email, String password, String confirmPassword) {
        // Add your registration logic here
        // Check if the passwords match, validate email format, etc.

        if (password.equals(confirmPassword)) {
            showToast("User registered successfully");
            // TODO: Add logic to save user details, e.g., connect to a server or update a local database
            finish(); // Finish the activity after successful registration
        } else {
            showToast("Passwords do not match");
        }
    }

    // Utility method to display Toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
