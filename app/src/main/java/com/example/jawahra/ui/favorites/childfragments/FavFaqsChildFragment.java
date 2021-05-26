package com.example.jawahra.ui.favorites.childfragments;

import android.os.Bundle;
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
//        favoriteViewModel.getCurrentFavorite(position).observe(getViewLifecycleOwner(), favorites -> {
//            currentFavorite = favorites;
//        });
        favoriteViewModel.getAllFavorites().observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            currentFavorite = favorites;
        });

        faqsWebsite = root.findViewById(R.id.faqs_website);
        faqsActivities = root.findViewById(R.id.faqs_activities);
        faqsAttire = root.findViewById(R.id.faqs_attire);
        faqsAvailability = root.findViewById(R.id.faqs_availability);
        faqsPrices = root.findViewById(R.id.faqs_prices);

        faqsWebsite.setText(currentFavorite.get(position).getWebsite());
        faqsActivities.setText(currentFavorite.get(position).getActivities());
        faqsAttire.setText(currentFavorite.get(position).getAttire());
        faqsAvailability.setText(currentFavorite.get(position).getAvailability());
        faqsPrices.setText(currentFavorite.get(position).getPrices());

        return root;
    }
}
