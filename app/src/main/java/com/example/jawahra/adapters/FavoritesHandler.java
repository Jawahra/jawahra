package com.example.jawahra.adapters;

import android.util.Log;

import com.example.jawahra.ui.visit.PlaceDetailsFragment;

public class FavoritesHandler {


    public boolean checkList(){
        return false;
    }

    public void editFavorite(boolean fav){
        Log.d("FAVORITES_FEATURE", "editFavorite: fav " + fav);
        if (!fav){
            Log.d("FAVORITES_FEATURE", "editFavorite: FAV IS FALSE ===" + fav);

            addFavorite();
        }
        else{
            Log.d("FAVORITES_FEATURE", "editFavorite: FAV IS TRUE === " + fav);

            removeFavorite();
        }
    }

    public void addFavorite(){

        Log.d("FAVORITES_FEATURE", "addFavorite: called ");
        Log.d("FAVORITES_FEATURE", "addFavorite: added to favorites ");
        PlaceDetailsFragment.isFav = true;
    }

    public void removeFavorite(){
        Log.d("FAVORITES_FEATURE", "removeFavorite: called");
        Log.d("FAVORITES_FEATURE", "removeFavorite: removed from favorites");
        PlaceDetailsFragment.isFav = false;
    }
}
