package com.example.jawahra.ui.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.FavoriteAdapter;
import com.example.jawahra.adapters.FavoriteViewModel;
import com.google.android.material.snackbar.Snackbar;

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
        toolbar.setNavigationOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        RecyclerView recyclerView = root.findViewById(R.id.list_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        FavoriteAdapter adapter = new FavoriteAdapter(this);
        recyclerView.setAdapter(adapter);

//        ViewModelProvider.AndroidViewModelFactory factory = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication());
//        favoriteViewModel = new ViewModelProvider(this,factory).get(FavoriteViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

//        favoriteViewModel = new ViewModelProvider(this, new FavoriteViewModelFactory(requireActivity().getApplication(),0)).get(FavoriteViewModel.class);
        favoriteViewModel.getFavoritesCardData().observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            adapter.setFavorites(favorites);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                favoriteViewModel.delete(adapter.getFavoriteAt(viewHolder.getAdapterPosition()));
                Snackbar.make(root,"Deleted from Favorites", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        return root;
    }


    @Override
    public void OnItemClick(int position, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("title",title);
        Log.d("CHECK_POSITION", "OnItemClick: CHECK POSITION favfrag" + position);
        NavHostFragment.findNavController(this).navigate(R.id.action_favoritesFragment_to_favoriteDetailsFragment,bundle);
    }
}