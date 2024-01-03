package com.example.striwomensafety;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        signUpTextView = findViewById(R.id.signup);
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.registerBtn);
        orSignUpWithTextView = findViewById(R.id.others);

        View socialIconsContainer = findViewById(R.id.social_icons);
        facebookImageView = socialIconsContainer.findViewById(R.id.fb);
        googleImageView = socialIconsContainer.findViewById(R.id.google_new);
        whatsappImageView = socialIconsContainer.findViewById(R.id.whatsapp);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RegisterActivity", "registerUser method called");

                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Check for empty fields
                if (email.isEmpty() || password.isEmpty()) {
                    showToast("Email and password cannot be empty");
                    return;
                }

                Log.d("RegisterActivity", "Email: " + email);
                Log.d("RegisterActivity", "Password: " + password);

                registerUser(username, email, password, confirmPassword);
            }
        });
    }

    private void registerUser(String username, String email, String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showToast("User registered successfully");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // You can update UI or perform additional actions after successful registration
                                finish();
                            } else {
                                Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                                task.getException().printStackTrace();

                                // Handle FirebaseAuthUserCollisionException
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    showToast("Email already in use. Please log in.");
                                    // Navigate to the login page
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish(); // Optional: Close the current signup activity
                                } else {
                                    showToast("Authentication failed: " + task.getException().getMessage());
                                }
                            }
                        }
                    });
        } else {
            showToast("Passwords do not match");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
