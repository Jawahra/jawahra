package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Page2 extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        button=findViewById(R.id.back_btn);
        button.setOnClickListener(v -> {
            Intent i = new Intent(Page2.this, MainActivity.class);
            startActivity(i);
        });

    }
}
