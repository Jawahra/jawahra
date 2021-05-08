package com.example.jawahra.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jawahra.R;
import com.example.jawahra.models.EmiratesModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class EmiratesAdapter extends FirestoreRecyclerAdapter<EmiratesModel, EmiratesAdapter.EmiratesViewHolder> {

    private Context context;
    public String documentId;
    private OnListItemClick onListItemClick;

    public EmiratesAdapter(@NonNull FirestoreRecyclerOptions<EmiratesModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull EmiratesViewHolder holder, int position, @NonNull EmiratesModel model) {
        holder.listName.setText(model.getEmirateName());
        Glide.with(context).load(model.getCoverImg()).into(holder.cardImage);
    }

    @NonNull
    @Override
    public EmiratesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_emirate, parent, false);
        return new EmiratesViewHolder(view);
    }

    public class EmiratesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView listName;
        public ImageView cardImage;

        public EmiratesViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_emirate_name);
            cardImage = itemView.findViewById(R.id.list_emirate_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //get document ID of selected recyclerview card
            onListItemClick.OnItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getId(),getSnapshots().getSnapshot(getAdapterPosition()).getString("emirateName"));
        }

    }

    public interface OnListItemClick {
        void OnItemClick(String myDocumentId, String emirateName);
    }
}


           /* listName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlacesFragment emiratesFragment = new PlacesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_visit,emiratesFragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

              }*/