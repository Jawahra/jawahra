package com.example.jawahra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnNewAcc;

    private CallbackManager callbackManager;
    private LoginButton btnFbLogin;
    private static final String TAG = "FacebookAuthentication";
    private static final String EMAIL = "email";
    private AccessTokenTracker accessTokenTracker;

    private SignInButtonImpl btnGoogleLogin;
    private GoogleSignInClient gsi;
    private int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // User input
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);

        // Login Button
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        // Sign Up Button
        btnNewAcc = findViewById(R.id.btn_new_acc);
        btnNewAcc.setOnClickListener(this);

        // Sign in with Google
        btnGoogleLogin = findViewById(R.id.btn_google_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize sign in client
        gsi = GoogleSignIn.getClient(LoginActivity.this, gso);

        btnGoogleLogin.setOnClickListener(this);

        // Sign in with Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        btnFbLogin = findViewById(R.id.btn_fb_login);
        btnFbLogin.setReadPermissions(Arrays.asList(EMAIL));

        callbackManager = CallbackManager.Factory.create();

        btnFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError");
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    mAuth.signOut();
                }
            }
        };
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
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Checks if there is no input
        if(email.isEmpty()){
            inputEmail.setError("Email is required!");
            inputEmail.requestFocus();
            return;
        } else if(password.isEmpty()){
            inputPassword.setError("Password is required!");
            inputPassword.requestFocus();
            return;
        }

        // Checks if email is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Provide a valid email!");
            inputEmail.requestFocus();
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);

                } else {
                    Toast.makeText(LoginActivity.this, "Google login failed", Toast.LENGTH_LONG).show();
                    updateUI(null);
                }
            }
        });
    }

    private void handleFacebookToken(AccessToken token) {
        Log.d(TAG, "handleFacebookToken" + token);

        AuthCredential authCredential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(LoginActivity.this, "FB login successful", Toast.LENGTH_LONG).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            Toast.makeText(LoginActivity.this, "FB login failed", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null){

            // Get display name and email of the account
            String username = user.getDisplayName();
            String email = user.getEmail();

            // Add to User object
            User userInfo = new User(username, email);
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(userInfo);

            // Redirect to homepage
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

}
