package com.example.jawahra.ui.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.FavoriteAdapter;
import com.example.jawahra.adapters.FavoriteViewModel;

public class FavoritesFragment extends Fragment implements FavoriteAdapter.OnListItemClick {

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

        FavoriteAdapter adapter = new FavoriteAdapter(this);
        recyclerView.setAdapter(adapter);

        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        favoriteViewModel.getAllFavorites().observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            adapter.setFavorites(favorites);
        });

        return root;
    }


    @Override
    public void OnItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        Log.d("CHECK_POSITION", "OnItemClick: CHECK POSITION favfrag" + position);
        NavHostFragment.findNavController(this).navigate(R.id.action_favoritesFragment_to_favoriteDetailsFragment,bundle);
    }
}