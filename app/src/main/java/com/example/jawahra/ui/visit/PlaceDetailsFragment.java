package com.example.jawahra.ui.visit;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PlaceDetailsFragment extends Fragment {

    //database
    private FirebaseFirestore firebaseFirestore;
    public static DocumentReference placeRef;
    private CollectionReference detailsRef, imagesRef, faqsRef;

    private View root;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SectionPagerAdapter sectionPagerAdapter;
    private FragmentManager fragmentManager;
    private FloatingActionButton locationButton;

    private TextView placeName, placeLocation;
    private ImageView imageView;
    public String emirateId, placeId, placeTitle, placeImg, detailsId, imagesId;

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

        detailsRef = placeRef.collection("details");
        imagesRef = placeRef.collection("images");
        faqsRef = placeRef.collection("faqs");
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

        //replace imageview with conresponding image
        imageView = root.findViewById(R.id.place_details_img);
        Glide.with(requireActivity())
                .load(placeImg)
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

    /*public void GetValues(){
        detailsRef.get()
            .addOnSuccessListener(snapshot -> {

                for (QueryDocumentSnapshot snapshots : snapshot){
                    PlaceDetailsModel placeDetailsModel = snapshots.toObject(PlaceDetailsModel.class);
                    String desc = placeDetailsModel.getDesc();
                    String history = placeDetailsModel.getHistory();
                    String map = placeDetailsModel.getMap();

//                    placeDesc.setText(desc);
//                    placeHistory.setText(history);
//                    placeLocation.setText(map);
                }
            })
            .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist" ));
    }*/

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}


    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager(),2);

        adapter.addFragment(new ImagesChildFragment(), "Images");
        adapter.addFragment(new FaqsChildFragment(), "FAQS");

        viewPager.setAdapter(adapter);
    }*/


        /*locationButton = root.findViewById(R.id.location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

//set views
//        placeName = root.findViewById(R.id.place_name);
//        placeDesc = root.findViewById(R.id.place_desc);
//        placeHistory = root.findViewById(R.id.place_history);
//        placeLocation = root.findViewById(R.id.place_location);

        /*collapsingToolbar = root.findViewById(R.id.place_details_collapsing_toolbar);
//       Bring to previous fragment when back button is pressed
        Toolbar toolbar = root.findViewById(R.id.place_details_toolbar);
        toolbar.setNavigationOnClickListener(view1 -> {
            PlacesFragment placesFragment = new PlacesFragment();
            fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place_details, placesFragment);
            fragmentTransaction.show(placesFragment);
            fragmentTransaction.commit();

//            Make Appbar disappear when going to previous fragment
            AppBarLayout appBarLayout = requireView().findViewById(R.id.place_details_app_bar);
            appBarLayout.setVisibility(View.GONE);
        });*/