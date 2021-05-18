package com.example.jawahra.ui.visit.childfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.jawahra.R;
import com.example.jawahra.models.PlaceDetailsModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AboutChildFragment extends Fragment {
    private View root;
    private CollectionReference detailsRef;

    private String desc, hist;
    private TextView placeDesc, placeHist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_about_child, container, false);

        placeDesc = root.findViewById(R.id.about_desc);
        placeHist = root.findViewById(R.id.about_history);
        detailsRef = PlaceDetailsFragment.placeRef.collection("details");
        detailsRef.get()
                .addOnSuccessListener(snapshot -> {

                    for (QueryDocumentSnapshot snapshots : snapshot){
                        PlaceDetailsModel placeDetailsModel = snapshots.toObject(PlaceDetailsModel.class);
                        desc = placeDetailsModel.getDesc();
                        hist = placeDetailsModel.getHistory();
                        placeDesc.setText(desc);
                        placeHist.setText(hist);
                        Log.d("ABTCHILD", "onCreate: desc" + desc);
                        Log.d("ABTCHILD", "onCreate: hist" + hist);
                    }
                })
                .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist" ));


        return root;
    }
}