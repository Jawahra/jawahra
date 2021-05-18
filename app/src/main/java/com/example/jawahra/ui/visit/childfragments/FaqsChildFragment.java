package com.example.jawahra.ui.visit.childfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.jawahra.R;
import com.example.jawahra.models.FaqsModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class FaqsChildFragment extends Fragment {

    private CollectionReference faqsRef;

    private TextView faqsPrices, faqsAttire, faqsAvailability, faqsActivities, faqsWebsite;
    private String string_website, string_attire, string_prices, string_activities, string_availability;
    private List<String> array_activities, array_prices, array_availability;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_faqs_child, container, false);

        faqsWebsite = root.findViewById(R.id.faqs_website);

        faqsActivities = root.findViewById(R.id.faqs_activities);
        faqsAttire = root.findViewById(R.id.faqs_attire);
        faqsAvailability = root.findViewById(R.id.faqs_availability);
        faqsPrices = root.findViewById(R.id.faqs_prices);

        faqsRef = PlaceDetailsFragment.placeRef.collection("faqs");

        faqsRef.get()
                .addOnSuccessListener(snapshot -> {

                    for (QueryDocumentSnapshot snapshots : snapshot) {
                        FaqsModel faqsModel = snapshots.toObject(FaqsModel.class);
                        string_attire = faqsModel.getAttire();
                        string_website = faqsModel.getWebsite();
                        array_activities = faqsModel.getActivities();
                        array_availability = faqsModel.getAvailability();
                        array_prices = faqsModel.getPrices();

                        Log.d("faqs", "ATTIRE: " + string_attire);
                        Log.d("faqs", "WEBSITE: " + string_website);
                        Log.d("faqs", "ARRAY_PRICES" + array_prices);
                        Log.d("faqs", "ARRAY SIZE: " + array_prices.size());

                        if(array_prices != null){
                            for (int i = 0; i < array_prices.size(); i++) {
                                if (i == 0){
                                    string_prices = array_prices.get(i);
                                }else {
                                    string_prices += array_prices.get(i);
                                }
                                string_prices += "\n";
                            }
                            Log.d("faqs", "STRING_PRICES: " + string_prices);
                        }

                        if (array_availability != null){
                            for (int i = 0; i < array_availability.size(); i++){
                                if (i == 0){
                                    string_availability = array_availability.get(i);
                                }else{
                                    string_availability += array_availability.get(i);
                                }
                                string_availability += "\n";
                            }
                            Log.d("faqs", "STRING_AVAILABILITY: " + string_availability);
                        }

                        if (array_activities != null) {
                            for (int i = 0; i < array_activities.size(); i++) {
                                if (i == 0) {
                                    string_activities = "\u2022 " + array_activities.get(i);
                                }else{
                                    string_activities += "\u2022 " + array_activities.get(i);
                                }
                                string_activities += "\n";
                            }
                            Log.d("faqs", "STRING_ACTIVITIES: " + string_activities);
                        }

                        faqsAttire.setText(string_attire);
                        faqsPrices.setText(string_prices);
                        faqsAvailability.setText(string_availability);
                        faqsActivities.setText(string_activities);
                        faqsWebsite.setText(string_website);
                    }
                })
                .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist"));

        return root;
    }
}
