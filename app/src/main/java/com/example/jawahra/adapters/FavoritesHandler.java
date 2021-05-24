package com.example.jawahra.adapters;

import android.content.Context;
import android.util.Log;

import com.example.jawahra.ui.visit.PlaceDetailsFragment;

import java.io.File;
import java.util.List;

public class FavoritesHandler {

    //json parsing values
//    JSONObject jsonParentObject, jsonChildObject;
//    JSONArray jsonParentArray;
    File file;
    int listLength;
    int index;

    Context context;

    public FavoritesHandler(Context context){
        this.context = context;
    }

    public void readFavoritesFile(){
        if (!file.exists()) {
            file = new File(context.getFilesDir(), "user_favorites");
        }
    }


   /* public String loadJSONFromAssets() {
        String json = "";
        Log.d("FAVORITES_FEATURE", "loadJSONFromAssets: test 1");
        AssetManager assetManager = context.getResources().getAssets();

        try {
            Log.d("FAVORITES_FEATURE", "loadJSONFromAssets: test 2");
            InputStream is =  assetManager.open("user_favorites.json");
            Log.d("FAVORITES_FEATURE", "loadJSONFromAssets: test 3");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }*/

    boolean check_isFav;
    /*public boolean checkList(String placeTitle){
        Log.d("FAVORITES_FEATURE", "checkList: check_isFav " + check_isFav);
        Log.d("FAVORITES_FEATURE", "checkList: place title " + placeTitle);


        try {
//            jsonParentObject = new JSONObject(loadJSONFromAssets(context));
            jsonParentObject = new JSONObject(new PlaceDetailsFragment().loadJSONFromAssets());
            jsonParentArray = jsonParentObject.getJSONArray("favorites");
            listLength = jsonParentArray.length();

            //check all objects
            //find array with equal title
            //if true, change bool
            if (listLength > 0 ){
                for ( int i = 0; i < listLength; i++){
                    jsonChildObject = jsonParentArray.getJSONObject(i);
                    String favplaceTitle = jsonChildObject.getString("placeTitle");
                    if (favplaceTitle.equals(placeTitle)){
                        index = i;
                        check_isFav = true;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("FAVORITES_FEATURE", "checkList: check_isFav after revalue" + check_isFav);
        return check_isFav;
    }*/

    /*public void editFavorite(boolean fav){
        Log.d("FAVORITES_FEATURE", "editFavorite: fav " + fav);
        if (!fav){
            Log.d("FAVORITES_FEATURE", "editFavorite: FAV IS FALSE ===" + fav);

            addFavorite();
        }
        else{
            Log.d("FAVORITES_FEATURE", "editFavorite: FAV IS TRUE === " + fav);

            removeFavorite();
        }
    }*/

    public void addFavorite(String desc, String hist, String website, String attire, List<String> activities, List<String> prices, List<String> availabilities ){
        Log.d("FAVORITES_FEATURE", "addFavorite: called ");
        Log.d("FAVORITES_FEATURE", "addFavorite: added to favorites ");
        Log.d("FAVORITES_FEATURE", "addFavorite: description " + desc);
        Log.d("FAVORITES_FEATURE", "addFavorite: hist " + hist);
        Log.d("FAVORITES_FEATURE", "addFavorite: website " + website);
        Log.d("FAVORITES_FEATURE", "addFavorite: attire" + attire);
        Log.d("FAVORITES_FEATURE", "addFavorite: activities " + activities);
        Log.d("FAVORITES_FEATURE", "addFavorite: prices " + prices);
        Log.d("FAVORITES_FEATURE", "addFavorite: availabilities " + availabilities);
        PlaceDetailsFragment.isFav = true;
    }

    public void removeFavorite(){
        Log.d("FAVORITES_FEATURE", "removeFavorite: called");
        Log.d("FAVORITES_FEATURE", "removeFavorite: removed from favorites");

        PlaceDetailsFragment.isFav = false;
    }
}
