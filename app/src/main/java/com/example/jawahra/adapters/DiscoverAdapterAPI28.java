package com.example.jawahra.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.jawahra.R;
import com.example.jawahra.models.DiscoverModel;
import com.example.jawahra.ui.home.HomeFragment;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;

import java.util.List;

public class DiscoverAdapterAPI28 extends PagerAdapter {
    private Context context;
    private List<DiscoverModel> listDiscover;
    private HomeFragment homeFragment;

    public DiscoverAdapterAPI28(Context context, List<DiscoverModel> listDiscover, HomeFragment homeFragment) {
        this.context = context;
        this.listDiscover = listDiscover;
        this.homeFragment = homeFragment;
    }


    @Override
    public int getCount() {
        return listDiscover.size();
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        Inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_discover, container, false);

        TextView emirate, name;
        ImageView coverImg;

        coverImg = view.findViewById(R.id.discover_img);
        name = view.findViewById(R.id.discover_title);
        emirate = view.findViewById(R.id.discover_emirate);

        //Get and set data
        DiscoverModel discoverModel = listDiscover.get(position);
        Glide.with(context).load(discoverModel.getCoverImg()).thumbnail(.25f).into(coverImg);
        name.setText(discoverModel.getName());
        emirate.setText(discoverModel.getEmirate());

        //Handle card click
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "CARD CLICKED", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("emirateName", discoverModel.getEmirate());
                bundle.putString("emirateId", discoverModel.getEmirateId());
                bundle.putString("placeId", discoverModel.getPlaceId());
                bundle.putString("placeName",discoverModel.getName());
                bundle.putString("placeImg", discoverModel.getCoverImg());

                Log.d("VIEW_PAGER, EMIRATE", discoverModel.getEmirateId() + "");
                Log.d("VIEW_PAGER, PLACE ID", discoverModel.getPlaceId() + "");

                // Open another fragment
                PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
                placeDetailsFragment.setArguments(bundle);
                NavHostFragment.findNavController(homeFragment).navigate(R.id.action_homeFragment_to_placeDetailsFragment,bundle);
            }
        });

        //Add view to container
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


}
