package com.example.jawahra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText input_email, input_password;
    private Button btn_login, btn_new_acc;
    private SignInButton btn_google_login;
    private GoogleSignInClient gsi;
    private int RC_SIGN_IN = 1;

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

        btn_google_login = findViewById(R.id.btn_google_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize sign in client
        gsi = GoogleSignIn.getClient(LoginActivity.this, gso);

        btn_google_login.setOnClickListener(this);
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
            case R.id.btn_google_login:
                Intent i = gsi.getSignInIntent();
                startActivityForResult(i, RC_SIGN_IN);
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Go to homepage
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "Signed in successfully", Toast.LENGTH_LONG).show();

                        } else {
                            // Display text
                            Toast.makeText(LoginActivity.this, "Failed to login! Check your credentials.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Signed in successfully", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);

        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    // Get display name and email of the account
                    String username = acc.getDisplayName();
                    String email = acc.getEmail();

                    // Add to User object
                    User user = new User(username, email);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user);

                    // Redirect to homepage
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
