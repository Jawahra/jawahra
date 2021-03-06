package com.example.jawahra.ui.news;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.R;
import com.example.jawahra.adapters.UpcomingEventsAdapter;
import com.example.jawahra.adapters.UpcomingPlacesAdapter;
import com.example.jawahra.models.UpcomingEventsModel;
import com.example.jawahra.models.UpcomingPlacesModel;
import com.example.jawahra.ui.UEDetailsFragment;
import com.example.jawahra.ui.UPDetailsFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NewsFragment extends Fragment implements UpcomingPlacesAdapter.OnCardsClickUP, UpcomingEventsAdapter.onCardsClickUE{

    // String variables for feature banner
    String featuredEventID, eventEmirate, eventName, eventImg;
    TextView tvTitleFeaturedEvent;
    RelativeLayout rlFeaturedImg;

    private UpcomingPlacesAdapter adapterUP;
    FirestoreRecyclerOptions<UpcomingPlacesModel> optionsUP;

    private UpcomingEventsAdapter adapterUE;
    FirestoreRecyclerOptions<UpcomingEventsModel> optionsUE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



//        Query to find collection for upcoming places
        Query queryUP = firebaseFirestore.collection("upcoming_places");

        optionsUP = new FirestoreRecyclerOptions.Builder<UpcomingPlacesModel>()
                .setQuery(queryUP, UpcomingPlacesModel.class)
                .build();

//        Query to find collection for upcoming events
        Query queryUE = firebaseFirestore.collection("upcoming_events");

        optionsUE = new FirestoreRecyclerOptions.Builder<UpcomingEventsModel>()
                .setQuery(queryUE, UpcomingEventsModel.class)
                .build();


//        Initialise adapter when view is created
        adapterUP = new UpcomingPlacesAdapter(optionsUP, this, getActivity());
        adapterUE = new UpcomingEventsAdapter(optionsUE, this, getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        Hook variable to Recycler view
        RecyclerView listUpcomingPlaces = requireView().findViewById(R.id.list_upcoming_places);
        RecyclerView listUpcomingEvents = requireView().findViewById(R.id.list_upcoming_events);


        Button btnLearnMore = requireView().findViewById(R.id.btn_learn_more);
        tvTitleFeaturedEvent = requireView().findViewById(R.id.featured_title);
        rlFeaturedImg = requireView().findViewById(R.id.featured_img);

//        Retrieve and set data for featured events banner
        setFeaturedEvent();

        btnLearnMore.setOnClickListener(v -> onCardClickUE(featuredEventID, eventEmirate, eventName, eventImg));


//        Set layout manager and adapter after view is created
        listUpcomingPlaces.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listUpcomingPlaces.setAdapter(adapterUP);

        listUpcomingEvents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listUpcomingEvents.setAdapter(adapterUE);
    }

    @Override
    public void onCardClickUP(String upcomingPlaceID, String placeEmirate, String placeName, String placeImg) {
        Log.d("ITEM_CLICK", "Clicked item id: " + upcomingPlaceID + "\nEmirate: " + placeEmirate + "\nName: " + placeName + "\nImg URL: " + placeImg);
        Bundle bundle = new Bundle();
        bundle.putString("upcomingPlaceID", upcomingPlaceID);
        bundle.putString("placeEmirate", placeEmirate);
        bundle.putString("placeName", placeName);
        bundle.putString("placeImg", placeImg);


        UPDetailsFragment upDetailsFragment = new UPDetailsFragment();
        upDetailsFragment.setArguments(bundle);
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_events_to_UPDetailsFragment,bundle);
    }

    private void setFeaturedEvent() {
        featuredEventID = "HuZ7ruLjER0WXEYKyhyg";
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("upcoming_events").document(featuredEventID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    eventEmirate = doc.getString("eventEmirate");
                    eventName = doc.getString("eventName");
                    tvTitleFeaturedEvent.setText(eventName);

                    eventImg = doc.getString("eventImg");
                    if (getActivity() != null) {
                        Glide.with(getActivity())
                                .load(eventImg)
                                .thumbnail(.25f)
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        rlFeaturedImg.setBackground(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });

                    } else {
                        Log.d("Document", "No data");
                    }
                }
            }
        });
    }


    @Override
    public void onCardClickUE(String upcomingEventID, String eventEmirate, String eventName, String eventImg) {
        Log.d("ITEM_CLICK", "Clicked item id: " + upcomingEventID + "\nEmirate: " + eventEmirate + "\nName: " + eventName + "\nImg URL: " + eventImg);
        Bundle bundle = new Bundle();
        bundle.putString("upcomingEventID", upcomingEventID);
        bundle.putString("eventEmirate", eventEmirate);
        bundle.putString("eventName", eventName);
        bundle.putString("eventImg", eventImg);


        UEDetailsFragment ueDetailsFragment = new UEDetailsFragment();
        ueDetailsFragment.setArguments(bundle);
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_events_to_UEDetailsFragment,bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterUP.startListening();
        adapterUE.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterUP.stopListening();
        adapterUE.stopListening();
    }
}
