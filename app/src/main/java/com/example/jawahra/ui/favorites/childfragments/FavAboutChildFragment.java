package com.example.jawahra.ui.favorites.childfragments;

import android.os.Bundle;
import android.os.Handler;
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
    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_about_child, container, false);
        position = FavoriteDetailsFragment.position;
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        favoriteViewModel.getCurrentFavorite(position).observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            currentFavorite = favorites;
        });

        if (!currentFavorite.isEmpty()){
            new Handler().postDelayed(this::setText,1500);
        }

        return root;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        if (currentFavorite.isEmpty()){
//            Log.d("check_fav", "onViewCreated: currentFavorites is empty");
//            new Handler().postDelayed(this::setText,1500);
//        }
//        else{
//            Log.d("check_fav", "onViewCreated: currentFavorites is initiated");
//            setText();
//        }
//    }

    public void setText(){
        placeDesc = root.findViewById(R.id.about_desc);
        placeHist = root.findViewById(R.id.about_history);
        placeDesc.setText(currentFavorite.get(0).getDescription());
        placeHist.setText(currentFavorite.get(0).getHistory());
    }
}
