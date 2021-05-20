package com.example.jawahra.ui.visit;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.PlacesAdapter;
import com.example.jawahra.models.PlacesModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PlacesFragment extends Fragment implements PlacesAdapter.OnListItemClick {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView listPlaces;
    private PlacesAdapter adapter;
    private String emirateId, emirateName;
//    public TextView emirateTitle;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get bundle values
        Bundle bundle = getArguments();
        if(bundle != null){
            emirateId = bundle.getString("docID");
            emirateName = bundle.getString("emirateName");
            Log.d("CHECK_ID", "bundle, id  part2 " + bundle.getString("docID"));
        }

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_places, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        /*emirateTitle = root.findViewById(R.id.emirate_title);
        emirateTitle.setText(emirateName);*/

        firebaseFirestore = FirebaseFirestore.getInstance();
        listPlaces = root.findViewById(R.id.list_places);

        //query
        CollectionReference query = firebaseFirestore.collection("emirates")
                .document(emirateId)
                .collection("places");

        FirestoreRecyclerOptions<PlacesModel> options = new FirestoreRecyclerOptions.Builder<PlacesModel>()
                .setQuery(query, PlacesModel.class)
                .build();

        Log.d("CHECK_ID", query.toString());

        adapter = new PlacesAdapter(options, this, getContext());

        listPlaces.setHasFixedSize(true);
        listPlaces.setLayoutManager(new GridLayoutManager(root.getContext(),2));
        listPlaces.setAdapter(adapter);

        initToolBar();
        setCollapsingToolBar();
        return root;
    }

    private void initToolBar(){
        Toolbar toolbar = root.findViewById(R.id.places_toolbar);
        ((AppCompatActivity) requireContext()).setSupportActionBar(toolbar);

        if(((AppCompatActivity) requireContext()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) requireContext()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }
    private void setCollapsingToolBar(){
        final CollapsingToolbarLayout collapsingToolbar = root.findViewById(R.id.places_collapsing_toolbar);
        //Set title font
        final Typeface fontPlayFairBold = ResourcesCompat.getFont(requireContext(), R.font.playfair_display_sc_bold);
        collapsingToolbar.setCollapsedTitleTypeface(fontPlayFairBold);
        collapsingToolbar.setExpandedTitleTypeface(fontPlayFairBold);
//        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setTitle(emirateName);
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
    public void OnItemClick(String placeId, String placeName, String placeImg) {
        Bundle bundle = new Bundle();
        bundle.putString("emirateId", emirateId);
        bundle.putString("placeId", placeId);
        bundle.putString("placeName",placeName);
        bundle.putString("placeImg", placeImg);

        Log.d("CHECK_ID", "PLACE ID : " + placeId);

        PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
        placeDetailsFragment.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_places,placeDetailsFragment,null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
