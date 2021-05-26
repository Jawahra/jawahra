package com.example.jawahra.ui.favorites.childfragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jawahra.R;
import com.example.jawahra.adapters.Favorite;
import com.example.jawahra.adapters.FavoriteViewModel;
import com.example.jawahra.ui.favorites.FavoriteDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class FavFaqsChildFragment extends Fragment {

    private FavoriteViewModel favoriteViewModel;
    private List<Favorite> currentFavorite = new ArrayList<>();
    private int position;

    private TextView faqsPrices, faqsAttire, faqsAvailability, faqsActivities, faqsWebsite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_faqs_child, container, false);

        position = FavoriteDetailsFragment.position;
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        favoriteViewModel.getCurrentFavorite(position).observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            currentFavorite = favorites;
        });
//
        faqsWebsite = root.findViewById(R.id.faqs_website);
        faqsActivities = root.findViewById(R.id.faqs_activities);
        faqsAttire = root.findViewById(R.id.faqs_attire);
        faqsAvailability = root.findViewById(R.id.faqs_availability);
        faqsPrices = root.findViewById(R.id.faqs_prices);

        if (currentFavorite.isEmpty()){
            Log.d("check_fav", "onViewCreated: currentFavorites is empty");
            new Handler().postDelayed(this::setText,1500);
        }
        else{
            Log.d("check_fav", "onViewCreated: currentFavorites is initiated");
            setText();
        }

        return root;
    }

    public void setText(){
        faqsWebsite.setText(currentFavorite.get(0).getWebsite());
        faqsActivities.setText(currentFavorite.get(0).getActivities());
        faqsAttire.setText(currentFavorite.get(0).getAttire());
        faqsAvailability.setText(currentFavorite.get(0).getAvailability());
        faqsPrices.setText(currentFavorite.get(0).getPrices());
    }
}
