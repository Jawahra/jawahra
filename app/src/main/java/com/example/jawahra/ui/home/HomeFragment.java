package com.example.jawahra.ui.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.CovidProtocolActivity;
import com.example.jawahra.R;
import com.example.jawahra.adapters.DiscoverAdapterAPI28;
import com.example.jawahra.adapters.DiscoverAdapterAPI29;
import com.example.jawahra.models.DiscoverModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    ConstraintLayout constraintLayoutHome;
    Button btnLearnMore;
    Button btnCovidProtocols;
    RelativeLayout rlFbImg;
    TextView tvFbTitle;



    // Variables for Discover Cards
    ViewPager2 discoverViewPager2;
    ViewPager discoverViewPager;
    Handler discoverHandler = new Handler();
    List<DiscoverModel> listDiscover = new ArrayList<>();
    private DiscoverAdapterAPI28 discoverAdapter;


    List<String> listDiscUrl = new ArrayList<>(),
            listDiscTitle = new ArrayList<>(),
            listDiscEmirate = new ArrayList<>(),
            listDiscEmirateId = new ArrayList<>(),
            listDiscPlaceId = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        Load Data for Discover Cards
        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray discPlacesArray = obj.getJSONArray("discoverPlaces");

//            Iterate through array to get each value and add to another array which will be displayed in each discover card
            for (int i = 0; i < discPlacesArray.length(); i++){
                JSONObject discDetail = discPlacesArray.getJSONObject(i);
                listDiscUrl.add(discDetail.getString("coverImg"));
                listDiscEmirate.add(discDetail.getString("emirate"));
                listDiscTitle.add(discDetail.getString("name"));
                listDiscEmirateId.add(discDetail.getString("emirateId"));
                listDiscPlaceId.add(discDetail.getString("placeId"));
                listDiscover.add(new DiscoverModel(listDiscUrl.get(i), listDiscEmirate.get(i), listDiscTitle.get(i), listDiscEmirateId.get(i), listDiscPlaceId.get(i)));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Create hooks for variables
        if(getView() != null) {
            constraintLayoutHome = requireView().findViewById(R.id.fragment_home);
            btnLearnMore = requireView().findViewById(R.id.featured_banner_learn_more);
            tvFbTitle = requireView().findViewById(R.id.featured_banner_title);
            rlFbImg = requireView().findViewById(R.id.home_featured_img);
            btnCovidProtocols = requireView().findViewById(R.id.btn_covid_protocols);
            discoverViewPager2 = requireView().findViewById(R.id.discover_view_pager_2);
            discoverViewPager = requireView().findViewById(R.id.discover_view_pager);
        }

//        Open Covid Protocols Activity when button is pressed
        btnCovidProtocols.setOnClickListener(v -> startActivity(new Intent(getActivity(), CovidProtocolActivity.class)));

        getFeaturedBanner();

        setDiscoverPagerAPI28();

//        if (Build.VERSION.SDK_INT > 28){
//            setDiscoverPagerAPI29();
//        } else{
//            setDiscoverPagerAPI28();
//        }

    }

    private void setDiscoverPagerAPI28() {
        //setup adapter
        discoverAdapter = new DiscoverAdapterAPI28(getContext(), listDiscover, this);

        //set adapter to view pager
        discoverViewPager.setAdapter(discoverAdapter);
        discoverViewPager.setPageMargin(50);
        discoverViewPager.setPageTransformer(false, new ZoomInTransformer());
        automateViewPagerSwiping();
    }

    public class ZoomInTransformer implements ViewPager.PageTransformer {

        public static final float MAX_ROTATION = 90.0f;

        @Override
        public void transformPage( View page, float pos ) {
            float r = 1 - Math.abs(pos);
            page.setScaleY(0.85f + r*0.15f);
        }
    }

    private void automateViewPagerSwiping() {
        final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (discoverViewPager.getCurrentItem() == discoverAdapter.getCount() - 1) {
                    discoverViewPager.setCurrentItem(0);
                }
                else {
                    discoverViewPager.setCurrentItem(discoverViewPager.getCurrentItem() + 1, true);
                }
            }
        };

        Timer timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }


    //    Initialise settings, add infinite scroll and animations for Discover Cards
    private void setDiscoverPagerAPI29() {
//        Initialise view pager settings
        discoverViewPager2.setAdapter(new DiscoverAdapterAPI29(getContext(), this, listDiscover, discoverViewPager2));
        discoverViewPager2.setClipToPadding(false);
        discoverViewPager2.setClipChildren(false);
        discoverViewPager2.setOffscreenPageLimit(3);
        discoverViewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

//        Size of each card
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(25));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r*0.15f);
        });
//        Auto-scroll card views
        discoverViewPager2.setPageTransformer(compositePageTransformer);
        discoverViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                discoverHandler.removeCallbacks(discoverRunnable);
                discoverHandler.postDelayed(discoverRunnable,3000); // Slide duration
            }
        });
    }

//    Get data for featured hidden gem from Firestore Database
    private void getFeaturedBanner() {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("home").document("featured_banner");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()){
                    String fbEmirate = doc.getString("emirate");
                    String fbEmirateId = doc.getString("emirateId");
                    String fbPlaceId = doc.getString("placeId");
                    String fbTitle = doc.getString("name");
                    String fbImg = doc.getString("coverImg");

                    tvFbTitle.setText(fbTitle);

//                    Load image from a url string
                    if(getActivity() != null) {
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

//                    Open Place Details Fragment and pass relevant values
                    btnLearnMore.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("emirateName", fbEmirate);
                        bundle.putString("emirateId", fbEmirateId);
                        bundle.putString("placeId", fbPlaceId);
                        bundle.putString("placeName",fbTitle);
                        bundle.putString("placeImg", fbImg);

                        Log.d("VIEW_PAGER, EMIRATE", fbEmirateId + "");
                        Log.d("VIEW_PAGER, PLACE ID", fbPlaceId + "");

                        // Open another fragment
                        PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
                        placeDetailsFragment.setArguments(bundle);
                        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_placeDetailsFragment,bundle);
                    });


                }
            }
        });


    }
    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getContext().getAssets().open("discover_places.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private final Runnable discoverRunnable = new Runnable() {
        @Override
        public void run() {
            discoverViewPager2.setCurrentItem(discoverViewPager2.getCurrentItem() + 1);
        }
    };
}