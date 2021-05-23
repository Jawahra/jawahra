package com.example.jawahra.ui.visit;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.R;
import com.example.jawahra.adapters.SectionPagerAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PlaceDetailsFragment extends Fragment {

    //database
    private FirebaseFirestore firebaseFirestore;
    public static DocumentReference placeRef;

    private View root;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SectionPagerAdapter sectionPagerAdapter;
    private FragmentManager fragmentManager;
    private FloatingActionButton locationButton;

    private ImageView imageView;
    public String emirateId, placeId, placeTitle, placeImg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get bundle values
        Bundle bundle = getArguments();
        if(bundle != null){
            emirateId = bundle.getString("emirateId");
            placeId = bundle.getString("placeId");
            placeTitle = bundle.getString("placeName");
            placeImg = bundle.getString("placeImg");
        }

        Log.d("ABTCHILD", "PlaceDetails onCreate: emiratesId" + emirateId);
        Log.d("ABTCHILD", "PlaceDetails onCreate: placeId" + placeId);
        //QUERY
        firebaseFirestore = FirebaseFirestore.getInstance();
        placeRef = firebaseFirestore.collection("emirates")
                .document(emirateId)
                .collection("places")
                .document(placeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_place_details, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        //set up tablayout
        initToolBar();
        SetTabLayoutAnim();
        viewPager = root.findViewById(R.id.view_pager);
        tabLayout = root.findViewById(R.id.tab_layout);
        sectionPagerAdapter = new SectionPagerAdapter(((AppCompatActivity) requireContext()).getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //replace imageview with corresponding image
        imageView = root.findViewById(R.id.place_details_img);
        Glide.with(requireActivity())
                .load(placeImg)
                .thumbnail(0.25f)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

//        GetValues();
        return root;
    }

    private void initToolBar(){
        Toolbar toolbar = root.findViewById(R.id.place_details_toolbar);
        ((AppCompatActivity) requireContext()).setSupportActionBar(toolbar);

        if(((AppCompatActivity) requireContext()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) requireContext()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }

    private void SetTabLayoutAnim(){
        final CollapsingToolbarLayout collapsingToolbar = root.findViewById(R.id.place_details_collapsing_toolbar);
        //Set title font
        final Typeface fontPlayFairBold = ResourcesCompat.getFont(requireContext(), R.font.playfair_display_bold);
        collapsingToolbar.setCollapsedTitleTypeface(fontPlayFairBold);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setTitle(placeTitle);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}