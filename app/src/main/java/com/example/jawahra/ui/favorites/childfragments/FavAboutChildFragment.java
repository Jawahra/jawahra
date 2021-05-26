package com.example.jawahra.ui.favorites.childfragments;

import android.os.Bundle;
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

public class FavAboutChildFragment extends Fragment {

    private FavoriteViewModel favoriteViewModel;
    private List<Favorite> currentFavorite = new ArrayList<>();
    private int position;

    private TextView placeDesc, placeHist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_faqs_child, container, false);

        position = FavoriteDetailsFragment.position;
        Log.d("CHECK_POSITION", "onCreateView: position" + position);
        Log.d("CHECK_POSITION", "onCreateView: raaawwwwwwwwwwwww" + FavoriteDetailsFragment.position);
//        Log.d("CHECK_POSITION", "onCreateView: getPOsitiiosng" + favoriteDetailsFragment.getPosition());

        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
//        favoriteViewModel.getCurrentFavorite(position).observe(getViewLifecycleOwner(), favorites -> {
//            currentFavorite = favorites;
//        });
        favoriteViewModel.getAllFavorites().observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            currentFavorite = favorites;
        });

        placeDesc = root.findViewById(R.id.about_desc);
        placeHist = root.findViewById(R.id.about_history);

        placeDesc.setText(currentFavorite.get(position).getDescription());
        placeHist.setText(currentFavorite.get(position).getHistory());

        return root;
    }
}
