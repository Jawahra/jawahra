package com.example.jawahra;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainAuth extends AppCompatActivity {

    Button logBtn, signBtn, guestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auth);

        logBtn = findViewById(R.id.authLogBtn);
        signBtn = findViewById(R.id.authSignBtn);
        guestBtn = findViewById(R.id.authGuestBtn);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAuth.this, LoginActivity.class));
            }
        });

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAuth.this, AuthActivity.class));
            }
        });

        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAuth.this, CovidProtocolActivity.class));
                Toast.makeText(MainAuth.this, "Logged in as guest!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}