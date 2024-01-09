package com.example.striwomensafety;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class EmergencyButton extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 2; // or any other value

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ImageButton  emergencyButton;
    private ImageView sidebarIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if setup is completed
        if (!isSetupCompleted()) {
            // If setup is not completed, redirect to the setup activity
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);
            finish(); // finish this activity to prevent the user from going back
            return;
        }
        checkSmsPermission();
        // Check and request location permissions
        checkLocationPermission();

        setContentView(R.layout.activity_emergency_button);

        // Find Views
        emergencyButton = findViewById(R.id.emergencyButton);
        sidebarIcon = findViewById(R.id.sidebarIcon);

        // Set Click Listeners
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEmergencyButtonClick();
                showToast("Emergency");
            }
        });
        sidebarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showToast("Side bar clicked");
            }
        });
    }

    private boolean isSetupCompleted() {
        // Check if setup is completed by verifying if the numbers are stored in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sharedPreferences.contains("EmergencyContact1")
                && sharedPreferences.contains("EmergencyContact2")
                && sharedPreferences.contains("EmergencyContact3");
    }

    private void handleEmergencyButtonClick() {
        // Check location permissions
        showToast("This also works");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Check if location services are enabled
            if (isLocationServicesEnabled()) {
                // Get current location
                showToast("Location is enabled");
                Location currentLocation = getCurrentLocation();

                // You can now use the 'currentLocation' to send location details in your emergency message
                if (currentLocation != null) {
                    double latitude = currentLocation.getLatitude();
                    double longitude = currentLocation.getLongitude();

                    // Create a Google Maps link
                    String googleMapsLink = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
                    // Send emergency message
                    sendEmergencyMessage(googleMapsLink);
                }
            } else {
                showToast("Location services are not enabled. Please enable them in settings.");
            }
        } else {
            // Request location permissions
            checkLocationPermission();
            // showToast("Location permissions not granted."); // This line is now commented
        }
    }

    private boolean isLocationServicesEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            handleEmergencyButtonClick();
        }
    }
    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Handle your SMS-related logic
            handleEmergencyButtonClick();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleEmergencyButtonClick();
            } else {
                showToast("SMS permission denied. Cannot send emergency message.");
            }
        }

        // Call super method
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private Location getCurrentLocation() {
        // Check location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
 showToast("OKI");
            if (locationManager != null) {

                showToast("location not null ");
                try {
                    // Check if GPS provider is enabled
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        showToast("Location GPS enabled");
                        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        // If GPS provider is not enabled, try using NETWORK_PROVIDER
                        showToast("Location network enabled");
                        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    } else {
                        // Handle the case when neither GPS nor NETWORK provider is enabled
                        showToast("Location providers are not enabled.");
                    }
                } catch (SecurityException e) {
                    showToast("ERROR");
                    e.printStackTrace();

                }
            }
        } else {
            checkLocationPermission();
        }
        showToast("NULL return");
        return null;
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendEmergencyMessage(String locationLink) {
        // Retrieve contact numbers from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String contactNumber1 = sharedPreferences.getString("EmergencyContact1", null);
        String contactNumber2 = sharedPreferences.getString("EmergencyContact2", null);
        String contactNumber3 = sharedPreferences.getString("EmergencyContact3", null);

        // Check if valid contact numbers are available
        if (contactNumber1 != null && !contactNumber1.isEmpty() &&
                contactNumber2 != null && !contactNumber2.isEmpty() &&
                contactNumber3 != null && !contactNumber3.isEmpty()) {

            // Use SmsManager to send the emergency message
            SmsManager smsManager = SmsManager.getDefault();
            String message = "Emergency! I need help. My location: " + locationLink;

            // Send the message to all three contact numbers
            smsManager.sendTextMessage(contactNumber1, null, message, null, null);
            smsManager.sendTextMessage(contactNumber2, null, message, null, null);
            smsManager.sendTextMessage(contactNumber3, null, message, null, null);

            // Display a confirmation message
            showToast("Emergency message sent to contacts.");
        } else {
            showToast("Incomplete setup. Please set three emergency contact numbers in settings.");
        }
    }
}
