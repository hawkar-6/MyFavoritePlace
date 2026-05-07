package com.example.choose;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

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
        // لێرە فایلی item_place بانگ دەکەین کە پێشتر دروستت کردووە
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = places.get(position);

        holder.txtTitle.setText(place.getTitle());
        holder.txtCoords.setText("Lat: " + place.getLatitude() + "  Lng: " + place.getLongitude());

        // بارکردنی وێنە بە بەکارهێنانی Glide
        Glide.with(holder.itemView.getContext())
                .load(place.getImagePath())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imgThumb);

        // دوگمەی سڕینەوە
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(place));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtCoords;
        ImageView imgThumb;
        MaterialButton btnDelete;

        PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtCoords = itemView.findViewById(R.id.txtCoords);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}