//package com.example.jawahra.adapters;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//public class FavoriteViewModelFactory implements ViewModelProvider.Factory {
//
//    private Application mApplication;
//    private int mParam;
//
//    public FavoriteViewModelFactory(Application application, int param){
//        mApplication = application;
//        mParam = param;
//    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return (T) new FavoriteViewModel(mApplication, mParam);
//    }
//}
