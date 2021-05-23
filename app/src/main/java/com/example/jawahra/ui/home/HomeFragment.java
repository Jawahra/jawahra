package com.example.jawahra.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
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
    TextView tvFbTitle, tvFbEmirate;
    ImageSlider imageSlider;
    List<SlideModel> imageList;
    List<String> listFbId = new ArrayList<>(),
        listFbUrl = new ArrayList<>(),
        listFbTitle = new ArrayList<>(),
        listFbEmirate = new ArrayList<>();


    Animation animTransition;
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
//            imageSlider = requireView().findViewById(R.id.home_featured_banner);
//            tvFbTitle = requireView().findViewById(R.id.featured_banner_title);
//            tvFbEmirate = requireView().findViewById(R.id.featured_banner_emirate);
//            btnCovidProtocols = requireView().findViewById(R.id.btn_covid_protocols);
            discoverViewPager = requireView().findViewById(R.id.discover_view_pager);
            animTransition = AnimationUtils.loadAnimation(getContext(), R.anim.transition);
        }

//        btnCovidProtocols.setOnClickListener(v -> {
//            startActivity(new Intent(getActivity(), CovidProtocolActivity.class));
//        });


        getFeaturedPlaces();
        discoverAdapter = new DiscoverAdapter(getContext(), listDiscoverPlaces);
        discoverViewPager.startAutoScroll(true);

    }


    private void getFeaturedPlaces() {
        imageList = new ArrayList<>();
        List<String> fbEmirates = new ArrayList<>();
        fbEmirates.add("abu_dhabi");
        fbEmirates.add("ajman");
        fbEmirates.add("dubai");
        fbEmirates.add("fujairah");
        fbEmirates.add("ras_al_khaimah");
        fbEmirates.add("sharjah");
        fbEmirates.add("umm_al_quwain");

//        TODO do this for discover part instead
        // Iterate through array list containing emirate names to retrieve data from firestore
        for (int i = 0; i < fbEmirates.size(); i++){
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("home")
                    .document("featured_banner")
                    .collection("featured_places")
                    .document(fbEmirates.get(i));

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        // Get data fields from firestore database
                            String fbId = doc.getString("placeId");
                            String fbUrl = doc.getString("coverImg");
                            String fbTitle = doc.getString("name");
                            String fbEmirate = doc.getString("emirate");
                            // Display data to image slideshow for featured banner
//                            imageList.add(new SlideModel(fbUrl, ScaleTypes.CENTER_CROP));
//                            imageSlider.setImageList(imageList);

                            // Add data to list
                            listFbId.add(fbId);
                            listFbUrl.add(fbUrl);
                            listFbTitle.add(fbTitle);
                            listFbEmirate.add(fbEmirate);

                            listDiscoverPlaces.add(new DiscoverModel(fbUrl, fbEmirate, fbTitle, fbId));
                            discoverAdapter.notifyDataSetChanged();


                        Log.d("FEATURED_BANNER", fbEmirate + " item!");
                        Log.d("CHECK_ID", "PLACE ID : " + fbId);

//                        imageSlider.setItemChangeListener(new ItemChangeListener() {
//                            @Override
//                            public void onItemChanged(int i) {
//                                tvFbTitle.setText(listFbTitle.get(i));
//                                tvFbEmirate.setText(listFbEmirate.get(i));
//
//                            }
//                        });


//                        imageSlider.setItemClickListener(new ItemClickListener() {
//                            @Override
//                            public void onItemSelected(int i) {
//                                Log.d("FEATURED_BANNER", listFbEmirate.get(i) + " item clicked!");
//                                Bundle bundle = new Bundle();
//                                bundle.putString("emirateId", listFbEmirate.get(i));
//                                bundle.putString("placeId", listFbId.get(i));
//                                bundle.putString("placeName",listFbTitle.get(i));
//                                bundle.putString("placeImg", listFbUrl.get(i));
//                                Log.d("CHECK_ID", "PLACE ID : " + listFbId.get(i));
//
//                                // Open another fragment
//                                PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
//                                placeDetailsFragment.setArguments(bundle);
//                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.fragment_home,placeDetailsFragment,null);
//                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                fragmentTransaction.addToBackStack(null);
//                                fragmentTransaction.commit();
//
//                            }
//                        });
                    } else{
                        Log.d("Document", "No data");
                    }
                }
            });

        }

    }
}