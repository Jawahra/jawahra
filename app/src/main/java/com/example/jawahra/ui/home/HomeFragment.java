package com.example.jawahra.ui.home;

import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.jawahra.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ImageSlider imageSlider;
    List<SlideModel> imageList;
    String fbUrl, fbTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageSlider = requireView().findViewById(R.id.home_feature_banner);

        getFeaturedPlaces();
    }

    private void getFeaturedPlaces() {
        imageList = new ArrayList<>();
        List<String> fbEmirates = new ArrayList<>();
//        fbEmirates.add("abu_dhabi");
        fbEmirates.add("ajman");
        fbEmirates.add("dubai");
        fbEmirates.add("fujairah");
        fbEmirates.add("ras_al_khaimah");
        fbEmirates.add("sharjah");
        fbEmirates.add("umm_al_quwain");

        // Iterate through array list containing emirate names to retrieve data from firestore
        for (int i = 0; i < fbEmirates.size(); i++){
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("home")
                    .document("featured_banner")
                    .collection("featured_places")
                    .document(fbEmirates.get(i));
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        fbUrl = doc.getString("coverImg");
                        fbTitle = doc.getString("name");
                        imageList.add(new SlideModel(fbUrl,fbTitle, ScaleTypes.CENTER_CROP));
                        imageSlider.setImageList(imageList);


                    } else{
                        Log.d("Document", "No data");
                    }
                }
            });
        }

    }
}