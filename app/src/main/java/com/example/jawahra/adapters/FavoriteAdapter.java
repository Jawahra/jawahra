package com.example.jawahra.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {
    private List<Favorite> favorites = new ArrayList<>();
    private final OnListItemClick onListItemClick;

    public FavoriteAdapter(OnListItemClick onListItemClick) {
        this.onListItemClick = onListItemClick;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favorite, parent, false);

        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        Favorite currentFav = favorites.get(position);
        Log.d("SAVE_FAV", "onBindViewHolder: getID" + currentFav.getId());
        holder.position = currentFav.getId();
        holder.textViewName.setText(currentFav.getTitle());
        holder.textViewEmirate.setText(currentFav.getEmirate());
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void setFavorites(List<Favorite> favorites){
        this.favorites = favorites;
        Log.d("SAVE_FAV", "setFavorites: CHECK LIST" + this.favorites);
        notifyDataSetChanged();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textViewName;
        private TextView textViewEmirate;
        private int position;


        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.fav_place);
            textViewEmirate = itemView.findViewById(R.id.fav_emirate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           onListItemClick.OnItemClick(position);
        }
    }

    public interface OnListItemClick{
        void OnItemClick(int position);
    }
}
