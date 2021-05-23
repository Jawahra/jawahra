package com.example.jawahra.ui.profile.childfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.jawahra.R;
public class FavoritesFragment extends Fragment {

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


        return root;
    }


}