package com.example.jawahra.ui.visit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawahra.R;
import com.example.jawahra.models.EmiratesModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VisitFragment extends Fragment {

//    private VisitViewModel visitViewModel;

    private RecyclerView listEmirates;
    private FirestoreRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_visit, container, false);

        /*visitViewModel =
                new ViewModelProvider(this).get(VisitViewModel.class);
        final TextView textView = root.findViewById(R.id.text_visit);
        visitViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        listEmirates = root.findViewById(R.id.list_emirates);

        //Query
        Query query = firebaseFirestore.collection("emirate");

        //RecyclerOptions
        FirestoreRecyclerOptions<EmiratesModel> options = new FirestoreRecyclerOptions.Builder<EmiratesModel>()
                .setQuery(query, EmiratesModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<EmiratesModel, EmiratesViewHolder>(options) {
            @NonNull
            @Override
            public EmiratesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_emirate, parent, false);
                return new EmiratesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EmiratesViewHolder holder, int position, @NonNull EmiratesModel model) {
                holder.listName.setText(model.getEmirateName());
            }
        };

        listEmirates.setHasFixedSize(true);
        listEmirates.setLayoutManager(new LinearLayoutManager(root.getContext()));
        listEmirates.setAdapter(adapter);

        return root;
    }

    private class EmiratesViewHolder extends RecyclerView.ViewHolder {

        private final TextView listName;
        public EmiratesViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.list_name);

        }

        public TextView getView(){
            return listName;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}