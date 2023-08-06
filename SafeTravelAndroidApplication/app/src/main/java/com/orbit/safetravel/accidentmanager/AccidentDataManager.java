package com.orbit.safetravel.accidentmanager;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.orbit.safetravel.accidentmanager.AccidentApi;
import com.orbit.safetravel.accidentmanager.AccidentData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccidentDataManager {

    private static Retrofit retrofit;


    public void fetchData(Context context, TextView details) {
       // Toast.makeText(context, "InFetch", Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "InFetch", Toast.LENGTH_SHORT).show();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://safe-travel-g52z.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        AccidentApi accidentApi = retrofit.create(AccidentApi.class);
        Call<List<AccidentData>> call = accidentApi.getAccidentData();
        call.enqueue(new Callback<List<AccidentData>>() {
            @Override
            public void onResponse(Call<List<AccidentData>> call, Response<List<AccidentData>> response) {
                if (response.isSuccessful()) {
                    List<AccidentData> accidentDataList = response.body();
                    if (accidentDataList != null && !accidentDataList.isEmpty()) {
//                        StringBuilder resultBuilder = new StringBuilder();
//
//                        for (AccidentData accidentData : accidentDataList) {
//                            String result = "Accident Index: " + accidentData.getAccidentIndex() + "\n" +
//                                    "Accident Date: " + accidentData.getAccidentDate() + "\n" +
//                                    "Day of Week: " + accidentData.getDayOfWeek() + "\n";
//                            resultBuilder.append(result).append("\n");
//                        }
                        StringBuilder resultBuilder = new StringBuilder(accidentDataList.size());
                        details.setText(resultBuilder);
                        details.setText(accidentDataList.get(0).getAccidentDate());
                       // Toast.makeText(this, "Se"+accidentDataList.size()+"Success"+ resultBuilder, Toast.LENGTH_SHORT).show();

                    } else {
                        details.setText("No data available");
                    }
                } else {
                    details.setText("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<List<AccidentData>> call, Throwable t) {
                details.setText("Error: " + t.getMessage());
            }
        });

    }

    public void fetchAccidentAreas(LatLng currentLatLng, LatLng destinetionLatLng){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://safe-travel-g52z.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        JSONObject requestBodyJson = new JSONObject();
        try {
            requestBodyJson.put("currentLatitude", currentLatLng.latitude);
            requestBodyJson.put("currentLongitude", currentLatLng.longitude);
            requestBodyJson.put("destinationLatitude", destinetionLatLng.latitude);
            requestBodyJson.put("destinationLongitude", destinetionLatLng.longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

// Convert the JSON request body to RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson.toString());

// Create an instance of your ApiService
        AccidentApi apiService = retrofit.create(AccidentApi.class);

// Make the API call
        Call<List<AccidentData>> call = apiService.getAccidentDataAreas(requestBody);
        call.enqueue(new Callback<List<AccidentData>>() {
            @Override
            public void onResponse(Call<List<AccidentData>> call, Response<List<AccidentData>> response) {
                if (response.isSuccessful()) {
                    List<AccidentData> accidentDataList = response.body();
                    // Process the accidentDataList here
                    Log.d("Err", ""+accidentDataList.size());
                } else {
                    // Handle API error
                    Log.d("Err", "Error while fetching data");
                }
            }

            @Override
            public void onFailure(Call<List<AccidentData>> call, Throwable t) {
                // Handle failure
                Log.d("Err", "Error while calling service");
            }
        });

    }
}

