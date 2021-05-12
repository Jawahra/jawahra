package com.example.jawahra.ui.visit;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        if (container != null) {
            container.removeAllViews();
        }

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
    public void OnItemClick(String placeId, String placeName) {
        Bundle bundle = new Bundle();
        bundle.putString("emirateId", emirateId);
        bundle.putString("placeId", placeId);
        bundle.putString("placeName",placeName);

        Log.d("CHECK_ID", "PLACE ID : " + placeId);

        PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
        placeDetailsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_places,placeDetailsFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}



//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public PlacesFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment PlacesFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static PlacesFragment newInstance(String param1, String param2) {
//        PlacesFragment fragment = new PlacesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }