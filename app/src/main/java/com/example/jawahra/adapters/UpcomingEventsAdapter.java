package com.example.jawahra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jawahra.R;
import com.example.jawahra.models.UpcomingEventsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpcomingEventsAdapter extends FirestoreRecyclerAdapter<UpcomingEventsModel, UpcomingEventsAdapter.UpcomingEventsViewHolder> {

    private Context context;
    private onCardsClickUE onCardsClickUE;

    public UpcomingEventsAdapter(@NonNull FirestoreRecyclerOptions<UpcomingEventsModel> options, onCardsClickUE onCardClick, Context context) {
        super(options);
        this.onCardsClickUE = onCardClick;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UpcomingEventsViewHolder holder, int position, @NonNull UpcomingEventsModel model) {
        holder.eventEmirate.setText(model.getEventEmirate());
        holder.eventName.setText(model.getEventName());

//                Set text of date TextView only when Textview is not null
        if (model.getEventDate() != null){
            String strEventDate = convertDateToString(model.getEventDate());
            holder.eventDate.setText(strEventDate);
        }

//                Get url string of image from document in Firestore and set ImageView to that image
        Glide.with(context).load(model.getEventImg()).into(holder.eventImg);

    }

    @NonNull
    @Override
    public UpcomingEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_upcoming_event, parent, false);
        return new UpcomingEventsViewHolder(view);
    }

    class UpcomingEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView eventEmirate, eventName, eventDate;
        private final ImageView eventImg;

        public UpcomingEventsViewHolder(@NonNull View itemView) {
            super(itemView);

            eventEmirate = itemView.findViewById(R.id.upcoming_event_emirate);
            eventName = itemView.findViewById(R.id.upcoming_event_name);
            eventDate = itemView.findViewById(R.id.upcoming_event_date);
            eventImg = itemView.findViewById(R.id.upcoming_event_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCardsClickUE.onCardClickUE(getSnapshots().getSnapshot(getAdapterPosition()).getId(), getStringFromSS("eventEmirate"), getStringFromSS("eventName"), getStringFromSS("eventImg"));
        }


        String getStringFromSS(String string){
            return getSnapshots().getSnapshot(getAdapterPosition()).getString(string);

        }
    }

    public interface onCardsClickUE{
        void onCardClickUE(String upcomingEventID, String eventEmirate, String eventName, String eventImg);
    }

    //    Function to convert date to string
    private String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy | hh:mm aa");
        return dateFormat.format(date);
    }
}
