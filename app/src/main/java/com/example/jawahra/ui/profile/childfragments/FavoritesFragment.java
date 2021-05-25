package com.example.jawahra.ui.profile.childfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.FavoriteAdapter;
import com.example.jawahra.adapters.FavoriteViewModel;

public class FavoritesFragment extends Fragment {
    public static final int SAVE_FAV_REQUEST = 1;

    private FavoriteViewModel favoriteViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        Toolbar toolbar = root.findViewById(R.id.favorites_toolbar);
        ((AppCompatActivity) requireContext()).setSupportActionBar(toolbar);

        RecyclerView recyclerView = root.findViewById(R.id.list_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        FavoriteAdapter adapter = new FavoriteAdapter();
        recyclerView.setAdapter(adapter);

        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        favoriteViewModel.getAllFavorites().observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            adapter.setFavorites(favorites);
        });

        return root;
    }
}