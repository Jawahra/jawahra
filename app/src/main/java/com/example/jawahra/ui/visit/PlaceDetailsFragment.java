package com.example.jawahra.ui.visit;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.SectionPagerAdapter;
import com.example.jawahra.models.PlaceDetailsModel;
import com.example.jawahra.ui.visit.childfragments.FaqsChildFragment;
import com.example.jawahra.ui.visit.childfragments.ImagesChildFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class PlaceDetailsFragment extends Fragment {

    //database
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference placeRef;
    private CollectionReference detailsRef, imagesRef, faqsRef;

    //child fragments
//    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;

    private TextView placeName, placeDesc, placeLocation;
    public String emirateId, placeId, placeTitle, detailsId, imagesId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get bundle values
        Bundle bundle = getArguments();
        if(bundle != null){
            emirateId = bundle.getString("emirateId");
            placeId = bundle.getString("placeId");
            placeTitle = bundle.getString("placeName");

            Log.d("CHECK_ID", "bundle, emirates id  part2 " + bundle.getString("emirateId"));
            Log.d("CHECK_ID", "bundle, place id  part2 " + bundle.getString("placeId"));
        }

        //QUERY
        firebaseFirestore = FirebaseFirestore.getInstance();
        placeRef = firebaseFirestore.collection("emirates")
                .document(emirateId)
                .collection("places")
                .document(placeId);

        Log.d("CHECK_ID", "onCreate: placeRef : " + placeRef);

        detailsRef = placeRef.collection("details");
        imagesRef = placeRef.collection("images");
        faqsRef = placeRef.collection("faqs");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_place_details, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        //set up child fragments
        viewPager = root.findViewById(R.id.view_pager);
        tabLayout = root.findViewById(R.id.tab_layout);

        //set views
        placeName = root.findViewById(R.id.place_name);
        placeDesc = root.findViewById(R.id.place_desc);
        placeLocation = root.findViewById(R.id.place_location);

        GetValues();

        return root;
    }

    public void GetValues(){
        detailsRef.get()
            .addOnSuccessListener(snapshot -> {

                for (QueryDocumentSnapshot snapshots : snapshot){
                    Log.d("CHECK_ID", "onCreate: DETAILSREF.GET IS WORKING???????");

                    PlaceDetailsModel placeDetailsModel = snapshots.toObject(PlaceDetailsModel.class);
                    /*String desc = snapshots.getString("desc");
                    String map = snapshots.getString("map");*/

                    String desc = placeDetailsModel.getDesc();
                    String map = placeDetailsModel.getMap();

                    Log.d("CHECK_ID", "onCreate: DESC VALUE: " +desc);
                    Log.d("CHECK_ID", "onCreate: MAP VALUE: " +map);

                    placeName.setText(placeTitle);
                    placeDesc.setText(desc);
                    placeLocation.setText(map);
                }
            })
            .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist" ));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager(),2);

        adapter.addFragment(new ImagesChildFragment(), "Images");
        adapter.addFragment(new FaqsChildFragment(), "FAQS");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}