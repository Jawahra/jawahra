package com.example.jawahra.ui.favorites;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.jawahra.R;
import com.example.jawahra.adapters.FavDetailsPagerAdapter;
import com.example.jawahra.adapters.Favorite;
import com.example.jawahra.adapters.FavoriteViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteDetailsFragment extends Fragment{
    private FavoriteViewModel favoriteViewModel;
    private List<Favorite> currentFavorite = new ArrayList<>();
    public static int position;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            position = bundle.getInt("position");
            title = bundle.getString("title");
            Log.d("CHECK_POSITION", "onCreate: favdetailfrag" + position);
        }
    }

    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_favorites_details, container, false);

        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        favoriteViewModel.getCurrentFavorite(position).observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            currentFavorite = favorites;
        });

        Log.d("SAVE_FAV", "onCreateView: IS THIS BITCH WORKING");

        Toolbar toolbar = root.findViewById(R.id.place_details_toolbar);
        initToolBar(toolbar);
        SetTabLayoutAnim();
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        FavDetailsPagerAdapter favDetailsPagerAdapter = new FavDetailsPagerAdapter(((AppCompatActivity) requireContext()).getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(favDetailsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        toolbar.setNavigationOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

//        checkInternet();
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
        collapsingToolbar.setTitle(title);
    }


}