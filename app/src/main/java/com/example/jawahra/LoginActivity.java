package com.example.jawahra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    EditText input_email, input_password;
    Button btn_login, btn_new_acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_new_acc = findViewById(R.id.btn_new_acc);
        btn_new_acc.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_new_acc:
                startActivity(new Intent(LoginActivity.this, AuthActivity.class));
                break;
            case R.id.btn_login:
                loginUser();
                break;
        }
    }

    private void loginUser() {
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();

        // Checks if there is no input
        if(email.isEmpty()){
            input_email.setError("Email is required!");
            input_email.requestFocus();
            return;
        } else if(password.isEmpty()){
            input_password.setError("Password is required!");
            input_password.requestFocus();
            return;
        }

        // Checks if email is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            input_email.setError("Provide a valid email!");
            input_email.requestFocus();
            return;
        }

        // Password must have more than 6 characters
        if(password.length() < 6){
            input_password.setError("Minimum length should be 6 characters!");
            input_password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Go to homepage
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        } else {
                            // Display text
                            Toast.makeText(LoginActivity.this, "Failed to login!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
