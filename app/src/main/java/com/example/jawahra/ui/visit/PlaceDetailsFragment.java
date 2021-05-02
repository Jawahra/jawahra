package com.example.jawahra.ui.visit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jawahra.R;
import com.example.jawahra.models.PlaceDetailsModel;
import com.example.jawahra.models.PlacesModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.Map;

public class PlaceDetailsFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;

    private TextView placeName, placeDesc, placeLocation;

    public String emirateId, placeId, placeTitle, detailsId, imagesId;

    private DocumentReference placeRef;
    private CollectionReference detailsRef;
    private DocumentReference imagesRef;
    private DocumentReference faqsRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get bundle values
        Bundle bundle = getArguments();
        if(bundle != null){
            emirateId = bundle.getString("emirateId");
            placeId = bundle.getString("placeId");
            placeTitle = bundle.getString("placeName");

            Log.d("CHECK_ID", "bundle, emirates id  part2 " + bundle.getString("emirateId"));
            Log.d("CHECK_ID", "bundle, place id  part2 " + bundle.getString("placeId"));
        }

        //QUERY
        firebaseFirestore = FirebaseFirestore.getInstance();
        placeRef = firebaseFirestore.collection("emirates")
                .document(emirateId)
                .collection("places")
                .document(placeId);

        Log.d("CHECK_ID", "onCreate: placeRef : " + placeRef);

        detailsRef = placeRef.collection("details");

       /* placeRef.get()
                .addOnSuccessListener(snapshot -> {
                    Log.d("CHECK_ID", "Document DOES Exist" );

                    if(!snapshot.exists()){
                        detailsRef  = placeRef.collection("details");

                        Log.d("CHECK_ID","details id: " + detailsRef.getId());

                    }else{
                        Log.d("CHECK_ID", "Document does not Exist" );
                    }
                })
                .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist" ));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_place_details, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        //set views
        placeName = root.findViewById(R.id.place_name);
        placeDesc = root.findViewById(R.id.place_desc);
        placeLocation = root.findViewById(R.id.place_location);

        GetValues();

        return root;
    }

    public void GetValues(){
        detailsRef.get()
                .addOnSuccessListener(snapshot -> {

                    for (QueryDocumentSnapshot snapshots : snapshot){
                        Log.d("CHECK_ID", "onCreate: DETAILSREF.GET IS WORKING???????");

                        PlaceDetailsModel placeDetailsModel = snapshots.toObject(PlaceDetailsModel.class);
                        /*String desc = snapshots.getString("desc");
                        String map = snapshots.getString("map");*/

                        String desc = placeDetailsModel.getDesc();
                        String map = placeDetailsModel.getMap();

                        Log.d("CHECK_ID", "onCreate: DESC VALUE: " +desc);
                        Log.d("CHECK_ID", "onCreate: MAP VALUE: " +map);

                        placeName.setText(placeTitle);
                        placeDesc.setText(desc);
                        placeLocation.setText(map);
                    }
                })
                .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist" ));

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}