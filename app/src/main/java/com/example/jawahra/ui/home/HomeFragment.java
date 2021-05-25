package com.example.jawahra.ui.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.CovidProtocolActivity;
import com.example.jawahra.R;
import com.example.jawahra.adapters.DiscoverAdapter;
import com.example.jawahra.models.DiscoverModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Button btnCovidProtocols;
    RelativeLayout rlFbImg;
    TextView tvFbTitle;

    HorizontalInfiniteCycleViewPager discoverViewPager;
    DiscoverAdapter discoverAdapter;
    List<DiscoverModel> listDiscoverPlaces = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getView() != null) {
            tvFbTitle = requireView().findViewById(R.id.featured_banner_title);
            rlFbImg = requireView().findViewById(R.id.home_featured_img);
            btnCovidProtocols = requireView().findViewById(R.id.btn_covid_protocols);
            discoverViewPager = requireView().findViewById(R.id.discover_view_pager);
        }

        btnCovidProtocols.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CovidProtocolActivity.class));
        });


        getFeaturedBanner();
        getDiscoverPlaces();
        discoverAdapter = new DiscoverAdapter(getContext(), listDiscoverPlaces);
        discoverViewPager.setAdapter(discoverAdapter);

    }

    private void getFeaturedBanner() {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("home").document("featured_banner");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()){
                    String fbEmirateId = doc.getString("emirateId");
                    String fbPlaceId = doc.getString("placeId");
                    String fbTitle = doc.getString("name");
                    String fbImg = doc.getString("coverImg");

                    tvFbTitle.setText(fbTitle);

                    Glide.with(getActivity())
                            .load(fbImg)
                            .thumbnail(.25f)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    rlFbImg.setBackground(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });



                    rlFbImg.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("emirateId", fbEmirateId);
                        bundle.putString("placeId", fbPlaceId);
                        bundle.putString("placeName",fbTitle);
                        bundle.putString("placeImg", fbImg);

                        Log.d("VIEW_PAGER, EMIRATE", fbEmirateId + "");
                        Log.d("VIEW_PAGER, PLACE ID", fbPlaceId + "");

                        // Open another fragment
                        PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
                        placeDetailsFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_home,placeDetailsFragment,null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    });


                }
            }
        });


    }

    private void callPlaceDetails(String emirateId, String placeId, String name, String placeImg) {
        Bundle bundle = new Bundle();
        bundle.putString("emirateId", emirateId);
        bundle.putString("placeId", placeId);
        bundle.putString("placeName", name);
        bundle.putString("placeImg", placeImg);

//        Log.d("VIEW_PAGER, EMIRATE", emirateId);
//        Log.d("VIEW_PAGER, PLACE ID", placeId);

        // Open another fragment
        PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
        placeDetailsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_home,placeDetailsFragment,null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void getDiscoverPlaces() {
        List<String> DiscEmirates = new ArrayList<>();
        DiscEmirates.add("abu_dhabi");
        DiscEmirates.add("ajman");
        DiscEmirates.add("dubai");
        DiscEmirates.add("fujairah");
        DiscEmirates.add("ras_al_khaimah");
        DiscEmirates.add("sharjah");
        DiscEmirates.add("umm_al_quwain");

//        TODO do this for discover part instead
        // Iterate through array list containing emirate names to retrieve data from firestore
        for (int i = 0; i < DiscEmirates.size(); i++){
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("home")
                    .document("discover")
                    .collection("featured_places")
                    .document(DiscEmirates.get(i));

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        // Get data fields from firestore database
                            String discUrl = doc.getString("coverImg");
                            String discTitle = doc.getString("name");
                            String discEmirate = doc.getString("emirate");
                            String discEmirateId = doc.getString("emirateId");
                            String discPlaceId = doc.getString("placeId");


                        listDiscoverPlaces.add(new DiscoverModel(discUrl, discEmirate, discTitle, discEmirateId, discPlaceId));
                            discoverAdapter.notifyDataSetChanged();

                        Log.d("FEATURED_BANNER", discEmirate + " item!");
                        Log.d("CHECK_ID", "PLACE ID : " + discPlaceId);

                    } else{
                        Log.d("Document", "No data");
                    }
                }
            });

        }

    }
}