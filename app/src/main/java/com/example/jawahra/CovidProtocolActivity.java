package com.example.jawahra;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.jawahra.adapters.CovidAdapter;
import com.example.jawahra.models.CovidModel;

import java.util.ArrayList;

public class CovidProtocolActivity extends AppCompatActivity {
    /*Variables*/
    private ViewPager covProtocols;
    private CovidAdapter covidAdapter;
    private ArrayList<CovidModel> covidModelArrayList;
    Button understandBtn;

    /*For Animated Gradient Background*/
    LinearLayout linearLayout;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);

        /*Opts the activity to be set to fullscreen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*Init Variables*/
        covProtocols = findViewById(R.id.protocols_covid);
        linearLayout = findViewById(R.id.covGradLayout);
        understandBtn = findViewById(R.id.covProtoBtn);

        /*Animate Gradient Background*/

       /*
        animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        */

        /*Button*/
        understandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CovidProtocolActivity.this, MainActivity.class));
            }
        });

        loadCards();
    }

    private void loadCards() {
        covidModelArrayList = new ArrayList<>();

        covidModelArrayList.add(new CovidModel(R.drawable.covid_distance, "Social Distancing", "Keep physical distance & avoid close contact with anyone showing signs of respiratory illness."));
        covidModelArrayList.add(new CovidModel(R.drawable.covid_hands, "Clean Hands", "Wash hands with soap and water for at least 20 secs. Rub palms, fingers and thumbs. Alternatively, use an alcohol-based hand sanitizer."));
        covidModelArrayList.add(new CovidModel(R.drawable.covid_healthy, "Stay Healthy", "Boost your immunity with healthy food, physical activity, plenty of water and enough sleep."));
        covidModelArrayList.add(new CovidModel(R.drawable.covid_masks, "Protect Yourself", "If required, wear masks correctly and gloves to avoid direct contact with surfaces."));
        covidModelArrayList.add(new CovidModel(R.drawable.covid_coughs, "Coughs and Sneezes", "Cover coughs and sneezes with a tissue, dispose properly, or cough into your elbow."));
        covidModelArrayList.add(new CovidModel(R.drawable.covid_home, "Stay Home", "Stay at home and follow the rules and advice of the authorities."));

        covidAdapter = new CovidAdapter(covidModelArrayList, this);

        covProtocols.setAdapter(covidAdapter);
        covProtocols.setPadding(100, 0, 100, 0);
    }
}
