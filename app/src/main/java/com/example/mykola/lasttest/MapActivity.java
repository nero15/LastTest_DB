package com.example.mykola.lasttest;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;


import com.example.mykola.lasttest.OtherClass.DirectionsJSONParser;
import com.example.mykola.lasttest.OtherClass.DrawWay;
import com.example.mykola.lasttest.SQLiteDataBase.DBHelper;
import com.example.mykola.lasttest.SQLiteDataBase.DataBaseReview.DataProvider;
import com.example.mykola.lasttest.SQLiteDataBase.Table;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class MapActivity extends AppCompatActivity {
    GoogleMap googleMap;
    List<Marker> markers = new ArrayList<Marker>();
    Marker my_location;
    Boolean showObject;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        createMapView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //addToMapAll(false);
//        drawWay();
//        if (true) return;
        Uri data = getIntent().getData();
        if (data != null){
            Button btn1,btn2,btn3;
            btn1 = (Button) findViewById(R.id.id_show_on_map_objects);
            btn2 = (Button) findViewById(R.id.id_clear_map);
            btn3 = (Button) findViewById(R.id.id_show_near_object);
            active(btn1,false);
            active(btn2,false);
            active(btn3,false);
            showObject = true;
            String str;
            str = data.toString();
            Log.i("Map",str);
            Double longitude,latitude;
            String name = str.substring(0,str.indexOf(";"));
            str = str.replace(name + ";","");
            latitude = Double.parseDouble(str.substring(0, str.indexOf(";")));
            str = str.replace(latitude.toString() + ";", "");
            longitude = Double.parseDouble(str.substring(0, str.length() - 1));
            addMarker(latitude,longitude,name,BitmapDescriptorFactory.defaultMarker(),true);
            //zoomToPoint(new LatLng(latitude,longitude),10);
        }
        else{
            Button btn1,btn2,btn3;
            btn1 = (Button) findViewById(R.id.id_show_on_map_objects);
            btn2 = (Button) findViewById(R.id.id_clear_map);
            btn3 = (Button) findViewById(R.id.id_show_near_object);
            active(btn1,true);
            active(btn2,true);
            active(btn3,true);
            showObject = false;
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//Geo
        }
    }

    private void createMapView() {
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    /**
     * Geolacation
     **/

    private void addMyPosition(Location location) {
        if (null != googleMap && location != null ) {
            if (my_location != null) my_location.remove();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            my_location = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Me")
                            .draggable(false)
            );
            Save(location.getLongitude(),location.getLatitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        }
    }
    protected void zoomToPoint(LatLng position, Integer zoom){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }
    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            addMyPosition(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // код який виконується при виключенні провайдера (GPS або WiFI)
        }

        @Override
        public void onProviderEnabled(String provider) {
            // код який виконується при включенні провайдера (GPS або WiFI)

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            showLocation(locationManager.getLastKnownLocation(provider));

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (showObject) return;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 5, 5, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 5, 5, locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (showObject) return;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    private void showLocation(Location location) {
        if (location == null)
            return;

        if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            addMyPosition(location);
        } else if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            addMyPosition(location);
        }
    }

    /**
     * Geolacation
     **/
    private Marker addMarker(double x, double y, String name, BitmapDescriptor color, Boolean visible) {

        /** Make sure that the map has been initialised **/
        Marker marker = null;
        if (null != googleMap) {
            marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(x, y))
                            .title(name)
                            .draggable(true)
                            .icon(color)
                            .visible(visible)
            );
            if (!markers.contains(marker) && name != "Me") markers.add(marker);
        }
        return marker;
    }

    public void onClickShowObjectsNear(View v) {
        float[] result = new float[1];
        for (int i = 0; i < markers.size(); i++) {
            Location.distanceBetween(my_location.getPosition().latitude, my_location.getPosition().longitude,
                    markers.get(i).getPosition().latitude,
                    markers.get(i).getPosition().longitude,
                    result);
            if (result[0] < 1000)
                markers.get(i).setVisible(true);
            else
                markers.get(i).setVisible(false);
        }
    }

    public void onClickClearMap(View v) {
        googleMap.clear();
        markers.clear();
        load();
        my_location = addMarker(
                my_location.getPosition().latitude,
                my_location.getPosition().longitude,
                "Me",BitmapDescriptorFactory.defaultMarker(),true);
    }
    public void onClickShowAllObjects(View v) {
        addToMapAll(true);
    }
    protected void addToMapAll(boolean paint){
        DBHelper dbHelper=new DBHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String[] projections = new String[]
                {
                        Table.Object_standart.ID,
                        Table.Object_standart.NAME,
                        Table.Object_standart.LONGITUDE,
                        Table.Object_standart.LATITUDE};
        Cursor cursor = dbHelper.getData(sqLiteDatabase, Table.Object_standart.TABLE_NAME, projections);
        addObjectToMap(cursor, null, paint);
    }
    protected void zoomCamera(final Vector<LatLng> point) {

        final View mapView = getFragmentManager().findFragmentById(R.id.mapView).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (int i =0;i<point.size();i++)
                        builder.include(point.get(i));
                    LatLngBounds bound = builder.build();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound, 50));
                }
            }
            );
        }
    }

    protected void addObjectToMap(Cursor cursor, BitmapDescriptor color, boolean show){

        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    String name;
                    Double longitude, latitude;
                    name = cursor.getString(cursor.getColumnIndex(Table.Object_standart.NAME));
                    longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Table.Object_standart.LONGITUDE)));
                    latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Table.Object_standart.LATITUDE)));
                    addMarker(longitude, latitude, name,
                            color == null ? BitmapDescriptorFactory.defaultMarker(): color,show);
                } while (cursor.moveToNext());
            }
        cursor.close();
    }

    public void Save(Double longitude, Double latitude){
        SharedPreferences sharedPreferences;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(Table.Object_standart.LONGITUDE, longitude.toString());
        ed.putString(Table.Object_standart.LATITUDE, latitude.toString());
        ed.commit();
        //Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }
    public void load(){
    SharedPreferences sharedPreferences;
    sharedPreferences = getPreferences(MODE_PRIVATE);
        my_location.setPosition(new LatLng(
                Double.parseDouble(sharedPreferences.getString(Table.Object_standart.LONGITUDE, "")),
                Double.parseDouble(sharedPreferences.getString(Table.Object_standart.LATITUDE, ""))));
    //Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
}
    private void active(Button button, boolean state){
        button.setActivated(state);
        button.setAlpha(state ? 1f : 0f);
        button.setClickable(state);
    }

    public void drawWay(){

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Map Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.mykola.lasttest/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Map Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.mykola.lasttest/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
