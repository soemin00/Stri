package com.example.striwomensafety;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

public class EmergencyButton extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ImageButton emergencyButton;
    private ImageView sidebarIcon;

    private boolean emergencyConfirmed = false;

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

        // Check and request SMS and location permissions
        checkSmsAndLocationPermissions();

        setContentView(R.layout.activity_emergency_button);

        // Find Views
        emergencyButton = findViewById(R.id.emergencyButton);
        sidebarIcon = findViewById(R.id.sidebarIcon);

        // Set Click Listeners
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        sidebarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Sidebar clicked");
            }
        });
    }

    private boolean isSetupCompleted() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sharedPreferences.contains("EmergencyContact1")
                && sharedPreferences.contains("EmergencyContact2")
                && sharedPreferences.contains("EmergencyContact3");
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to send an emergency message?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                emergencyConfirmed = true;
                handleEmergencyButtonClick();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing if the user clicks "No"
            }
        });
        builder.show();
    }

    private void handleEmergencyButtonClick() {
        if (!emergencyConfirmed) {
            // If emergency is not confirmed, do nothing
            return;
        }

        // Check location permission and enable location services
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (isLocationServicesEnabled()) {
                sendEmergencyMessage();
            } else {
                showToast("Location services are not enabled. Please enable them in settings.");
            }
        } else {
            checkSmsAndLocationPermissions();
        }
    }

    private boolean isLocationServicesEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void checkSmsAndLocationPermissions() {
        // Check SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        }

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SMS permission granted, check location permission
                checkLocationPermission();
            } else {
                showToast("SMS permission denied. Cannot send emergency message.");
            }
        } else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, handle emergency button click
                handleEmergencyButtonClick();
            } else {
                showToast("Location permission denied. Cannot get current location.");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Location permission granted, handle emergency button click
            handleEmergencyButtonClick();
        }
    }

    private Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            try {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    showToast("Location providers are not enabled.");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void sendEmergencyMessage() {
        Location currentLocation = getCurrentLocation();

        if (currentLocation != null) {
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();

            String googleMapsLink = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
            sendEmergencyMessage(googleMapsLink);
        } else {
            showToast("Unable to get current location.");
        }
    }

    private void sendEmergencyMessage(String locationLink) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String contactNumber1 = sharedPreferences.getString("EmergencyContact1", null);
        String contactNumber2 = sharedPreferences.getString("EmergencyContact2", null);
        String contactNumber3 = sharedPreferences.getString("EmergencyContact3", null);

        if (contactNumber1 != null && !contactNumber1.isEmpty() &&
                contactNumber2 != null && !contactNumber2.isEmpty() &&
                contactNumber3 != null && !contactNumber3.isEmpty()) {

            SmsManager smsManager = SmsManager.getDefault();
            String message = "Emergency! I need help. My location: " + locationLink;

            // Use a loop to send messages to each contact number
            String[] contactNumbers = {contactNumber1, contactNumber2, contactNumber3};
            for (int i = 0; i < contactNumbers.length; i++) {
                sendSms(smsManager, contactNumbers[i], message, i);
            }

            showToast("Emergency message sent to contacts.");
        } else {
            showToast("Incomplete setup. Please set three emergency contact numbers in settings.");
        }
    }

    private void sendSms(SmsManager smsManager, String contactNumber, String message, int index) {
        Intent sentIntent = new Intent("SMS_SENT");
        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, index, sentIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent deliveredIntent = new Intent("SMS_DELIVERED");
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, index, deliveredIntent, PendingIntent.FLAG_IMMUTABLE);

        smsManager.sendTextMessage(contactNumber, null, message, sentPendingIntent, deliveredPendingIntent);
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
