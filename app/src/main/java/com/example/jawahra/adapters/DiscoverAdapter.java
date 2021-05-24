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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.jawahra.R;
import com.example.jawahra.models.DiscoverModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends PagerAdapter {
    private Context context;
    private List<DiscoverModel> listDiscover;

    public DiscoverAdapter(Context context, List<DiscoverModel> listDiscoverModel) {
        this.context = context;
        this.listDiscover = listDiscoverModel;
    }

    @Override
    public int getCount() {
        return listDiscover.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
         return view.equals(object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_discover, container, false);

        ImageView ivDiscoverImg = view.findViewById(R.id.discover_img);
        TextView tvDiscoverTitle = view.findViewById(R.id.discover_title);
        TextView tvDiscoverEmirate = view.findViewById(R.id.discover_emirate);

        Glide.with(context).load(listDiscover.get(position).getCoverImg()).thumbnail(.25f).into(ivDiscoverImg);
        tvDiscoverTitle.setText(listDiscover.get(position).getName());
        tvDiscoverEmirate.setText(listDiscover.get(position).getEmirate());

        view.setOnClickListener(v ->{
            Bundle bundle = new Bundle();
          bundle.putString("emirateId", listDiscover.get(position).getEmirateId());
          bundle.putString("placeId", listDiscover.get(position).getPlaceId());
          bundle.putString("placeName",listDiscover.get(position).getName());
          bundle.putString("placeImg", listDiscover.get(position).getCoverImg());

            Log.d("VIEW_PAGER, EMIRATE", listDiscover.get(position).getEmirateId());
            Log.d("VIEW_PAGER, PLACE ID", listDiscover.get(position).getEmirateId());

            // Open another fragment
            PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
            placeDetailsFragment.setArguments(bundle);
            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_home,placeDetailsFragment,null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();



        });

        container.addView(view);
        return view;
    }
}
