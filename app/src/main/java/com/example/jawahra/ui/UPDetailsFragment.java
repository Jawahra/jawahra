package com.example.jawahra.ui;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UPDetailsFragment extends Fragment {

    String upcomingPlaceID, placeEmirate, placeName, placeImg;
    TextView tvPlaceEmirate, tvPlaceContent;
    LinearLayout llPlaceImg;

    FragmentManager fragmentManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get bundle values
        Bundle bundle = getArguments();
        if (bundle != null){
            upcomingPlaceID = bundle.getString("upcomingPlaceID", upcomingPlaceID);
            placeEmirate = bundle.getString("placeEmirate", placeEmirate);
            placeName = bundle.getString("placeName", placeName);
            placeImg = bundle.getString("placeImg", placeImg);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollapsingToolbarLayout collapsingToolbar = requireView().findViewById(R.id.up_details_collapsing_toolbar);
        tvPlaceEmirate = requireView().findViewById(R.id.up_details_emirate);
        tvPlaceContent = requireView().findViewById(R.id.up_details_content);
        llPlaceImg = requireView().findViewById(R.id.up_details_img_layout);


//       Bring to previous fragment when back button is pressed
        Toolbar toolbar = requireView().findViewById(R.id.up_details_toolbar);
        toolbar.setNavigationOnClickListener(view1 -> {
            /*NewsFragment newsFragment = new NewsFragment();
            fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_up_details, newsFragment);
            fragmentTransaction.show(newsFragment);
            fragmentTransaction.commit();

//            Make Appbar disappear when going to previous fragment
            AppBarLayout appBarLayout = requireView().findViewById(R.id.up_details_app_bar);
            appBarLayout.setVisibility(View.GONE);*/
            NavHostFragment.findNavController(this).popBackStack();
        });



//        Set title font
        final Typeface fontPlayFairBold = ResourcesCompat.getFont(requireContext(), R.font.playfair_display_bold);
        collapsingToolbar.setCollapsedTitleTypeface(fontPlayFairBold);


//        collapsingToolbar.setExpandedTitleTypeface(fontPlayFairBold);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setTitle(placeName);

        tvPlaceEmirate.setText(placeEmirate);

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


}