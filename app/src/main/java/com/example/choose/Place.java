package com.example.choose;

/**
 * Place model stored in SQLite.
 * imagePath is stored as String (usually a content:// Uri string from gallery).
 */
public class Place {

    private long id;
    private String title;
    private String imagePath;
    private double latitude;
    private double longitude;

    public Place(long id, String title, String imagePath, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place(String title, String imagePath, double latitude, double longitude) {
        this(-1, title, imagePath, latitude, longitude);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}