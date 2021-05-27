package com.example.jawahra;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LaunchActivity extends AppCompatActivity {
    /*VARIABLES*/
    private static int LAUNCH_SCREEN = 5000;
    private SharedPreferences spObj;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Opts the activity to be set to fullscreen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launch);

        /*Creating the Launch Screen*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spObj = new SharedPreferences(LaunchActivity.this);
                if(spObj.getApp_runFirst().equals("FIRST"))
                {
                    // If this is the user's first time launching the application
                    startActivity(new Intent(LaunchActivity.this, OnboardingIntro.class));
                    finish();

                    // Sets status to not their first time anymore
                    spObj.setApp_runFirst("NO");
                }
                else
                {
                    //If they are a returning user
                    if (user != null) {
                        // If the user has an account and is already signed in
                        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    } else {
                        // If the user has no account or not signed in
                        startActivity(new Intent(LaunchActivity.this, MainAuth.class));
                    }
                    finish();
                }
            }
        }, LAUNCH_SCREEN);
    }
}