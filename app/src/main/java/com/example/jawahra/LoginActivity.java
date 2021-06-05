package com.example.jawahra;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.jawahra.models.UserModel;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private String userID;
    private DocumentReference userDocRef;

    private EditText inputEmail, inputPassword;
    private AppCompatButton btnLogin, btnNewAcc;
    private boolean isOnlineLogin;

    private CallbackManager callbackManager;
    private AppCompatButton btnFbLogin;

    private static final String TAG = "FacebookAuthentication";
    private static final String EMAIL = "email";

    private AppCompatButton btnGoogleLogin;
    private GoogleSignInClient gsi;
    private int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Gradient animation for background
        ConstraintLayout constraintLayout = findViewById(R.id.layout_log_in);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

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
        btnFbLogin.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        if(currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    public void onClick(View v) {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        // If wifi is enabled
        if (wifiManager.isWifiEnabled()) {
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
                case R.id.btn_fb_login:
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(EMAIL));
                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d(TAG, "onSuccess" + loginResult);
                            handleFacebookToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() { Log.d(TAG, "onCancel"); }

                        @Override
                        public void onError(FacebookException error) { Log.d(TAG, "onError"); }
                    });
                    break;
            }
        } else { // If wifi is not enabled
            switch (v.getId()) {
                case R.id.btn_new_acc:
                    Toast.makeText(this, "Turn WIFI on to sign up.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_login:
                case R.id.btn_google_login:
                case R.id.btn_fb_login:{
                    Toast.makeText(this, "Turn WIFI on to login.", Toast.LENGTH_SHORT).show();
                }
            }
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
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        FirebaseUser user = mAuth.getCurrentUser();
                        userID = user.getUid();

                        userDocRef = fStore.collection("users").document(userID);
                        userDocRef.addSnapshotListener((value, error) -> updateUI(user));

                    } else {
                        // Display text
                        Toast.makeText(LoginActivity.this, "Failed to login! Check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            if(resultCode == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    FirebaseGoogleAuth(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    FirebaseGoogleAuth(null);
                }
            }

            // If the user cancels out of the dialog
            if (resultCode == RESULT_CANCELED) {
                setResult(RESULT_CANCELED, data);
                finish();
            }

        } else { //If not request code is RC_SIGN_IN it must be facebook

            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);

        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    isOnlineLogin = true;
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Failed to login!", Toast.LENGTH_LONG).show();
                    updateUI(null);
                }
            }
        });
    }

    private void handleFacebookToken(AccessToken token) {
        Log.d(TAG, "handleFacebookToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            isOnlineLogin = true;
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {

            userID = mAuth.getCurrentUser().getUid();
            userDocRef = fStore.collection("users").document(userID);
            Map<String, Object> userProfile = new HashMap<>();

            if(isOnlineLogin) {

                // Get display name and email of the account
                String username = user.getDisplayName();
                String email = user.getEmail();

                userProfile.put("username", username);
                userProfile.put("email", email);

                userDocRef.addSnapshotListener((value, error) -> {
                    UserModel userModel = value.toObject(UserModel.class);
                    String imageUrl = userModel.imageUrl;

                    if (imageUrl != null) {
                        userProfile.put("imageUrl", imageUrl);
                    }
                });

                userDocRef.set(userProfile, SetOptions.merge());

            } else {

                userDocRef.addSnapshotListener((value, error) -> {
                    UserModel userModel = value.toObject(UserModel.class);
                    String username = userModel.username;
                    String email = userModel.email;
                    String imageUrl = userModel.imageUrl;

                    userProfile.put("username", username);
                    userProfile.put("email", email);
                    userProfile.put("imageUrl", imageUrl);

                    userDocRef.set(userProfile, SetOptions.merge());
                });
            }

            Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();

            // Redirect to covid protocol activity
            startActivity(new Intent(LoginActivity.this, CovidProtocolActivity.class));

        }
    }
}
