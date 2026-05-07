package com.example.choose;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.hawkar6.myfavoriteplaces.databinding.ActivityAddPlaceBinding;

/**
 * AddPlaceActivity:
 * - Enter title
 * - Pick image from gallery (READ_EXTERNAL_STORAGE)
 * - Pick location via MapActivity
 * - Save to SQLite
 */
public class AddPlaceActivity extends AppCompatActivity {

    private ActivityAddPlaceBinding binding;

    private Uri selectedImageUri = null;
    private double selectedLat = 0;
    private double selectedLng = 0;

    private static final int REQ_PICK_LOCATION = 2001;
    private static final int REQ_STORAGE_PERMISSION = 3001;

    // Activity Result API for picking gallery image
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    binding.imgPreview.setImageURI(selectedImageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding
        binding = ActivityAddPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Pick image
        binding.btnPickImage.setOnClickListener(v -> pickImageWithPermission());

        // Pick location
        binding.btnPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivityForResult(intent, REQ_PICK_LOCATION);
        });

        // Save
        binding.btnSave.setOnClickListener(v -> savePlace());
    }

    /**
     * Request permission if needed, then open gallery picker.
     */
    private void pickImageWithPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQ_STORAGE_PERMISSION
            );
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    /**
     * Validate and insert Place into SQLite.
     */
    private void savePlace() {
        String title = (binding.edtTitle.getText() != null)
                ? binding.edtTitle.getText().toString().trim()
                : "";

        if (title.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Please enter a title", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Snackbar.make(binding.getRoot(), "Please pick an image", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (selectedLat == 0 && selectedLng == 0) {
            Snackbar.make(binding.getRoot(), "Please pick a location", Snackbar.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);
        db.insertPlace(new Place(title, selectedImageUri.toString(), selectedLat, selectedLng));

        Snackbar.make(binding.getRoot(), "Place saved!", Snackbar.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Receive picked location from MapActivity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PICK_LOCATION && resultCode == RESULT_OK && data != null) {
            selectedLat = data.getDoubleExtra("lat", 0);
            selectedLng = data.getDoubleExtra("lng", 0);
            binding.txtLocation.setText("Lat: " + selectedLat + ", Lng: " + selectedLng);
        }
    }

    /**
     * Permission callback for READ_EXTERNAL_STORAGE.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageWithPermission();
            } else {
                Snackbar.make(binding.getRoot(), "Storage permission denied", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}