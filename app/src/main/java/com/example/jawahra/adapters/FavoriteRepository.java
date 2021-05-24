package com.example.jawahra.adapters;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

//this is not part of Android Architecture Component but rather just a BEST PRACTICE WOWOOW
//DAO will interact with this class instead of the directly into the database
public class FavoriteRepository {
    private FavoriteDao favoriteDao;
    private LiveData<List<Favorite>> allFavorites;

    public FavoriteRepository(Application application){
        FavoriteDatabase database = FavoriteDatabase.getInstance(application);
        favoriteDao = database.favoriteDao();
        allFavorites = favoriteDao.getAllFavorites();
    }

    public void insert(Favorite favorite){
        new InsertFavAsyncTask(favoriteDao).execute(favorite);
    }

    public void update(Favorite favorite){
        new UpdateFavAsyncTask(favoriteDao).execute(favorite);
    }

    public void delete(Favorite favorite){
        new DeleteFavAsyncTask(favoriteDao).execute(favorite);
    }

    public LiveData<List<Favorite>> getAllFavorites() {
        return allFavorites;
    }

    private  static class InsertFavAsyncTask extends AsyncTask<Favorite, Void, Void>{

        private FavoriteDao favoriteDao;

        private InsertFavAsyncTask(FavoriteDao favoriteDao){
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.insert(favorites[0]);
            return null;
        }
    }

    private  static class UpdateFavAsyncTask extends AsyncTask<Favorite, Void, Void>{

        private FavoriteDao favoriteDao;

        private UpdateFavAsyncTask(FavoriteDao favoriteDao){
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.update(favorites[0]);
            return null;
        }
    }
    private  static class DeleteFavAsyncTask extends AsyncTask<Favorite, Void, Void>{

        private FavoriteDao favoriteDao;

        private DeleteFavAsyncTask(FavoriteDao favoriteDao){
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.delete(favorites[0]);
            return null;
        }
    }
}
