package com.example.jawahra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.jawahra.R;
import com.example.jawahra.models.DiscoverModel;

import java.util.List;

public class DiscoverAdapter extends PagerAdapter {
    private Context context;
    private List<DiscoverModel> listDiscoverModel;

    public DiscoverAdapter(Context context, List<DiscoverModel> listDiscoverModel) {
        this.context = context;
        this.listDiscoverModel = listDiscoverModel;
    }

    @Override
    public int getCount() {
        return listDiscoverModel.size();
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
         return view.equals(object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        destroyItem(container, position, object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_discover, container, false);

        ImageView ivDiscoverImg = view.findViewById(R.id.discover_img);
        TextView tvDiscoverTitle = view.findViewById(R.id.discover_title);
        TextView tvDiscoverEmirate = view.findViewById(R.id.discover_emirate);

        Glide.with(context).load(listDiscoverModel.get(position).getCoverImg()).thumbnail(.25f).into(ivDiscoverImg);
        tvDiscoverTitle.setText(listDiscoverModel.get(position).getName());
        tvDiscoverEmirate.setText(listDiscoverModel.get(position).getEmirate());

        view.setOnClickListener(v ->{
            Toast.makeText(context, ""+listDiscoverModel, Toast.LENGTH_SHORT).show();
        });

        container.addView(view);
        return view;
    }
}
