package com.example.jawahra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.jawahra.R;
import com.example.jawahra.models.CovidModel;

import java.util.ArrayList;

public class CovidAdapter extends PagerAdapter {
    private ArrayList<CovidModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public CovidAdapter(ArrayList<CovidModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.protocols_cards, container, false);

        ImageView covImg = view.findViewById(R.id.covidImage);
        TextView covTitle = view.findViewById(R.id.covidTitle);
        TextView covDesc = view.findViewById(R.id.covidDescription);

        covImg.setImageResource(models.get(position).getImage());
        covTitle.setText(models.get(position).getTitle());
        covDesc.setText(models.get(position).getDescription());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
