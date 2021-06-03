package com.example.jawahra;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

public class OnboardingScreen extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard explore = new AhoyOnboarderCard("DISCOVER", "Uncover and explore the hidden gems in the UAE.", R.drawable.ic_onb_explore_gems);
        AhoyOnboarderCard immerse = new AhoyOnboarderCard("INCLUSIVE", "Immerse yourselves with its vast and remarkable culture.", R.drawable.ic_onb_immerse_culture);
        AhoyOnboarderCard hands = new AhoyOnboarderCard("CONVENIENCE", "Have all of the information you need in the palm of your hands.", R.drawable.ic_onb_info_hands);

        explore.setBackgroundColor(R.color.white);
        immerse.setBackgroundColor(R.color.white);
        hands.setBackgroundColor(R.color.white);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(explore);
        pages.add(immerse);
        pages.add(hands);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.black);
            page.setDescriptionColor(R.color.black);
            page.setTitleTextSize(dpToPixels(10, this));
            page.setDescriptionTextSize(dpToPixels(6, this));
            page.setIconLayoutParams(800, 800, 10, 10, 10, 10);
        }

        setFinishButtonTitle("Welcome!");
        showNavigationControls(true);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.blue_dark);
        colorList.add(R.color.blue);
        colorList.add(R.color.blue_light);

        setColorBackground(colorList);

        //Set pager indicator colors
        setInactiveIndicatorColor(R.color.black);
        setActiveIndicatorColor(R.color.white);

//        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quicksand.ttf");
//        setFont(face);

        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(OnboardingScreen.this, MainAuth.class));
        finish();
    }
}