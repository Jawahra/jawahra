package com.example.jawahra.ui.visit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jawahra.R;

public class VisitFragment extends Fragment {

    private VisitViewModel visitViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        visitViewModel =
                new ViewModelProvider(this).get(VisitViewModel.class);
        View root = inflater.inflate(R.layout.fragment_visit, container, false);
        final TextView textView = root.findViewById(R.id.text_visit);
        visitViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}