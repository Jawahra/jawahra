package com.example.jawahra.ui.visit;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.R;
import com.example.jawahra.adapters.FavoritesHandler;
import com.example.jawahra.adapters.SectionPagerAdapter;
import com.example.jawahra.ui.visit.childfragments.AboutChildFragment;
import com.example.jawahra.ui.visit.childfragments.FaqsChildFragment;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PlaceDetailsFragment extends Fragment {

    FavoritesHandler favoritesHandler;
    AboutChildFragment aboutChildFragment;
    FaqsChildFragment faqsChildFragment;
    //database
    private FirebaseFirestore firebaseFirestore;
    public static DocumentReference placeRef;
    public static boolean isFav = true;

    private View root;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SectionPagerAdapter sectionPagerAdapter;
    private FragmentManager fragmentManager;
    private FloatingActionButton locationButton;

    private ImageView imageView;
    public static String emirateId, placeId, placeTitle, placeImg;

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

        Log.d("FAVORITES_FEATURE", "onCreate: isfav" + isFav);
        Log.d("FAVORITES_FEATURE", "onCreate: placetitle" + placeTitle);

        favoritesHandler = new FavoritesHandler();
        isFav = favoritesHandler.checkList(placeTitle);

        Log.d("ABTCHILD", "PlaceDetails onCreate: emiratesId, " + emirateId);
        Log.d("ABTCHILD", "PlaceDetails onCreate: placeId, " + placeId);
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
        viewPager = root.findViewById(R.id.view_pager);
        tabLayout = root.findViewById(R.id.tab_layout);
        sectionPagerAdapter = new SectionPagerAdapter(((AppCompatActivity) requireContext()).getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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

    public String loadJSONFromAssets() {
        String json = null;
        Log.d("FAVORITES_FEATURE", "loadJSONFromAssets: test 1");
        try {
            Log.d("FAVORITES_FEATURE", "loadJSONFromAssets: test 2");
            InputStream is = requireActivity().getAssets().open("user_favorites");
            Log.d("FAVORITES_FEATURE", "loadJSONFromAssets: test 3");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void editFavorite(boolean fav){
        Log.d("FAVORITES_FEATURE", "editFavorite: fav " + fav);
        if (!fav){
            Log.d("FAVORITES_FEATURE", "editFavorite: FAV IS FALSE ===" + fav);

            favoritesHandler.addFavorite(aboutChildFragment.desc, aboutChildFragment.hist, faqsChildFragment.string_website, faqsChildFragment.string_attire, faqsChildFragment.array_activities,faqsChildFragment.array_prices,faqsChildFragment.array_availability);
        }
        else{
            Log.d("FAVORITES_FEATURE", "editFavorite: FAV IS TRUE === " + fav);

            favoritesHandler.removeFavorite();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.place_details_toolbar_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            editFavorite(isFav);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}