package com.example.jawahra.adapters;

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

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favorite, parent, false);

        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        Favorite currentFav = favorites.get(position);
        holder.textViewName.setText(currentFav.getTitle());
        holder.textViewEmirate.setText(currentFav.getEmirate());
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void setFavorites(List<Favorite> favorites){
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textViewEmirate;

        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.fav_place);
            textViewEmirate = itemView.findViewById(R.id.fav_emirate);
        }
    }
}
