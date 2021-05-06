package com.example.jawahra.ui;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UPDetailsFragment extends Fragment {

    String upcomingPlaceID, placeEmirate, placeName, placeImg;
    TextView tvPlaceEmirate, tvPlaceContent;
    ImageView ivPlaceImg;
    LinearLayout llPlaceImg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get bundle values
        Bundle bundle = getArguments();
        if (bundle != null){
            upcomingPlaceID = bundle.getString("upcomingPlaceID");
            placeEmirate = bundle.getString("placeEmirate", placeEmirate);
            placeName = bundle.getString("placeName", placeName);
            placeImg = bundle.getString("placeImg", placeImg);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                Log.d("FRAGMENT_OPENED", "Upcoming Details Fragment has been opened");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_details, container, false);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("upcoming_places").document(upcomingPlaceID);
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    String content = doc.getString("placeContent");
                    tvPlaceContent.setText(content);
                } else{
                    Log.d("Document", "No data");
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollapsingToolbarLayout collapsingToolbar = requireView().findViewById(R.id.up_details_collapsing_toolbar);
        tvPlaceEmirate = requireView().findViewById(R.id.up_details_emirate);
        tvPlaceContent = requireView().findViewById(R.id.up_details_content);
        llPlaceImg = requireView().findViewById(R.id.up_details_img_layout);

//        Set title font
        final Typeface fontPlayFairBold = ResourcesCompat.getFont(requireContext(), R.font.playfair_display_bold);
        collapsingToolbar.setCollapsedTitleTypeface(fontPlayFairBold);


//        collapsingToolbar.setExpandedTitleTypeface(fontPlayFairBold);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        tvPlaceEmirate.setText(placeEmirate);
        collapsingToolbar.setTitle(placeName);


        //                Get url string of image from document in Firestore and set ImageView to that image
        Glide.with(requireActivity())
                .load(placeImg)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        llPlaceImg.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }
}