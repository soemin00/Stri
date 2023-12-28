package com.example.striwomensafety;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    // Duration of the splash screen display in milliseconds
    private static final int SPLASH_DISPLAY_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Using a handler to delay the transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the main activity
                Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                // Start the main activity
                startActivity(mainIntent);
                // Finish the splash screen activity to prevent it from being revisited
                finish();
            }
        }, SPLASH_DISPLAY_DURATION);
    }
}
