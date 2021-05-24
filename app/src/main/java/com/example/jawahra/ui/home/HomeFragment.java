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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.CovidProtocolActivity;
import com.example.jawahra.R;
import com.example.jawahra.adapters.DiscoverAdapter;
import com.example.jawahra.models.DiscoverModel;
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
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("emirates")
                .document("fujairah")
                .collection("places")
                .document("dnTpL704ybhyzp57BHS9");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()){
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

                }
            }
        });
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
                    .document("featured_banner")
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