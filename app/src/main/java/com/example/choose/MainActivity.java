package com.example.choose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.choose.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.example.choose.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity:
 * - Displays saved places from SQLite in a RecyclerView
 * - Opens AddPlaceActivity from FAB
 */
public class MainActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceDeleteListener {

    private ActivityMainBinding binding;

    private DatabaseHelper db;
    private PlaceAdapter adapter;
    private final List<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // DB
        db = new DatabaseHelper(this);

        // RecyclerView
        adapter = new PlaceAdapter(places, this);
        binding.recyclerPlaces.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerPlaces.setAdapter(adapter);

        // FAB -> AddPlaceActivity
        binding.fabAddPlace.setOnClickListener(v ->
                startActivity(new Intent(this, AddPlaceActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPlaces();
    }

    private void refreshPlaces() {
        places.clear();
        places.addAll(db.getAllPlaces());
        adapter.notifyDataSetChanged();

        binding.txtEmpty.setVisibility(places.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDelete(Place place) {
        db.deletePlace(place.getId());
        Snackbar.make(binding.getRoot(), "Deleted: " + place.getTitle(), Snackbar.LENGTH_SHORT).show();
        refreshPlaces();
    }
}