package com.example.jawahra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jawahra.R;
import com.example.jawahra.models.PlacesModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PlacesAdapter  extends FirestoreRecyclerAdapter<PlacesModel, PlacesAdapter.PlacesViewHolder> {

//    public String documentId;

    private final Context context;
    private final OnListItemClick onListItemClick;

    public PlacesAdapter(@NonNull FirestoreRecyclerOptions<PlacesModel> options, OnListItemClick onListItemClick, Context context) {
        super(options);
        this.onListItemClick = onListItemClick;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PlacesViewHolder holder, int position, @NonNull PlacesModel model) {
        holder.listName.setText(model.getName());
        Glide.with(context).load(model.getCoverImg()).into(holder.cardImage);
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_places, parent, false);
        return new PlacesViewHolder(view);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView listName;
        public ImageView cardImage;
        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_emirate_name);
            cardImage = itemView.findViewById(R.id.list_places_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //get document ID of selected recyclerview card
            onListItemClick.OnItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getId(), getSnapshots().getSnapshot(getAdapterPosition()).getString("name"), getSnapshots().getSnapshot(getAdapterPosition()).getString("coverImg"));
        }

    }

    public interface OnListItemClick {
        void OnItemClick(String placeId, String placeName, String placeImg);
    }
}
