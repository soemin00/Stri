package com.example.striwomensafety;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmergencyButton extends AppCompatActivity {

    private ImageButton emergencyButton;
    private ImageView sidebarIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_button);

        // Find Views
        emergencyButton = findViewById(R.id.emergencyButton);
        sidebarIcon = findViewById(R.id.sidebarIcon);

        // Set Click Listeners
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle emergency button click
                handleEmergencyButtonClick();
            }
        });

        sidebarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle sidebar icon click
                handleSidebarIconClick();
            }
        });
    }

    private void handleEmergencyButtonClick() {
        // Add your logic for handling emergency button click
        Toast.makeText(this, "Emergency button clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleSidebarIconClick() {
        // Add your logic for handling sidebar icon click
        Toast.makeText(this, "Sidebar icon clicked", Toast.LENGTH_SHORT).show();
    }
}
