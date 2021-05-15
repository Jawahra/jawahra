package com.example.jawahra;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DocumentReference userDocRef;
    private String userID;
    private EditText inputUsername, inputEmail, inputPassword, inputConfirmPassword;
    private AppCompatButton btnSignUp, btnExistingUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

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
                        if (task.isSuccessful()) {

                            userID = mAuth.getCurrentUser().getUid();
                            userDocRef = fStore.collection("users").document(userID);

                            Map<String, Object> userProfile = new HashMap<>();
                            userProfile.put("username", username);
                            userProfile.put("email", email);
                            userProfile.put("imageUrl", null);

                            userDocRef.set(userProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("CHECK_USER", "onSuccess: user profile is created for "+ userID);
                                }
                            });

                            // Redirect to homepage
                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("CHECK_USER", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

}