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
    EditText inputUsername, inputEmail, inputPassword, inputConfirmPassword;
    Button btnSignUp, btnExistingUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // User input
        inputUsername=findViewById(R.id.input_username);
        inputEmail=findViewById(R.id.input_email);
        inputPassword=findViewById(R.id.input_password);
        inputConfirmPassword=findViewById(R.id.input_confirm_password);

        // Buttons
        btnExistingUser=findViewById(R.id.btn_existing_user);
        btnExistingUser.setOnClickListener(this);
        
        btnSignUp=findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(this);
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
        String username = inputUsername.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        // Checks if there is no input
        if(username.isEmpty()){
            inputUsername.setError("Username is required!");
            inputUsername.requestFocus();
            return;
        } else if(email.isEmpty()){
            inputEmail.setError("Email is required!");
            inputEmail.requestFocus();
            return;
        } else if(password.isEmpty()){
            inputPassword.setError("Password is required!");
            inputPassword.requestFocus();
            return;
        } else if(confirmPassword.isEmpty()){
            inputConfirmPassword.setError("Confirm password!");
            inputConfirmPassword.requestFocus();
            return;
        }

        // Checks if email is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Provide a valid email!");
            inputEmail.requestFocus();
            return;
        }

        // Password must have more than 6 characters
        if(password.length() < 6){
            inputPassword.setError("Minimum length should be 6 characters!");
            inputPassword.requestFocus();
            return;
        }

        // Verifies if password is written correctly
        if(!confirmPassword.equals(password)){
            inputConfirmPassword.setError("Password does not match!");
            inputConfirmPassword.requestFocus();
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