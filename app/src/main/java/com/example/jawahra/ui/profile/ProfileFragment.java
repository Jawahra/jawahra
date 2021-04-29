package com.example.jawahra.ui.profile;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jawahra.AuthActivity;
import com.example.jawahra.LoginActivity;
import com.example.jawahra.MainActivity;
import com.example.jawahra.R;
import com.example.jawahra.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private View layoutGuest, layoutLoggedIn, layoutSettings, layoutFavorites;
    private int screen;
    private static final int GUEST = 0;
    private static final int LOGGED = 1;
    private static final int SETTINGS = 2;
    private static final int FAVORITES = 3;

    private FirebaseUser user;

    private DatabaseReference reference;
    private String userID;
    private GoogleSignInClient gsi;

    private CardView cardFavorites;
    private TextView btnLogout, btnGuestLogin, btnSettings;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View profile = inflater.inflate(R.layout.fragment_profile, container, false);

        layoutGuest = profile.findViewById(R.id.layout_guest);
        layoutLoggedIn = profile.findViewById(R.id.layout_logged_in);
        layoutSettings = profile.findViewById(R.id.layout_user_settings);
        layoutFavorites = profile.findViewById(R.id.layout_favorites);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            screen = LOGGED;
            loggedIn(profile);
        } else {
            screen = GUEST;
            guestSignedIn(profile);
        }

        renderScreen();

        return profile;
    }

    private void guestSignedIn(View profile) {

        btnGuestLogin=profile.findViewById(R.id.btn_guest_login);
        btnGuestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Redirect to Login Activity
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

    private void loggedIn(View profile) {
        final TextView usernameText = profile.findViewById(R.id.profile_name);
        final TextView emailText = profile.findViewById(R.id.profile_email);

        // Toolbar
        Toolbar toolbar = profile.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize sign in client
        gsi = GoogleSignIn.getClient(getActivity(), gso);

        btnLogout = profile.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut(); // Log out from email
                gsi.signOut(); // Log out from Google
                LoginManager.getInstance().logOut(); // Log out from Facebook

                // Redirect to Login Activity
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        btnSettings = profile.findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen = SETTINGS;
                userSettings(profile);
                renderScreen();
            }
        });

        cardFavorites = profile.findViewById(R.id.profile_fav);
        cardFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen = FAVORITES;
                userFavorites(profile);
                renderScreen();
            }
        });


        updateUI(usernameText, emailText);
    }

    private void userSettings(View profile) {
        final TextView usernameText = profile.findViewById(R.id.profile_name2);
        final TextView emailText = profile.findViewById(R.id.profile_email2);
        updateUI(usernameText, emailText);

        // Toolbar
        Toolbar toolbar = profile.findViewById(R.id.toolbar1);
        toolbar.setNavigationIcon(null);
    }

    private void userFavorites(View profile) {

        // Toolbar
        Toolbar toolbar = profile.findViewById(R.id.toolbar_fav);
        toolbar.setTitle("FAVORITES");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen = LOGGED;
                renderScreen();
            }
        });
    }

    private void updateUI(TextView usernameText, TextView emailText) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String username = userProfile.username;
                    String email = userProfile.email;

                    usernameText.setText(username);
                    emailText.setText(email);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "There has been an error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void renderScreen() {
        layoutGuest.setVisibility(screen == GUEST ? View.VISIBLE : View.GONE);
        layoutLoggedIn.setVisibility(screen == LOGGED ? View.VISIBLE : View.GONE);
        layoutSettings.setVisibility(screen == SETTINGS ? View.VISIBLE : View.GONE);
        layoutFavorites.setVisibility(screen == FAVORITES ? View.VISIBLE : View.GONE);
    }
}