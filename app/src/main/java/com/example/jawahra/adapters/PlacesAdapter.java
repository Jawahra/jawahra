package com.example.jawahra.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.models.PlacesModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PlacesAdapter  extends FirestoreRecyclerAdapter<PlacesModel, PlacesAdapter.PlacesViewHolder> {

//    public String documentId;

    private OnListItemClick onListItemClick;

    public PlacesAdapter(@NonNull FirestoreRecyclerOptions<PlacesModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull PlacesViewHolder holder, int position, @NonNull PlacesModel model) {
        holder.listName.setText(model.getName());
        
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_places, parent, false);
        return new PlacesViewHolder(view);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView listName;
        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_emirate_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //get document ID of selected recyclerview card
            onListItemClick.OnItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getId(), getSnapshots().getSnapshot(getAdapterPosition()).getString("name"));
        }

    }

    public interface OnListItemClick {
        void OnItemClick(String placeId, String placeName);
    }
}
