package edu.asu.msse.rsingh92.assignment1.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.airbnb.lottie.L;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.msse.rsingh92.assignment1.database.PlaceDataBase;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;

public class DBUtility {

    private static PlaceDataBase placeDataBase;
    private static SQLiteDatabase sqLiteDatabase;
    private static Context context;
    private static List<PlaceDescription> nonSyncedPlaces;

    public static void initDatabase(Context _context){

        Log.d("AAA", "inside cons");

        placeDataBase = new PlaceDataBase(_context);
        context = _context;
        try {
            sqLiteDatabase = placeDataBase.openDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<PlaceDescription> getAllPlacesFromDB(){

        Log.d("AAA", "inside");

        List<PlaceDescription> allPlaces = new ArrayList<>();

        String select_all_query = "SELECT * FROM place";
        Cursor c= sqLiteDatabase.rawQuery(select_all_query, null);
        c.moveToFirst();

        int count = c.getCount();

        Log.d("AAA", count+"");

        for(int i=0;i<count;i++){

            PlaceDescription place = new PlaceDescription();

            place.setName(c.getString(c.getColumnIndex("name")));
            place.setDescription(c.getString(c.getColumnIndex("description")));
            place.setCategory(c.getString(c.getColumnIndex("category")));
            place.setAddressStreet(c.getString(c.getColumnIndex("address_street")));
            place.setAddressTitle(c.getString(c.getColumnIndex("address_title")));
            place.setLongitude((c.getDouble(c.getColumnIndex("longitude"))));
            place.setLatitude((c.getDouble(c.getColumnIndex("latitude"))));
            place.setElevation(c.getString(c.getColumnIndex("elevation")));

            Log.d("AAA", ""+i+": "+place.getName());

            allPlaces.add(place);

            c.moveToNext();
        }

        return allPlaces;

    }

    public static void addAllPlacesToDatabase(List<PlaceDescription> places){
        for(PlaceDescription place:places){
            addPlaceToDatabase(place);
        }
    }

    public static void addPlaceToDatabase(PlaceDescription place){

        String name = place.getName();
        String description = place.getDescription();
        String category = place.getCategory();
        String addresstitle = place.getAddressTitle();
        String addressstreet = place.getAddressStreet();
        Double elevation = Double.parseDouble(place.getElevation());
        Double latitude = place.getLatitude();
        Double longitude = place.getLongitude();

//        String insertQuery = "INSERT INTO place VALUES("+name+","+description+","+category+","+addresstitle+","+addressstreet+","+elevation+","+latitude+","+longitude+");";
//        sqLiteDatabase.execSQL(insertQuery);


        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("category",category);
        contentValues.put("address_title",addresstitle);
        contentValues.put("address_street",addressstreet);
        contentValues.put("elevation",elevation);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);

        long rownum = sqLiteDatabase.insert("place",null,contentValues);
        Log.d("AAA", ""+rownum);


    }

    public static int deleteAllPlacesOnDatabase(){
        return sqLiteDatabase.delete("place", "1", null);
    }

    /**
     *   Methods which deal with places that could not be pused to Server
     */
    public static List<PlaceDescription> getAllNonSyncedPlaces(){
        return nonSyncedPlaces;
    }

    public static void addInNonSynedPlaces(PlaceDescription nonSyncPlace){
        nonSyncedPlaces.add(nonSyncPlace);
    }

    public static void resetNonSyncPlace(){
        nonSyncedPlaces = new ArrayList<PlaceDescription>();
    }

}