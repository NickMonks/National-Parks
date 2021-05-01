package com.nickmonks.parks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nickmonks.parks.R;
import com.nickmonks.parks.model.Images;
import com.squareup.picasso.Picasso;

import java.util.List;

// the mecanism is the same as the recycler view, therefore we will inherit from the reycler view class.
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ImageSlider> {

    private List<Images> imagesList;

    public ViewPagerAdapter(List<Images> imagesList) {
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ImageSlider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_pager_row, parent, false);



        return new ImageSlider(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSlider holder, int position) {
        // get images and put them in the user interface
        // we use picasso to fetch the image from the url
        Picasso.get()
                .load(imagesList.get(position).getUrl())
                .fit()
                .placeholder(android.R.drawable.stat_notify_error)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ImageSlider extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ImageSlider(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.view_pager_imageview);

        }
    }
}
