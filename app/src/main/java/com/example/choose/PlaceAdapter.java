package com.example.choose;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hawkar6.myfavoriteplaces.databinding.ItemPlaceBinding;

import java.util.List;

/**
 * RecyclerView Adapter using View Binding.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    public interface OnPlaceDeleteListener {
        void onDelete(Place place);
    }

    private final List<Place> places;
    private final OnPlaceDeleteListener deleteListener;

    public PlaceAdapter(List<Place> places, OnPlaceDeleteListener deleteListener) {
        this.places = places;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaceBinding binding = ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PlaceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = places.get(position);

        // Bind title + coordinates
        holder.binding.txtTitle.setText(place.getTitle());
        holder.binding.txtCoords.setText("Lat: " + place.getLatitude() + "  Lng: " + place.getLongitude());

        // Load image (supports content:// uris)
        Glide.with(holder.binding.imgThumb.getContext())
                .load(place.getImagePath())
                .centerCrop()
                .into(holder.binding.imgThumb);

        // Delete action
        holder.binding.btnDelete.setOnClickListener(v -> deleteListener.onDelete(place));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        final ItemPlaceBinding binding;

        PlaceViewHolder(ItemPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}