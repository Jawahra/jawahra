package com.example.jawahra.ui.visit.childfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.GalleryAdapter;
import com.example.jawahra.adapters.GalleryUrl;
import com.example.jawahra.models.PlaceDetailsModel;
import com.example.jawahra.ui.visit.PlaceDetailsFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryChildFragment extends Fragment {

    public CollectionReference mediaRef;
    public StorageReference mediaFile;
    public String ref;
    private RecyclerView galleryRecycler;
    /*private ScrollGalleryView galleryView;
    private List<String> mediaString;
    String media;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_gallery_child, container, false);

        galleryRecycler = root.findViewById(R.id.gallery_recycler);
//        galleryRecycler.setHasFixedSize(true);
        galleryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        mediaRef = PlaceDetailsFragment.placeRef.collection("details");
        mediaRef.get()
                .addOnSuccessListener(snapshot -> {

                    for (QueryDocumentSnapshot snapshots : snapshot){
                        PlaceDetailsModel placeDetailsModel = snapshots.toObject(PlaceDetailsModel.class);
                        ref = placeDetailsModel.getMedia();

                        Log.d("MYGALLERY", "onCreateView: ref " + ref);

                        if (ref != null){
                            mediaFile = storage.getReference().child(ref);
                            mediaFile.listAll()
                                    .addOnSuccessListener(listResult -> {

                                        ArrayList<GalleryUrl> mediaUrls = new ArrayList<>();
                                        for (StorageReference item : listResult.getItems()){
                                            String path = item.getName();
                                            GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(),mediaUrls);
                                            mediaFile.child(path).getDownloadUrl().addOnSuccessListener(uri -> {
                                                GalleryUrl galleryUrl = new GalleryUrl();
                                                galleryUrl.setGalleryUrl(String.valueOf(uri));
                                                mediaUrls.add(galleryUrl);
                                                galleryRecycler.setAdapter(galleryAdapter);
                                            }).addOnFailureListener(e -> Log.d("MYGALLERY", "Document does not Exist" ));
                                        }
                                    }).addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist" ));
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d("CHECK_ID", "Document does not Exist" ));
        return root;
    }
}