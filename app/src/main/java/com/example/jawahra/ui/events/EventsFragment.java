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
import com.example.jawahra.models.UpcomingPlacesModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class EventsFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<UpcomingPlacesModel> options;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();


//        Query to find right collection
        Query query = firebaseFirestore.collection("upcoming_places");

        options = new FirestoreRecyclerOptions.Builder<UpcomingPlacesModel>()
                .setQuery(query, UpcomingPlacesModel.class)
                .build();

//        Initialise adapter when view is created
        setAdapterUpcomingPlaces();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Hook variable to Recycler view
        RecyclerView listUpcomingPlaces = requireView().findViewById(R.id.list_upcoming_places);

//        Set layout manager and adapter after view is created
        listUpcomingPlaces.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listUpcomingPlaces.setAdapter(adapter);

    }

//    Function to initialise adapter
    private void setAdapterUpcomingPlaces() {
        adapter = new FirestoreRecyclerAdapter<UpcomingPlacesModel, UpcomingPlacesViewHolder>(options) {
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

//          Contain data for recycler view
    private static class UpcomingPlacesViewHolder extends RecyclerView.ViewHolder{
        private final TextView listName;
    private final TextView listEmirate;


        public UpcomingPlacesViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.upcoming_places_name);
            listEmirate = itemView.findViewById(R.id.upcoming_places_emirate);
        }

        public TextView getListName() {
            return listName;
        }

        public TextView getListEmirate() {
            return listEmirate;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}