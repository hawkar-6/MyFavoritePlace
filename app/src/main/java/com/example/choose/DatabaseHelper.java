package com.example.choose;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper: SQLite CRUD for a table named "places".
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database config
    private static final String DB_NAME = "my_favorite_places.db";
    private static final int DB_VERSION = 1;

    // Table + columns
    public static final String TABLE_PLACES = "places";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_IMAGE_PATH = "imagePath";
    public static final String COL_LAT = "latitude";
    public static final String COL_LNG = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Create places table.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE " + TABLE_PLACES + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_TITLE + " TEXT NOT NULL, " +
                        COL_IMAGE_PATH + " TEXT, " +
                        COL_LAT + " REAL, " +
                        COL_LNG + " REAL" +
                        ")";
        db.execSQL(sql);
    }

    /**
     * Upgrade strategy: drop and recreate (OK for a student project).
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }

    // ---------------- CRUD ----------------

    /**
     * CREATE
     */
    public long insertPlace(Place place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, place.getTitle());
        values.put(COL_IMAGE_PATH, place.getImagePath());
        values.put(COL_LAT, place.getLatitude());
        values.put(COL_LNG, place.getLongitude());
        return db.insert(TABLE_PLACES, null, values);
    }

    /**
     * READ (all, newest first)
     */
    public List<Place> getAllPlaces() {
        List<Place> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PLACES,
                null,
                null,
                null,
                null,
                null,
                COL_ID + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_PATH));
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LAT));
                double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LNG));

                list.add(new Place(id, title, imagePath, lat, lng));
            }
            cursor.close();
        }
        return list;
    }

    /**
     * DELETE (by id)
     */
    public int deletePlace(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PLACES, COL_ID + "=?", new String[]{String.valueOf(id)});
    }
}