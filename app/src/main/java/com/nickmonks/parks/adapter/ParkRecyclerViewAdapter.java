package com.nickmonks.parks.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nickmonks.parks.R;
import com.nickmonks.parks.model.Park;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ParkRecyclerViewAdapter extends RecyclerView.Adapter<ParkRecyclerViewAdapter.ViewHolder> {
    // List of
    private final List<Park> parkList;
    private final OnParkClickListener parkClickListener;

    public ParkRecyclerViewAdapter(List<Park> parkList, OnParkClickListener parkClickListener) {
        this.parkList = parkList;
        this.parkClickListener = parkClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // first we need to inflate our view:
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.park_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // where we bind data and the views with the position pointer
        Park park = parkList.get(position);

        holder.parkName.setText(park.getName());
        holder.parkType.setText(park.getDesignation());
        holder.parkState.setText(park.getStates());

        // Image - Use Picasso Library
        if (park.getImages().size() > 0){
            Picasso.get().load(park.getImages().get(0).getUrl())
                    .placeholder(android.R.drawable.stat_sys_download)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(100,100)
                    .centerCrop()
                    .into(holder.parkImage);
        }

    }

    @Override
    public int getItemCount() {
        return parkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Class where we manipulate our widgets
        public ImageView parkImage;
        public TextView parkName;
        public TextView parkType;
        public TextView parkState;
        OnParkClickListener onParkClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parkImage = itemView.findViewById(R.id.row_park_imageview);
            parkName = itemView.findViewById(R.id.row_park_name);
            parkType = itemView.findViewById(R.id.row_parktype_textview);
            parkState = itemView.findViewById(R.id.row_park_state_textview);
            this.onParkClickListener = parkClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Park currPark = parkList.get(getAdapterPosition()); // give us the adapter position we're pointing to
            onParkClickListener.onParkClicked(currPark);
        }
    }
}
