package com.example.jawahra.ui.visit;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.R;
import com.example.jawahra.adapters.Favorite;
import com.example.jawahra.adapters.FavoriteDatabase;
import com.example.jawahra.adapters.SectionPagerAdapter;
import com.example.jawahra.models.FaqsModel;
import com.example.jawahra.models.PlaceDetailsModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Objects;

public class PlaceDetailsFragment extends Fragment {

    public static DocumentReference placeRef;

    private View root;
    private FloatingActionButton locationButton;

    private ImageView imageView;
    private String emirateId, placeId, placeTitle, placeImg, emirateName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get bundle values
        Bundle bundle = getArguments();
        if(bundle != null){
            emirateId = bundle.getString("emirateId");
            emirateName = bundle.getString("emirateName");
            placeId = bundle.getString("placeId");
            placeTitle = bundle.getString("placeName");
            placeImg = bundle.getString("placeImg");
        }
        Log.d("ABTCHILD", "PlaceDetails onCreate: emiratesId, " + emirateId);
        Log.d("ABTCHILD", "PlaceDetails onCreate: placeId, " + placeId);
        //QUERY
        //database
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
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

        //replace imageview with corresponding image
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

        setHasOptionsMenu(true);
        //set up tablayout
        Toolbar toolbar = root.findViewById(R.id.place_details_toolbar);
        initToolBar(toolbar);
        SetTabLayoutAnim();
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(((AppCompatActivity) requireContext()).getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        toolbar.setNavigationOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
//        GetValues();

        return root;
    }


    private void initToolBar(Toolbar toolbar){
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.place_details_toolbar_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private CollectionReference detailsRef, faqsRef;
    public String desc, hist, string_website, string_attire;;
    private String string_prices, string_activities, string_availability;
    public List<String> array_activities, array_prices, array_availability;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            FavoriteGetData();
            new Handler().postDelayed(this::SaveFavorite, 5000);
            Snackbar.make(root,"Added to Favorites", Snackbar.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SaveFavorite(){
        Log.d("SAVE_FAV", "SaveFavorite: CALLED");
        AsyncTask.execute(() -> {
            Log.d("SAVE_FAV", "SaveFavorite: RUNNING, DESC: " + desc);
            Log.d("SAVE_FAV", "SaveFavorite: RUNNING, HIST" + hist );
            Log.d("SAVE_FAV", "SaveFavorite: RUNNING, ACTIVITIES: " + string_activities);
            Log.d("SAVE_FAV", "SaveFavorite: RUNNING, AVAILABILITY: " + string_availability);
            Log.d("SAVE_FAV", "SaveFavorite: RUNNING, PRICES: " + string_prices);

            Favorite favorite = new Favorite(placeTitle,emirateName,desc,hist,string_website,string_attire,string_availability,string_prices,string_activities);
            Log.d("SAVE_FAV", "SaveFavorite: " + favorite.toString());
            Log.d("SAVE_FAV", "SaveFavorite: " + favorite.getId() + favorite.getTitle() + favorite.getEmirate() + favorite.getDescription() + favorite.getHistory() + favorite.getActivities() + favorite.getAttire() + favorite.getWebsite() + favorite.getAvailability() + favorite.getPrices());
            FavoriteDatabase.getInstance(getContext()).favoriteDao().insert(favorite);
            Log.d("SAVE_FAV", "SaveFavorite: RUNNING");
        });
        Log.d("SAVE_FAV", "SaveFavorite: END");
    }

    public void FavoriteGetData(){
        Log.d("SAVE_FAV", "FavoriteGetData: CALLED");
        detailsRef = placeRef.collection("details");
        faqsRef = placeRef.collection("faqs");

        detailsRef.get()
                .addOnSuccessListener(snapshot -> {
                    Log.d("SAVE_FAV", "FavoriteGetData: detailsRef CALLED");
                    for (QueryDocumentSnapshot snapshots : snapshot){
                        PlaceDetailsModel placeDetailsModel = snapshots.toObject(PlaceDetailsModel.class);
                        desc = placeDetailsModel.getDesc();
                        hist = placeDetailsModel.getHistory();

                        Log.d("SAVE_FAV", "FavoriteGetData: detailsRef RUNNING");
                        Log.d("SAVE_FAV", "FavoriteGetData: detailsRef RUNNING, DESC: " + desc);
                        Log.d("SAVE_FAV", "FavoriteGetData: detailsRef RUNNING, HIST" + hist );
                    }
                    Log.d("SAVE_FAV", "FavoriteGetData: detailsRef DONE");

                })
                .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist" ));

        faqsRef.get()
                .addOnSuccessListener(snapshot -> {
                    Log.d("SAVE_FAV", "FavoriteGetData: faqsRef CALLED");
                    for (QueryDocumentSnapshot snapshots : snapshot) {
                        Log.d("SAVE_FAV", "FavoriteGetData: faqsRef RUNNING");
                        FaqsModel faqsModel = snapshots.toObject(FaqsModel.class);
                        string_attire = faqsModel.getAttire();
                        string_website = faqsModel.getWebsite();
                        array_activities = faqsModel.getActivities();
                        array_availability = faqsModel.getAvailability();
                        array_prices = faqsModel.getPrices();

                        Log.d("faqs", "ATTIRE: " + string_attire);
                        Log.d("faqs", "WEBSITE: " + string_website);
                        Log.d("faqs", "ARRAY_PRICES" + array_prices);
                        Log.d("faqs", "ARRAY SIZE: " + array_prices.size());

                        if(array_prices != null){
                            for (int i = 0; i < array_prices.size(); i++) {
                                if (i == 0){
                                    string_prices = array_prices.get(i);
                                }else {
                                    string_prices += array_prices.get(i);
                                }
                                string_prices += "\n";
                            }
                        }

                        if (array_availability != null){
                            for (int i = 0; i < array_availability.size(); i++){
                                if (i == 0){
                                    string_availability = array_availability.get(i);
                                }else{
                                    string_availability += array_availability.get(i);
                                }
                                string_availability += "\n";
                            }
                        }

                        if (array_activities != null) {
                            for (int i = 0; i < array_activities.size(); i++) {
                                if (i == 0) {
                                    string_activities = "\u2022 " + array_activities.get(i);
                                }else{
                                    string_activities += "\u2022 " + array_activities.get(i);
                                }
                                string_activities += "\n";
                            }
                        }
                        Log.d("SAVE_FAV", "FavoriteGetData: faqsRef RUNNING, ACTIVITIES: " + string_activities);
                        Log.d("SAVE_FAV", "FavoriteGetData: faqsRef RUNNING, AVAILABILITY: " + string_availability);
                        Log.d("SAVE_FAV", "FavoriteGetData: faqsRef RUNNING, PRICES: " + string_prices);
                    }
                    Log.d("SAVE_FAV", "FavoriteGetData: faqsRef DONE");
                })
                .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist"));

//            AsyncTask.execute(() -> {
//                Favorite favorite = new Favorite(placeTitle,emirateName,desc,hist,string_website,string_attire,string_availability,string_prices,string_activities);
//                Log.d("SAVE_FAV", "SaveFavorite: " + favorite.toString());
//                Log.d("SAVE_FAV", "SaveFavorite: " + favorite.getId() + favorite.getTitle() + favorite.getEmirate() + favorite.getDescription() + favorite.getHistory() + favorite.getActivities() + favorite.getAttire() + favorite.getWebsite() + favorite.getAvailability() + favorite.getPrices());
//                FavoriteDatabase.getInstance(getContext()).favoriteDao().insert(favorite);
//                Log.d("SAVE_FAV", "SaveFavorite: RUNNING");
//            });

        Log.d("SAVE_FAV", "FavoriteGetData: DONE");
    }
}