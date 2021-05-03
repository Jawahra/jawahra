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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.jawahra.R;
import com.example.jawahra.models.UpcomingPlacesModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UpcomingPlacesAdapter extends FirestoreRecyclerAdapter<UpcomingPlacesModel, UpcomingPlacesAdapter.UpcomingPlacesViewHolder> {

    private OnCardsClickUP onCardsClickUP;
    private Context context;

    public UpcomingPlacesAdapter(@NonNull FirestoreRecyclerOptions<UpcomingPlacesModel> options, OnCardsClickUP onCardClickUP, Context context) {
        super(options);
        this.onCardsClickUP = onCardClickUP;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UpcomingPlacesViewHolder holder, int position, @NonNull UpcomingPlacesModel model) {
//                get id of selected card
//                upcomingPlaceId = getSnapshots().getSnapshot(position).getId();

        holder.placeName.setText(model.getPlaceName());
        holder.placeEmirate.setText(model.getPlaceEmirate());

//                Change s
//                ettings to make image load faster
        RequestOptions repOpt = RequestOptions
                .fitCenterTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .override(holder.placeImg.getWidth(),holder.placeImg.getHeight()); // Overrides size of downloaded image and converts it's bitmaps to your desired image size;

        //                Get url string of image from document in Firestore and set ImageView to that image
        Glide.with(context)
                .load(model.getPlaceImg())
                .apply(repOpt)
                .into(holder.placeImg);
    }

    @NonNull
    @Override
    public UpcomingPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_upcoming_place, parent, false);
        return new UpcomingPlacesViewHolder(view);
    }

    //Contain data for recycler view
    public class UpcomingPlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView placeName, placeEmirate;
        private final ImageView placeImg;


        public UpcomingPlacesViewHolder(@NonNull View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.upcoming_place_name);
            placeEmirate = itemView.findViewById(R.id.upcoming_place_emirate);
            placeImg = itemView.findViewById(R.id.upcoming_place_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCardsClickUP.onCardClickUP(getSnapshots().getSnapshot(getAdapterPosition()).getId(), getStringFromSS("placeEmirate"), getStringFromSS("placeName"), getStringFromSS("placeImg"));
        }

        String getStringFromSS(String string){
            return getSnapshots().getSnapshot(getAdapterPosition()).getString(string);

        }
    }



    public interface OnCardsClickUP {
        void onCardClickUP(String upcomingPlaceID, String placeEmirate, String placeName, String placeImg);
    }
}