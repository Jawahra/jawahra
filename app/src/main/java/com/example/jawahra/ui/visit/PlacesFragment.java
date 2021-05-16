package com.example.jawahra.ui.visit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.PlacesAdapter;
import com.example.jawahra.models.PlacesModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlacesFragment extends Fragment implements PlacesAdapter.OnListItemClick {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView listPlaces;
    private PlacesAdapter adapter;
    private String emirateId, emirateName;
    public TextView emirateTitle;

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
        View root = inflater.inflate(R.layout.fragment_places, container, false);
        /*if (container != null) {
            container.removeAllViews();
        }*/

        emirateTitle = root.findViewById(R.id.emirate_title);
        emirateTitle.setText(emirateName);

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
        return root;
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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_places,placeDetailsFragment,null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
