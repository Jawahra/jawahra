package com.example.jawahra.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jawahra.R;
import com.example.jawahra.models.PlaceDetailsModel;
import com.example.jawahra.models.PlacesModel;
import com.example.jawahra.models.UserLocationModel;
import com.example.jawahra.models.UserModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Converter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference detailsRef;
    private DocumentReference userLocationRef;
    public String emirateId, placeId, placeTitle;

    // place and user variables
    private UserLocationModel userPosition;
    private GeoPoint userGeoPoint;
    private double placeLat, placeLng, userLat, userLng;
    private LatLng placeLatLng, userLatLng;
    private MarkerOptions placeMarker, userMarker;

    private PolylineOptions direction;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            readData(new FirestoreCallback() {
                @Override
                public void onCallback(GeoPoint placeGeoPoint, DocumentSnapshot snapshot) {

                    placeLat = placeGeoPoint.getLatitude();
                    placeLng = placeGeoPoint.getLongitude();

                    placeLatLng = new LatLng(placeLat, placeLng);
                    placeMarker = new MarkerOptions().position(placeLatLng)
                            .title(placeTitle);

                    Log.d("CHECK_MAP", "onMapReady: retrieved place position: " + placeTitle
                            + "\nlatitude: " + placeLat
                            + "\nlongitude: " + placeLng);

                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        // Get user details
                        userPosition = snapshot.toObject(UserLocationModel.class);
                        userGeoPoint = snapshot.getGeoPoint("geoPoint");

                        // Set latitude and longitude for user and place
                        userLat = userGeoPoint.getLatitude();
                        userLng = userGeoPoint.getLongitude();
                        userLatLng = new LatLng(userLat, userLng);
                        userMarker = new MarkerOptions().position(userLatLng)
                                .title(userPosition.getUserModel().username);

                        placeMarker.snippet("Tap to see directions");

                        Log.d("CHECK_MAP", "onMapReady: retrieved user position: " + userPosition.getUserModel().username
                                + "\nlatitude: " + userLat
                                + "\nlongitude: " + userLng);

                        googleMap.addMarker(userMarker);
                    }

                    googleMap.addMarker(placeMarker);

                    // Camera position
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLatLng));

                    // Zoom animation
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 13.0f), 900, null);
                }
            });

//            userLocationRef.get().addOnSuccessListener(snapshots -> {
//
//                userPosition = snapshots.toObject(UserLocationModel.class);
//                userGeoPoint = snapshots.getGeoPoint("geoPoint");
//
//                userLat = userGeoPoint.getLatitude();
//                userLng = userGeoPoint.getLongitude();
//                userLatLng = new LatLng(userLat, userLng);
//
//                userMarker = new MarkerOptions().position(userLatLng).title(userPosition.getUserModel().username);
//
//                googleMap.addMarker(userMarker);
//
//                Log.d("CHECK_MAP", "onMapReady: retrieved user position: " + userPosition.getUserModel().username
//                        + "\nlatitude: " + userLat
//                        + "\nlongitude: "+ userLng);
//
//            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        //get bundle values
        Bundle bundle = getArguments();
        if(bundle != null){
            emirateId = bundle.getString("emirateId");
            placeId = bundle.getString("placeId");
            placeTitle = bundle.getString("placeName");
        }



        // Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("emirateId", emirateId);
                bundle.putString("placeId", placeId);
                bundle.putString("placeName", String.valueOf(placeTitle));

                PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
                placeDetailsFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_places,placeDetailsFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void readData(FirestoreCallback firestoreCallback){


        detailsRef = fStore.collection("emirates")
                .document(emirateId).collection("places")
                .document(placeId).collection("details");

        detailsRef.get().addOnSuccessListener(snapshot -> {
            for (QueryDocumentSnapshot snapshots : snapshot) {
                GeoPoint placeGeoPoint = snapshots.getGeoPoint("position");

                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    userLocationRef = fStore.collection("user locations")
                            .document(FirebaseAuth.getInstance().getUid());

                    userLocationRef.get().addOnSuccessListener(docSnapshots -> {

                        userPosition = docSnapshots.toObject(UserLocationModel.class);
                        userGeoPoint = docSnapshots.getGeoPoint("geoPoint");

                        firestoreCallback.onCallback(placeGeoPoint, docSnapshots);

                    });
                } else { firestoreCallback.onCallback(placeGeoPoint, null);}
            }

        });

    }

    public interface FirestoreCallback {
        void onCallback(GeoPoint placeGeoPoint, DocumentSnapshot snapshot);
    }
}