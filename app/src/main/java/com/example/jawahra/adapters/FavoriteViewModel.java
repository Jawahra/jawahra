package com.example.jawahra.adapters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {
    private FavoriteRepository repository;
    private LiveData<List<Favorite>> allFavorites;
//    private LiveData<List<Favorite>> currentFavorite;
//    private List<Favorite> currentFavorite;
//    private int position;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteRepository(application);
//        this.position = position;
        allFavorites = repository.getAllFavorites();
//        currentFavorite = repository.getCurrentFavorite(position);
    }

    public void insert(Favorite favorite){
        repository.insert(favorite);
    }

    public void update(Favorite favorite){
        repository.update(favorite);
    }

    public void delete(Favorite favorite){
        repository.delete(favorite);
    }

    public LiveData<List<Favorite>> getAllFavorites(){
        return allFavorites;
    }

//    public List<Favorite> getCurrentFavorite(int position){ return currentFavorite; }
}
