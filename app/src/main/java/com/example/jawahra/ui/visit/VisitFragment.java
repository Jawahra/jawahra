package com.example.jawahra.ui.visit;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.EmiratesAdapter;
import com.example.jawahra.models.EmiratesModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class VisitFragment extends Fragment implements EmiratesAdapter.OnListItemClick {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView listEmirates;
    private EmiratesAdapter adapter;
    View root;
//    private ImageView cardImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_visit, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        listEmirates = root.findViewById(R.id.list_emirates);

        //Query
        Query query = firebaseFirestore.collection("emirates");

        //RecyclerOptions

        FirestoreRecyclerOptions<EmiratesModel> options = new FirestoreRecyclerOptions.Builder<EmiratesModel>()
                .setQuery(query, new SnapshotParser<EmiratesModel>() {
                    @NonNull
                    @Override
                    public EmiratesModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        EmiratesModel emiratesModel = snapshot.toObject(EmiratesModel.class);
                        return emiratesModel;
                    }
                })
                .build();

        adapter = new EmiratesAdapter(options, this, getActivity());

        listEmirates.setHasFixedSize(true);
        listEmirates.setLayoutManager(new LinearLayoutManager(root.getContext()));
        listEmirates.setAdapter(adapter);

        initToolBar();
        setCollapsingToolBar();
        return root;
    }

    private void initToolBar(){
        Toolbar toolbar = root.findViewById(R.id.visit_toolbar);
        ((AppCompatActivity) requireContext()).setSupportActionBar(toolbar);

        if(((AppCompatActivity) requireContext()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) requireContext()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }
    }
    private void setCollapsingToolBar(){
        final CollapsingToolbarLayout collapsingToolbar = root.findViewById(R.id.visit_collapsing_toolbar);
        //Set title font
        final Typeface fontPlayFairBold = ResourcesCompat.getFont(requireContext(), R.font.playfair_display_sc_bold);
        collapsingToolbar.setCollapsedTitleTypeface(fontPlayFairBold);
        collapsingToolbar.setExpandedTitleTypeface(fontPlayFairBold);
//        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void OnItemClick(String myDocumentId, String emirateName) {
        Log.d("CHECK_ID", "visit fragment, id received: " + myDocumentId);

        Bundle bundle = new Bundle();
        bundle.putString("docID",myDocumentId);
        bundle.putString("emirateName", emirateName);

        Log.d("CHECK_ID", "bundle, id received" + bundle.getString("docID"));

        PlacesFragment placesFragment = new PlacesFragment();
        placesFragment.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_visit,placesFragment,null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}