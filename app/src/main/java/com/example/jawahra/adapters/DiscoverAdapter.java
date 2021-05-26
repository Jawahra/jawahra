package com.example.jawahra.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.jawahra.R;
import com.example.jawahra.models.DiscoverModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;

import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.SliderViewHolder>{

    private Context context;
    private List<DiscoverModel> listDiscover;
    private ViewPager2 viewPager2;

    public DiscoverAdapter(Context context, List<DiscoverModel> listDiscover, ViewPager2 viewPager2) {
        this.context = context;
        this.listDiscover = listDiscover;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from((context)).inflate(R.layout.list_item_discover, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setData(listDiscover.get(position));

        if (position == listDiscover.size() - 2){
            viewPager2.post(holder.runnable);
        }

    }

    @Override
    public int getItemCount() {
        return listDiscover.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        TextView emirate, name, emirateId, placeId;
        ImageView coverImg;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.discover_img);
            name = itemView.findViewById(R.id.discover_title);
            emirate = itemView.findViewById(R.id.discover_emirate);

        }

        void setData (DiscoverModel listDiscover){
            Glide.with(context).load(listDiscover.getCoverImg()).thumbnail(.25f).into(coverImg);
            name.setText(listDiscover.getName());
            emirate.setText(listDiscover.getEmirate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("emirateId", listDiscover.getEmirateId());
                    bundle.putString("placeId", listDiscover.getPlaceId());
                    bundle.putString("placeName",listDiscover.getName());
                    bundle.putString("placeImg", listDiscover.getCoverImg());

                    Log.d("VIEW_PAGER, EMIRATE", listDiscover.getEmirateId() + "");
                    Log.d("VIEW_PAGER, PLACE ID", listDiscover.getPlaceId() + "");

                    // Open another fragment
                    PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
                    placeDetailsFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_home,placeDetailsFragment,null);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                listDiscover.addAll(listDiscover);
                notifyDataSetChanged();
            }
        };

    }
}
