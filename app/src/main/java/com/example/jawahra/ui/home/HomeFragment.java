package com.example.jawahra.ui.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.CovidProtocolActivity;
import com.example.jawahra.R;
import com.example.jawahra.adapters.DiscoverAdapter;
import com.example.jawahra.models.DiscoverModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Button btnCovidProtocols;
    RelativeLayout rlFbImg;
    TextView tvFbTitle;

    ViewPager2 discoverViewPager;
    Handler discoverHandler = new Handler();
    DiscoverAdapter discoverAdapter;
    List<DiscoverModel> listDiscover = new ArrayList<>();
    List<String> listDiscUrl = new ArrayList<>(),
            listDiscTitle = new ArrayList<>(),
            listDiscEmirate = new ArrayList<>(),
            listDiscEmirateId = new ArrayList<>(),
            listDiscPlaceId = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        discoverAdapter = new DiscoverAdapter(getContext(), listDiscover);
//        discoverViewPager.setAdapter(discoverAdapter);
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
        setDiscoverPager();
    }

    private void setDiscoverPager() {
//        listDiscover.add(new DiscoverModel("", "", "","",""));
        initDiscPlaces();
        int noOfDiscoverPlaces = 7;

//      Iterate through array to add data into card views
        Log.d("ARRAY_SIZE", listDiscEmirate.size() + "");
        for (int i = 0; i < noOfDiscoverPlaces; i++){
            listDiscover.add(new DiscoverModel(listDiscUrl.get(i), listDiscEmirate.get(i), listDiscTitle.get(i),listDiscEmirateId.get(i), listDiscPlaceId.get(i)));
            Log.d("DISCOVER_PAGER", listDiscTitle.get(i));
            Log.d("DISCOVER_PAGER", listDiscEmirate.get(i));

        }

//        Initialise view pager settings
        discoverViewPager.setAdapter(new DiscoverAdapter(getContext(), listDiscover, discoverViewPager));
        discoverViewPager.setClipToPadding(false);
        discoverViewPager.setClipChildren(false);
        discoverViewPager.setOffscreenPageLimit(3);
        discoverViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(25));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r*0.15f);
        });
//        Auto-scroll card views
        discoverViewPager.setPageTransformer(compositePageTransformer);
        discoverViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                discoverHandler.removeCallbacks(discoverRunnable);
                discoverHandler.postDelayed(discoverRunnable,3000); // Slide duration
            }
        });
    }

    private void initDiscPlaces() {
        listDiscUrl.add("https://firebasestorage.googleapis.com/v0/b/jawahra-ec2e2.appspot.com/o/images%2Fabu_dhabi%2Fforever_rose_cafe%2Fforeverrosecafe_2.jpg?alt=media&token=037c4a3d-201d-4612-ab7b-633bfac5664d");
        listDiscEmirate.add("Abu Dhabi");
        listDiscTitle.add("Forever Rose Cafe");
        listDiscEmirateId.add("abu_dhabi");
        listDiscPlaceId.add("0Cv0FjnvT1KdVVYWRmdm");

        listDiscUrl.add("https://firebasestorage.googleapis.com/v0/b/jawahra-ec2e2.appspot.com/o/images%2Fajman%2Fclay_corner_studio%2Fclaycornerstudio_1.jpg?alt=media&token=3926f531-4e89-49e8-ad5d-c982f3e50130");
        listDiscEmirate.add("Ajman");
        listDiscTitle.add("Clay Corner Studio");
        listDiscEmirateId.add("ajman");
        listDiscPlaceId.add("Idi7pikJyvy5KQkqcz0O");

        listDiscUrl.add("https://firebasestorage.googleapis.com/v0/b/jawahra-ec2e2.appspot.com/o/images%2Fdubai%2Fhive_board_game_cafe%2Fhiveboardgamecafe_4.jpg?alt=media&token=2ae140c9-9a44-4715-a1ea-7a0fb49d362a");
        listDiscEmirate.add("Dubai");
        listDiscTitle.add("Hive Board Game Cafe");
        listDiscEmirateId.add("dubai");
        listDiscPlaceId.add("vo3V74KvrnIyv7Wnfvra");

        listDiscUrl.add("https://firebasestorage.googleapis.com/v0/b/jawahra-ec2e2.appspot.com/o/images%2Ffujairah%2Fwadi_abadilah%2Fwadialabadilah_3.jpg?alt=media&token=1b2ff2b4-c213-4b22-80bc-751e1a16f5ff");
        listDiscEmirate.add("Fujairah");
        listDiscTitle.add("Wadi Abadilah");
        listDiscEmirateId.add("fujairah");
        listDiscPlaceId.add("nQkQmi2AyJG4R79oiSzk");

        listDiscUrl.add("https://firebasestorage.googleapis.com/v0/b/jawahra-ec2e2.appspot.com/o/images%2Fras_al_khaimah%2Fcrescent_moon_lake%2Fcrescentmoonlake_3.jpg?alt=media&token=12f25fce-64b9-473a-9be8-73c62a8e80b2");
        listDiscEmirate.add("Ras Al Khaimah");
        listDiscTitle.add("Crescent Moon Lake");
        listDiscEmirateId.add("ras_al_khaimah");
        listDiscPlaceId.add("3Q9xqQH5p8jDbP3PMqh9");

        listDiscUrl.add("https://firebasestorage.googleapis.com/v0/b/jawahra-ec2e2.appspot.com/o/images%2Fsharjah%2Fhouse_of_wisdom%2Fhouseofwisdom_5.jpg?alt=media&token=976451fd-f210-4849-a30d-ef7e5a3e409a");
        listDiscEmirate.add("Sharjah");
        listDiscTitle.add("House of Wisdom");
        listDiscEmirateId.add("sharjah");
        listDiscPlaceId.add("nmOdVA53mBlVsvowrpvm");

        listDiscUrl.add("https://firebasestorage.googleapis.com/v0/b/jawahra-ec2e2.appspot.com/o/images%2Fumm_al_quwain%2Fkite_beach_center%2Fkitebeachcenter_1.jpg?alt=media&token=dc72fba0-421b-4da1-8580-ee3da027b7c2");
        listDiscEmirate.add("Umm AL Quwain");
        listDiscTitle.add("Kite Beach Center");
        listDiscEmirateId.add("umm_al_quwain");
        listDiscPlaceId.add("VospVKGtM8G37aehiM1A");
    }

    private Runnable discoverRunnable = new Runnable() {
        @Override
        public void run() {
            discoverViewPager.setCurrentItem(discoverViewPager.getCurrentItem() + 1);
        }
    };







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
        List<String> discEmirates = new ArrayList<>();
        discEmirates.add("abu_dhabi");
        discEmirates.add("ajman");
        discEmirates.add("dubai");
        discEmirates.add("fujairah");
        discEmirates.add("ras_al_khaimah");
        discEmirates.add("sharjah");
        discEmirates.add("umm_al_quwain");

//        TODO do this for discover part instead
        // Iterate through array list containing emirate names to retrieve data from firestore
        for (int i = 0; i < discEmirates.size(); i++){
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("home")
                    .document("discover")
                    .collection("featured_places")
                    .document(discEmirates.get(i));

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

                        Log.d("FEATURED_BANNER", discEmirate + " item!");
                        Log.d("CHECK_ID", "PLACE ID : " + discPlaceId);
                        listDiscUrl.add(discUrl);
                        listDiscTitle.add(discTitle);
                        listDiscEmirate.add(discEmirate);
                        listDiscEmirateId.add(discEmirateId);
                        listDiscPlaceId.add(discPlaceId);



                    } else{
                        Log.d("Document", "No data");
                    }
                }
            });
        }
        int noOfDiscoverPlaces = 7;
    }
}