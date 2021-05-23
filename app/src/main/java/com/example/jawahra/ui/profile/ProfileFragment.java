package com.example.jawahra.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.jawahra.LoginActivity;
import com.example.jawahra.R;
import com.example.jawahra.models.UserModel;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    // Variables for switching layouts
    private View layoutGuest, layoutLoggedIn, layoutBlank;
    private int screen;
    private static final int BLANK = 0;
    private static final int GUEST = 1;
    private static final int LOGGED = 2;

    // Firebase database and storage
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DocumentReference userDocRef;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private GoogleSignInClient gsi;
    
    // User information
    private String userID;
    private TextView usernameText, emailText;
    private ImageView profileImg;
    private Uri imageUri;

    // Cards and buttons
    private CardView cardFavorites;
    private TextView btnGuestLogin;

    // Toolbar
    private Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        View profile = inflater.inflate(R.layout.fragment_profile, container, false);

        layoutGuest = profile.findViewById(R.id.layout_guest);
        layoutLoggedIn = profile.findViewById(R.id.layout_logged_in);
        layoutBlank = profile.findViewById(R.id.layout_blank);

        if (currentUser == null) {
            screen = GUEST;
            guestSignedIn(profile);
        } else {
            screen = LOGGED;
            loggedIn(profile);
        }

        renderScreen();
        setHasOptionsMenu(true);
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
        usernameText = profile.findViewById(R.id.profile_name);
        emailText = profile.findViewById(R.id.profile_email);
        profileImg = profile.findViewById(R.id.profile_img);

        // Toolbar
        toolbar = profile.findViewById(R.id.profile_toolbar);
        toolbar.setOnMenuItemClickListener(this);

        cardFavorites = profile.findViewById(R.id.profile_fav);
        cardFavorites.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_profile_to_favoritesFragment);
            screen = BLANK;
            renderScreen();

        });

        updateUI(usernameText, emailText, profileImg);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_settings:
                return true;

            case R.id.option_edit_image:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 5);
                return true;

            case R.id.option_instructions:
                Toast.makeText(getActivity(), "Instructions", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.option_log_out:
                Toast.makeText(getActivity(), "Logged out.", Toast.LENGTH_SHORT).show();

                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                // Initialize sign in client
                gsi = GoogleSignIn.getClient(getActivity(), gso);

                FirebaseAuth.getInstance().signOut(); // Log out from email
                gsi.signOut(); // Log out from Google
                LoginManager.getInstance().logOut(); // Log out from Facebook

                // Redirect to Login Activity
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return true;
        }
        return false;
    }

    // Uploading new profile picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == Activity.RESULT_OK && data != null&& data.getData() != null){
            imageUri = data.getData();
            profileImg.setImageURI(imageUri);

            uploadPic(imageUri);
        }
    }

    private void uploadPic(Uri uri) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading image...");
        pd.show();

        final StorageReference imageRef = storageRef.child("profile/" + imageUri.getLastPathSegment() + getFileExtension(uri));

        imageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                userID  = currentUser.getUid();
                                userDocRef = fStore.collection("users").document(userID);
                                userDocRef.update("imageUrl", uri.toString());

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

    // Update user information
    private void updateUI(TextView usernameText, TextView emailText, ImageView profileImg) {

        userID  = currentUser.getUid();
        userDocRef = fStore.collection("users").document(userID);
        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(isAdded()){
                    UserModel userModel = value.toObject(UserModel.class);

                    String username = userModel.username;
                    String email = userModel.email;
                    String imageUrl = userModel.imageUrl;

                    usernameText.setText(username);
                    emailText.setText(email);

                    Glide.with(getActivity())
                            .load(imageUrl).circleCrop()
                            .placeholder(R.drawable.placeholder_blank_image)
                            .into(profileImg);
                }
            }
        });
    }

    // Render current screen
    private void renderScreen() {
        layoutGuest.setVisibility(screen == GUEST ? View.VISIBLE : View.GONE);
        layoutLoggedIn.setVisibility(screen == LOGGED ? View.VISIBLE : View.GONE);
        layoutBlank.setVisibility(screen == BLANK ? View.VISIBLE : View.GONE);

    }
}