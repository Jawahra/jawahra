package com.example.jawahra.adapters;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Favorite.class, version = 2)
public abstract class FavoriteDatabase extends RoomDatabase {

    //is singleton (cannot be made into multiple instance, but used the same instance everytime)
    private static FavoriteDatabase instance;

    public abstract FavoriteDao favoriteDao();

    //ensures that there is only one instance of the database
    public static synchronized FavoriteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FavoriteDatabase.class, "favorite_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
