package com.example.jawahra.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jawahra.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private ArrayList<GalleryUrl> mediaLinks;
    private Context context;

    public GalleryAdapter(Context context, ArrayList<GalleryUrl> mediaLinks){
        this.context = context;
        this.mediaLinks = mediaLinks;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Glide.with(context).load(mediaLinks.get(position).getGalleryUrl()).thumbnail(0.25f).into(holder.img);
        Log.d("MYGALLERY", "onBindViewHolder: THIS IS RUNNING");
    }

    @Override
    public int getItemCount() {
        return mediaLinks.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img;
        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
