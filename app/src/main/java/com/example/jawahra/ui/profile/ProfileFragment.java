package com.example.jawahra.ui.profile;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jawahra.AuthActivity;
import com.example.jawahra.CovidProtocolActivity;
import com.example.jawahra.LoginActivity;
import com.example.jawahra.MainActivity;
import com.example.jawahra.R;
import com.example.jawahra.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ProfileFragment extends Fragment {

    // Variables for switching layouts
    private View layoutGuest, layoutLoggedIn, layoutSettings, layoutFavorites;
    private int screen;
    private static final int GUEST = 0;
    private static final int LOGGED = 1;
    private static final int SETTINGS = 2;
    private static final int FAVORITES = 3;

    // Firebase database and storage
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private String userID;
    private GoogleSignInClient gsi;
    
    // User information
    private TextView usernameText, emailText;
    private ShapeableImageView profileImg;
    private Uri imageUri;

    // Cards and buttons
    private CardView cardProtocol, cardFavorites;
    private TextView btnLogout, btnGuestLogin, btnSettings;
    private TextView btnChangePFP;

    // Toolbar
    private Toolbar toolbar;
    private TextView pageTitle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View profile = inflater.inflate(R.layout.fragment_profile, container, false);

        layoutGuest = profile.findViewById(R.id.layout_guest);
        layoutLoggedIn = profile.findViewById(R.id.layout_logged_in);
        layoutSettings = profile.findViewById(R.id.layout_user_settings);
        layoutFavorites = profile.findViewById(R.id.layout_favorites);

        if (currentUser == null) {
            screen = GUEST;
            guestSignedIn(profile);
        } else {
            screen = LOGGED;
            loggedIn(profile);
        }

        renderScreen();

        return profile;
    }

    // Guest signed in layout
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

    // User logged in layout
    private void loggedIn(View profile) {
        final TextView usernameText = profile.findViewById(R.id.profile_name);
        final TextView emailText = profile.findViewById(R.id.profile_email);
        final ShapeableImageView profileImg = profile.findViewById(R.id.profile_img);

        // Toolbar
        toolbar = profile.findViewById(R.id.toolbar);
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

        cardProtocol = profile.findViewById(R.id.protocols_covid);
        cardProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Redirect to Activity
                startActivity(new Intent(getActivity(), CovidProtocolActivity.class));
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

        updateUI(usernameText, emailText, profileImg);
    }

    // User settings layout
    private void userSettings(View profile) {
        usernameText = profile.findViewById(R.id.profile_name2);
        emailText = profile.findViewById(R.id.profile_email2);
        profileImg = profile.findViewById(R.id.profile_img2);

        updateUI(usernameText, emailText, profileImg);

        // Toolbar
        toolbar = profile.findViewById(R.id.toolbar1);
        toolbar.setNavigationIcon(null);

        // Select Image
        btnChangePFP = profile.findViewById(R.id.btn_change_pfp);
        btnChangePFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                getActivity().startActivityForResult(intent, 1);
            }
        });

    }

    // Uploading new profile picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
         if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null&& data.getData() != null){
             imageUri = data.getData();
             profileImg.setImageURI(imageUri);

             uploadPic(imageUri);
         }
    }

    private void uploadPic(Uri uri) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading image...");
        pd.show();

        final StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment() + getFileExtension(uri));

        imageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                userID  = currentUser.getUid();
                                reference.child(userID).child("imageUrl").setValue(uri.toString());

                                // Toast message
                                Toast.makeText(getActivity(), "Image Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Dismiss dialog
                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Dismiss dialog
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Failed Uploading.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Uploaded " + (int) progressPercent + "%");
                    }
                });
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }


    // User Favorites Layout
    private void userFavorites(View profile) {

        // Toolbar
        toolbar = profile.findViewById(R.id.toolbar_fav);
        pageTitle = toolbar.findViewById(R.id.toolbar_title);
        pageTitle.setText("FAVORITES");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen = LOGGED;
                renderScreen();
            }
        });
    }


    // Update user information
    private void updateUI(TextView usernameText, TextView emailText, ShapeableImageView profileImg) {

        userID  = currentUser.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String username = userProfile.username;
                    String email = userProfile.email;
                    String imageUrl = userProfile.imageUrl;

                    usernameText.setText(username);
                    emailText.setText(email);
                    Picasso.get().load(imageUrl).fit()
                            .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .into(profileImg);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "There has been an error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Render current screen
    private void renderScreen() {
        layoutGuest.setVisibility(screen == GUEST ? View.VISIBLE : View.GONE);
        layoutLoggedIn.setVisibility(screen == LOGGED ? View.VISIBLE : View.GONE);
        layoutSettings.setVisibility(screen == SETTINGS ? View.VISIBLE : View.GONE);
        layoutFavorites.setVisibility(screen == FAVORITES ? View.VISIBLE : View.GONE);
    }
}