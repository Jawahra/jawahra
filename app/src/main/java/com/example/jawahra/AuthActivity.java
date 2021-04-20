package com.example.jawahra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText input_username, input_email, input_password, input_confirm_password;
    Button btn_sign_up, btn_existing_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // User input
        input_username=findViewById(R.id.input_username);
        input_email=findViewById(R.id.input_email);
        input_password=findViewById(R.id.input_password);
        input_confirm_password=findViewById(R.id.input_confirm_password);

        // Buttons
        btn_existing_user=findViewById(R.id.btn_existing_user);
        btn_existing_user.setOnClickListener(this);
        
        btn_sign_up=findViewById(R.id.btn_signup);
        btn_sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_existing_user:
                startActivity(new Intent(AuthActivity.this, LoginActivity.class));
                break;
            case R.id.btn_signup:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String username = input_username.getText().toString().trim();
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();
        String confirmPassword = input_confirm_password.getText().toString().trim();

        // Checks if there is no input
        if(username.isEmpty()){
            input_username.setError("Username is required!");
            input_username.requestFocus();
            return;
        } else if(email.isEmpty()){
            input_email.setError("Email is required!");
            input_email.requestFocus();
            return;
        } else if(password.isEmpty()){
            input_password.setError("Password is required!");
            input_password.requestFocus();
            return;
        } else if(confirmPassword.isEmpty()){
            input_confirm_password.setError("Confirm password!");
            input_confirm_password.requestFocus();
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

        // Verifies if password is written correctly
        if(!confirmPassword.equals(password)){
            input_confirm_password.setError("Password does not match!");
            input_confirm_password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        // Display text
                                        Toast.makeText(AuthActivity.this, "You have registered successfully!", Toast.LENGTH_LONG).show();

                                        // Go to homepage
                                        startActivity(new Intent(AuthActivity.this, MainActivity.class));

                                    } else {
                                        // Display text
                                        Toast.makeText(AuthActivity.this, "Registration unsuccessful.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else{
                            // Display text
                            Toast.makeText(AuthActivity.this, "Registration unsuccessful.", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}