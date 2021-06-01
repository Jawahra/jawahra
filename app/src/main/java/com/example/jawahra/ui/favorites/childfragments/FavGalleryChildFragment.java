package com.example.jawahra.ui.favorites.childfragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.adapters.Favorite;
import com.example.jawahra.adapters.FavoriteViewModel;
import com.example.jawahra.adapters.GalleryAdapter;
import com.example.jawahra.adapters.GalleryUrl;
import com.example.jawahra.ui.favorites.FavoriteDetailsFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FavGalleryChildFragment extends Fragment {

    public StorageReference mediaFile;
    public String ref;
    private RecyclerView galleryRecycler;
    private TextView layoutDisconnected;

    private int position;
    private List<Favorite> currentFavorite = new ArrayList<>();
    private FavoriteViewModel favoriteViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery_child, container, false);
        layoutDisconnected = root.findViewById(R.id.layout_gallery_disconnected);
        galleryRecycler = root.findViewById(R.id.gallery_recycler);

        galleryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        position = FavoriteDetailsFragment.position;
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        favoriteViewModel.getCurrentFavorite(position).observe(getViewLifecycleOwner(), favorites -> {
            //update RecyclerView
            currentFavorite = favorites;
        });

        new Handler().postDelayed(this::checkInternet,1500);

        return root;
    }

    private void checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected){
            galleryRecycler.setVisibility(View.VISIBLE);
            layoutDisconnected.setVisibility(View.INVISIBLE);
            ref = currentFavorite.get(0).getMediaReference();
            Log.d("FIREBASE_QUERY", "checkInternet: red: " + ref);
            showGallery();
            Log.d("CHECK_WIFI", "checkInternet: CONNECTED");
        }else{
            layoutDisconnected.setVisibility(View.VISIBLE);
            galleryRecycler.setVisibility(View.INVISIBLE);
            Log.d("CHECK_WIFI", "checkInternet: DISCONNECTED");
        }
    }

    private void showGallery(){

        FirebaseStorage storage = FirebaseStorage.getInstance();
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
                            }).addOnFailureListener(e -> Log.d("FIREBASE_QUERY", "GalleryChildFragment: Document does not Exist" ));
                        }
                    }).addOnFailureListener(e -> Log.d("FIREBASE_QUERY", "GalleryChildFragment: Document does not Exist" ));
        }else{
            Toast.makeText(getContext(), "Unable to fetch images", Toast.LENGTH_SHORT).show();
        }
    }
}
