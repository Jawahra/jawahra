package com.example.jawahra.ui.events;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.models.UpcomingEventsModel;
import com.example.jawahra.models.UpcomingPlacesModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class EventsFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;

    private FirestoreRecyclerAdapter adapterUP;
    FirestoreRecyclerOptions<UpcomingPlacesModel> optionsUP;

    private FirestoreRecyclerAdapter adapterUE;
    FirestoreRecyclerOptions<UpcomingEventsModel> optionsUE;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();


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
        setAdapterUpcomingPlaces();
        setAdapterUpcomingEvents();

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Hook variable to Recycler view
        RecyclerView listUpcomingPlaces = requireView().findViewById(R.id.list_upcoming_places);
        RecyclerView listUpcomingEvents = requireView().findViewById(R.id.list_upcoming_events);

//        Set layout manager and adapter after view is created
        listUpcomingPlaces.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listUpcomingPlaces.setAdapter(adapterUP);

        listUpcomingEvents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listUpcomingEvents.setAdapter(adapterUE);

    }

//    Function to initialise adapter
    private void setAdapterUpcomingPlaces() {
        adapterUP = new FirestoreRecyclerAdapter<UpcomingPlacesModel, UpcomingPlacesViewHolder>(optionsUP) {
            @NonNull
            @Override
            public UpcomingPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_upcoming_place, parent, false);
                return new UpcomingPlacesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UpcomingPlacesViewHolder holder, int position, @NonNull UpcomingPlacesModel model) {
                holder.listName.setText(model.getName());
                holder.listEmirate.setText(model.getEmirate());
            }
        };

    }

    private void setAdapterUpcomingEvents() {
        adapterUE = new FirestoreRecyclerAdapter<UpcomingEventsModel, UpcomingEventsViewHolder>(optionsUE) {
            @NonNull
            @Override
            public UpcomingEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_upcoming_event, parent, false);
                return new UpcomingEventsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UpcomingEventsViewHolder holder, int position, @NonNull UpcomingEventsModel model) {
                holder.eventEmirate.setText(model.getEventEmirate());
                holder.eventName.setText(model.getEventName());

//                Put this in function and add delay handler
                if (model.getEventDate() != null){
                    String strEventDate = convertDateToString(model.getEventDate());
                    holder.eventDate.setText(strEventDate);

                }
            }
        };
    }

    private String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-MM-yyyy");
        return dateFormat.format(date);
    }


    //          Contain data for recycler view
    private static class UpcomingPlacesViewHolder extends RecyclerView.ViewHolder{
        private final TextView listName;
    private final TextView listEmirate;


        public UpcomingPlacesViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.upcoming_place_name);
            listEmirate = itemView.findViewById(R.id.upcoming_place_emirate);
        }

    }

    private static class UpcomingEventsViewHolder extends RecyclerView.ViewHolder{
        private final TextView eventEmirate, eventName, eventDate;

        public UpcomingEventsViewHolder(@NonNull View itemView) {
            super(itemView);

            eventEmirate = itemView.findViewById(R.id.upcoming_event_emirate);
            eventName = itemView.findViewById(R.id.upcoming_event_name);
            eventDate  = itemView.findViewById(R.id.upcoming_event_date);

        }
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