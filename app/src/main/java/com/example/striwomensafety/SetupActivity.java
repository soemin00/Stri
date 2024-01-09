package com.example.striwomensafety;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {

    private EditText contact1EditText;
    private EditText contact2EditText;
    private EditText contact3EditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Find Views
        contact1EditText = findViewById(R.id.contact1EditText);
        contact2EditText = findViewById(R.id.contact2EditText);
        contact3EditText = findViewById(R.id.contact3EditText);
        saveButton = findViewById(R.id.saveButton);

        // Set Click Listener for Save Button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the entered contact numbers in SharedPreferences
                saveEmergencyContacts();
            }
        });
    }

    private void saveEmergencyContacts() {
        // Retrieve entered contact numbers
        String contact1 = contact1EditText.getText().toString().trim();
        String contact2 = contact2EditText.getText().toString().trim();
        String contact3 = contact3EditText.getText().toString().trim();

        // Check if all three contact numbers are provided
        if (contact1.isEmpty() || contact2.isEmpty() || contact3.isEmpty()) {
            showToast("Please enter all three emergency contact numbers.");
            return;
        }

        // Store contact numbers in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EmergencyContact1", contact1);
        editor.putString("EmergencyContact2", contact2);
        editor.putString("EmergencyContact3", contact3);
        editor.apply();

        // Indicate that setup is completed
        sharedPreferences.edit().putBoolean("SetupCompleted", true).apply();

        // Finish the setup activity
        finish();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
