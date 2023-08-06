package com.orbit.safetravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.orbit.safetravel.accidentmanager.AccidentDataManager;
import com.orbit.safetravel.location.ViewLocation;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

//    private Button getDataButton;
//    private AccidentDataManager accidentDataManager;
//    private TextView details;
//    private static Retrofit retrofit;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;

    // Replace with this number with emergency number
    private String phoneNumber = "9657317101";


    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private ViewLocation viewLocation;
    private Button btnSendLocation;

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.map_search_view);
        btnSendLocation = findViewById(R.id.btnSendLocation);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);

        viewLocation = new ViewLocation(MainActivity.this, supportMapFragment, fusedLocationProviderClient);
        viewLocation.setLocationConfig();
        viewLocation.getCurrentLocation();
//        getDataButton = findViewById(R.id.getData);
//        details = findViewById(R.id.details);
//        accidentDataManager = new AccidentDataManager();
//
//        getDataButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                accidentDataManager.fetchData(MainActivity.this, details);
//                Toast.makeText(MainActivity.this, "Method Complete", Toast.LENGTH_SHORT).show();
//            }
//        });

        btnSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 checkPermissionAndSendLocation();
                //sendLocation();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;


                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addressList == null || addressList.isEmpty())
                        Toast.makeText(MainActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                    else {
                        Address address = addressList.get(0);
                        Toast.makeText(MainActivity.this, "" + address.getCountryName(), Toast.LENGTH_SHORT).show();
                        LatLng destinationLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                        viewLocation.updateRoute(destinationLatLng);
                    }
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void checkPermissionAndSendLocation() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            sendLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }


    private void sendLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String message = "Latitude: " + location.getLatitude() +
                            ", Longitude: " + location.getLongitude();
                    Log.d("Location", message);
                    sendSMS(phoneNumber, message);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            Toast.makeText(this, "Please enable GPS to send location.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String phoneNumber, String message) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//            Toast.makeText(this, "Location sent successfully.", Toast.LENGTH_SHORT).show();
//        } catch (Exception ex) {
//            Toast.makeText(this, "Failed to send location.", Toast.LENGTH_SHORT).show();
//            ex.printStackTrace();
//        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);

            smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, deliveredIntent);
            Toast.makeText(MainActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    // Check Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The user granted the location permissions, you can proceed with location-related tasks
                viewLocation.getCurrentLocation();
            } else {
                // The user denied the location permissions, handle this situation (e.g., show a message or disable location-related features)
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}