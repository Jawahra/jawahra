package com.example.jawahra.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jawahra.R;
import com.example.jawahra.ui.news.NewsFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UEDetailsFragment extends Fragment {

    String upcomingEventID, eventEmirate, eventName, eventImg, eventLocation, eventDetails;
    Date eventFromDate, eventToDate;
    TextView tvEventEmirate, tvEventDetails, tvEventFromDate,tvEventToDate, tvEventLocation;
    LinearLayout llEventImg;
    Button btnMarkCalendar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get bundle values
        Bundle bundle = getArguments();
        if (bundle != null){
            upcomingEventID = bundle.getString("upcomingEventID", upcomingEventID);
            eventEmirate = bundle.getString("eventEmirate", eventEmirate);
            eventName = bundle.getString("eventName", eventName);
            eventImg = bundle.getString("eventImg", eventImg);
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollapsingToolbarLayout collapsingToolbar = requireView().findViewById(R.id.ue_details_collapsing_toolbar);
        llEventImg = requireView().findViewById(R.id.ue_details_img_layout);
        tvEventEmirate = requireView().findViewById(R.id.ue_details_emirate);
        tvEventDetails = requireView().findViewById(R.id.ue_details_content);
        tvEventFromDate = requireView().findViewById(R.id.ue_details_from_date);
        tvEventToDate = requireView().findViewById(R.id.ue_details_to_date);
        tvEventLocation = requireView().findViewById(R.id.ue_details_location);

        btnMarkCalendar = requireView().findViewById(R.id.ue_details_btn);





//        Bring to previous fragment when back button is pressed
        Toolbar toolbar = requireView().findViewById(R.id.ue_details_toolbar);
        toolbar.setNavigationOnClickListener(view1 -> {
            NewsFragment newsFragment = new NewsFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_ue_details, newsFragment);
            fragmentTransaction.show(newsFragment);
            fragmentTransaction.commit();

//            Make Appbar disappear when going to previous fragment
            AppBarLayout appBarLayout = requireView().findViewById(R.id.ue_details_app_bar);
            appBarLayout.setVisibility(View.GONE);

        });



//        Set title font
        final Typeface fontPlayFairBold = ResourcesCompat.getFont(requireContext(), R.font.playfair_display_bold);
        collapsingToolbar.setCollapsedTitleTypeface(fontPlayFairBold);
        collapsingToolbar.setExpandedTitleTypeface(fontPlayFairBold);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);


//        Set Data
        collapsingToolbar.setTitle(eventName);
        tvEventEmirate.setText(eventEmirate);

        //                Get url string of image from document in Firestore and set Linear layout's background to that image
        Glide.with(requireActivity())
                .load(eventImg)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        llEventImg.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


//        Open calendar when button is pressed, values from database are passed into the calendar to fill the fields
        btnMarkCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);

            intent.putExtra(CalendarContract.Events.TITLE, eventName);
//            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation);
            intent.putExtra(CalendarContract.Events.DESCRIPTION, eventDetails);
            intent.putExtra(CalendarContract.Events.ALL_DAY, false);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventFromDate.getTime());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventFromDate.getTime());

            Log.d("DATE", eventFromDate.toString());


            if (intent.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(intent);
            } else{
                Toast.makeText(getActivity(), "There is no app that can support this action", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ue_details, container, false);


        DocumentReference docRef = FirebaseFirestore.getInstance().collection("upcoming_events").document(upcomingEventID);
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    eventDetails = doc.getString("eventDetails");
                    tvEventDetails.setText(eventDetails);

                    eventFromDate = doc.getDate("eventFromDate");
                    tvEventFromDate.setText("From: " + convertDateToString(eventFromDate));

                    eventToDate = doc.getDate("eventToDate");
                    tvEventToDate.setText("To: " + convertDateToString(eventToDate));

                    eventLocation = doc.getString("eventLocation");
                    tvEventLocation.setText("Location: " + eventLocation);
                } else{
                    Log.d("Document", "No data");
                }
            }
        });

        return view;
    }

    //    Function to convert date to string
    private String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        return dateFormat.format(date);
    }


}