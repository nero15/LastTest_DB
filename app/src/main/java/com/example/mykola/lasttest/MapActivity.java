package com.example.mykola.lasttest;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.mykola.lasttest.SQLiteDataBase.AddDBActivity;
import com.example.mykola.lasttest.SQLiteDataBase.DBHelper;
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
import java.util.Locale;
import java.util.Vector;

public class MapActivity extends AppCompatActivity {
    GoogleMap googleMap;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    Marker my_location;
    Boolean showObject;

    LatLng myPosition;

    Marker currentMarker;

    ArrayList<Marker> markersWay = new ArrayList<Marker>();

    public static boolean addLocation = false;

    public static boolean add_way = false;

    Button buttonAddWay;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    void DrawWay(LatLng origin, LatLng dest, String mode) { //mode: {"driving", "walking ", "bicycling"}

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest, mode);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    void DrawWay(Marker marker1, Marker marker2, String mode) {
        LatLng origin = new LatLng(marker1.getPosition().latitude, marker1.getPosition().longitude);
        LatLng dest = new LatLng(marker2.getPosition().latitude, marker2.getPosition().longitude);
        DrawWay(origin, dest, mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        createMapView();

        buttonAddWay = (Button) findViewById(R.id.buttonAddWay);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //addToMapAll(false);
//        drawWay();
//        if (true) return;
        Uri data = getIntent().getData();
        if (data != null) {
            Button btn1, btn2, btn3;
            btn1 = (Button) findViewById(R.id.id_show_on_map_objects);
            btn2 = (Button) findViewById(R.id.id_clear_map);
            btn3 = (Button) findViewById(R.id.id_show_near_object);
            active(btn1, false);
            active(btn2, false);
            active(btn3, false);
            showObject = true;
            String str;
            str = data.toString();
            Log.i("Map", str);
            Double longitude, latitude;
            String name = str.substring(0, str.indexOf(";"));
            str = str.replace(name + ";", "");
            latitude = Double.parseDouble(str.substring(0, str.indexOf(";")));
            str = str.replace(latitude.toString() + ";", "");
            longitude = Double.parseDouble(str.substring(0, str.length() - 1));
            addMarker(latitude, longitude, name, BitmapDescriptorFactory.defaultMarker(), true);
            //zoomToPoint(new LatLng(latitude,longitude),10);
        } else {
            Button btn1, btn2, btn3;
            btn1 = (Button) findViewById(R.id.id_show_on_map_objects);
            btn2 = (Button) findViewById(R.id.id_clear_map);
            btn3 = (Button) findViewById(R.id.id_show_near_object);
            active(btn1, true);
            active(btn2, true);
            active(btn3, true);
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

                googleMap.setMyLocationEnabled(true);
                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        currentMarker = marker;
                        showDialog(1);
                    }
                });

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        if (addLocation) {
                            AddDBActivity.latitude.setText(String.valueOf(latLng.latitude));
                            AddDBActivity.longitude.setText(String.valueOf(latLng.longitude));
                            addLocation = false;

                            googleMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title(getCompleteAddressString(latLng.latitude, latLng.longitude))
                                            .draggable(true)
                            );

                        }
                        else showWayDialoq(getCompleteAddressString(latLng.latitude,latLng.longitude),"Адрес");
                    }
                });

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (add_way) {
                            markersWay.add(googleMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title("!")
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                            .draggable(true)
                            ));
                        }
                    }
                });

            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    /**
     * Geolacation
     **/

    private void addMyPosition(Location location) {
        if (null != googleMap && location != null) {

            if (my_location != null) my_location.remove();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            my_location = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Me")
                            .draggable(false)
            );

            Save(location.getLongitude(), location.getLatitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
    }

    protected void zoomToPoint(LatLng position, Integer zoom) {
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
        myPosition = my_location.getPosition();
        googleMap.clear();
        markers.clear();
        markersWay.clear();
        load();

        my_location = addMarker(
                myPosition.latitude,
                myPosition.longitude,
                "Me", BitmapDescriptorFactory.defaultMarker(), true);


    }

    public void onClickShowAllObjects(View v) {
        addToMapAll(true);
    }

    protected void addToMapAll(boolean paint) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
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
                                                                            for (int i = 0; i < point.size(); i++)
                                                                                builder.include(point.get(i));
                                                                            LatLngBounds bound = builder.build();
                                                                            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound, 50));
                                                                        }
                                                                    }
            );
        }
    }

    protected void addObjectToMap(Cursor cursor, BitmapDescriptor color, boolean show) {

        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    String name;
                    Double longitude, latitude;
                    name = cursor.getString(cursor.getColumnIndex(Table.Object_standart.NAME));
                    longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Table.Object_standart.LONGITUDE)));
                    latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Table.Object_standart.LATITUDE)));
                    addMarker(longitude, latitude, name,
                            color == null ? BitmapDescriptorFactory.defaultMarker() : color, show);
                } while (cursor.moveToNext());
            }
        cursor.close();
    }

    public void Save(Double longitude, Double latitude) {
        SharedPreferences sharedPreferences;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(Table.Object_standart.LONGITUDE, longitude.toString());
        ed.putString(Table.Object_standart.LATITUDE, latitude.toString());
        ed.commit();
        //Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    public void load() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        my_location.setPosition(new LatLng(
                Double.parseDouble(sharedPreferences.getString(Table.Object_standart.LONGITUDE, "")),
                Double.parseDouble(sharedPreferences.getString(Table.Object_standart.LATITUDE, ""))));
        //Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }

    private void active(Button button, boolean state) {
        button.setActivated(state);
        button.setAlpha(state ? 1f : 0f);
        button.setClickable(state);
    }

    public void drawWay() {

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

    private String getDirectionsUrl(ArrayList<Marker> markersWay1, String mode) {

        // Origin of route
        String str_origin = "origin=" + markersWay1.get(0).getPosition().latitude + "," + markersWay1.get(0).getPosition().longitude;

        // Destination of route
        String str_dest = "destination=" + markersWay1.get(markersWay1.size() - 1).getPosition().latitude + "," + markersWay1.get(markersWay1.size() - 1).getPosition().longitude;
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&mode" + mode;
        // Sensor enabled
        String wayPoints = "&waypoints=";
        if (markersWay1.size() > 2) {

            wayPoints += markersWay1.get(1).getPosition().latitude + "," + markersWay1.get(1).getPosition().longitude;
            for (int i = 2; i < markersWay1.size() - 2; i++)
                wayPoints += "|" + markersWay1.get(i).getPosition().latitude + "," + markersWay1.get(i).getPosition().longitude;

        parameters = str_origin + "&" + str_dest +wayPoints+ "&" + sensor + "&mode" + mode;
        }

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, String mode) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";


        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&mode" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(3);
                lineOptions.color(Color.BLUE);

            }

            // Drawing polyline in the Google Map for the i-th route
            //   if (wayPolyline != null) wayPolyline.remove();
            googleMap.addPolyline(lineOptions);
            showWayDialoq(DirectionsJSONParser.ShowInfo(), "Маршрут");

        }
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final String[] mItemsName;
        switch (id) {
            // массив
            case 1:
                mItemsName = new String[]{"Маршрут", "Про цей об\'єкт","Адрес"};
                adb.setTitle("Зробіть вибір");
                // adb.setItems(mItemsName, myClickListener1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.select_dialog_item, mItemsName);
                adb.setAdapter(adapter, myClickListener1);
                break;
            // адаптер
            case 2:
                mItemsName = new String[]{"Автомобіль", "Пішки", "Велосипед"};
                adb.setTitle("Зробіть вибір");
                adb.setItems(mItemsName, myClickListener2);
                break;

            case 3:
                mItemsName = new String[]{"Автомобіль", "Пішки", "Велосипед"};
                adb.setTitle("Зробіть вибір");
                adb.setItems(mItemsName, myClickListener3);
                break;

        }
        return adb.create();
    }


    OnClickListener myClickListener1 = new OnClickListener() {
        public void onClick(DialogInterface dialog, int item) {
            switch (item) {
                case 0:
                    showDialog(2);
                    break;

                case 1:
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.google.com/search?q=" + currentMarker.getTitle())));
                    break;
                case 2:
                    showWayDialoq(getCompleteAddressString(currentMarker.getPosition().latitude,currentMarker.getPosition().longitude),"Адрес");
                    break;
            }
        }
    };

    OnClickListener myClickListener2 = new OnClickListener() {
        public void onClick(DialogInterface dialog, int item) {
            final String[] mode = {"driving", "walking ", "bicycling"};
            // Getting URL to the Google Directions API
            DrawWay(my_location, currentMarker, mode[item]);
        }
    };

    OnClickListener myClickListener3 = new OnClickListener() {
        public void onClick(DialogInterface dialog, int item) {
            final String[] mode = {"driving", "walking ", "bicycling"};
            showWay_with_wayPoints(mode[item]);
        markersWay.clear();
        }
    };

    public void showWay_with_wayPoints(String mode) {
        String url = getDirectionsUrl(markersWay, mode);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    public void showWayDialoq(String Message,String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle(title)
                .setMessage(/*"Вкажіть точки на карті по яких буде пролягати маршрут"*/ Message)
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void OnClickAddWay(View view) {
        if (!add_way) {
            showWayDialoq("Вкажіть точки на карті по яких буде пролягати маршрут","Маршрут");
            add_way = true;
            buttonAddWay.setText("Прокласти маршрут");
        } else {
            if (markersWay.size() != 0)

                showDialog(3);
            buttonAddWay.setText("Маршрут");
            add_way = false;

        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }



}

