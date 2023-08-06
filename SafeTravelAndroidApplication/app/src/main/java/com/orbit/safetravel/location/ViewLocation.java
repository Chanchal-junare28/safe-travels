package com.orbit.safetravel.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orbit.safetravel.MainActivity;
import com.orbit.safetravel.R;
import com.orbit.safetravel.accidentmanager.AccidentDataManager;

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

public class ViewLocation {
    public static GoogleMap googleMap;
    private Context context;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationData currentLocation;
    private static final int REQUEST_LOCATION_PERMISSION = 100;

    
    public ViewLocation(MainActivity context, SupportMapFragment supportMapFragment, FusedLocationProviderClient fusedLocationProviderClient) {
        this.context = context;
        this.supportMapFragment = supportMapFragment;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }


    public void setLocationConfig(){

        Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getCurrentLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        if(location != null){
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            Toast.makeText(context, ""+latLng.toString(), Toast.LENGTH_SHORT).show();
                            currentLocation = new LocationData(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Me-");
                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            setMap(googleMap);
                        }
                    }


                });
            }
        });


    }

    private void setMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    public void updateRoute(LatLng destinetionLatLng){
        getCurrentLocation();
        AccidentDataManager accidentDataManager = new AccidentDataManager();
       try{
           if(currentLocation != null ){
               LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
               updateMapWithLocation(currentLatLng, destinetionLatLng);
               accidentDataManager.fetchAccidentAreas(currentLatLng, destinetionLatLng);
           }else{
               getCurrentLocation();
               LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
               updateMapWithLocation(currentLatLng, destinetionLatLng);
               accidentDataManager.fetchAccidentAreas(currentLatLng, destinetionLatLng);
           }


       }catch (NullPointerException e) {
           e.printStackTrace();
       } catch (Exception e) {
           e.printStackTrace();
       }

    }


    private void updateMapWithLocation(LatLng currentLatLng, LatLng destinationLatLng) {
        if (googleMap != null) {
            // Update map with current location and destination

            googleMap.clear();

            MarkerOptions markerOptions = new MarkerOptions();
            if(destinationLatLng.latitude <= 20.707228 && destinationLatLng.latitude >= 20.685555 || destinationLatLng.longitude == 77.002960){
                LatLng markerPosition = new LatLng( 20.685555, 76.797951);
                googleMap.addMarker(new MarkerOptions().position(markerPosition).title("Accident Prone Area"));

            }

            googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
            googleMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 5));


            // Draw route on the map
            String url = getDirectionsUrl(currentLatLng, destinationLatLng);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }

    class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
            Toast.makeText(context, "You are good to go..", Toast.LENGTH_SHORT).show();

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) googleMap.addPolyline(lineOptions);

        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

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
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


}
