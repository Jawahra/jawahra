package com.example.jawahra.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.jawahra.ui.visit.childfragments.AboutChildFragment;
import com.example.jawahra.ui.visit.childfragments.FaqsChildFragment;
import com.example.jawahra.ui.visit.childfragments.ImagesChildFragment;

import java.util.ArrayList;
import java.util.List;

public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public SectionPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AboutChildFragment();
            case 1:
                return new FaqsChildFragment();
            case 2:
                return new ImagesChildFragment();
            default:
                return new AboutChildFragment();
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
