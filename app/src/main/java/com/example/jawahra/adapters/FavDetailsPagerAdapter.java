package com.example.jawahra.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.jawahra.ui.favorites.childfragments.FavAboutChildFragment;
import com.example.jawahra.ui.favorites.childfragments.FavFaqsChildFragment;
import com.example.jawahra.ui.favorites.childfragments.FavGalleryChildFragment;

import java.util.ArrayList;
import java.util.List;

public class FavDetailsPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();

    public FavDetailsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FavAboutChildFragment();
//            case 1:
//                return new FavFaqsChildFragment();
            case 1:
                return new FavGalleryChildFragment();
            case 2:
                return new FavFaqsChildFragment();
            default:
                return new FavAboutChildFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "About";
                break;
//            case 1:
//                title = "FAQs";
//                break;
            case 1:
                title = "Images";
                break;
            case 2:
                title = "FAQs";
                break;
        }
        return title;
    }
}