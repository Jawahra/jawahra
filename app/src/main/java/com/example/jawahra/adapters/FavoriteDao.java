package com.example.jawahra.adapters;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void insert(Favorite favorite);

    @Update
    void update(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("SELECT * FROM favorite_table ORDER BY id DESC")
    LiveData<List<Favorite>> getAllFavorites();

    @Query("SELECT * FROM favorite_table WHERE id = :position")
    List<Favorite> getCurrentFavorite(int position);
//    LiveData<List<Favorite>> getCurrentFavorite(int position);

}
