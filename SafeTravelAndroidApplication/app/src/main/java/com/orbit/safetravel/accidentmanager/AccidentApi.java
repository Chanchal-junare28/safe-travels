package com.orbit.safetravel.accidentmanager;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccidentApi {
    @GET("api/accidents") // Replace "your_api_endpoint_here" with the actual API endpoint URL
    Call<List<AccidentData>> getAccidentData();

    @POST("20.87204487076285,%2076.26088272450681/20.728092874307414,%2076.92951562072189")
    Call<List<AccidentData>> getAccidentDataAreas(@Body RequestBody requestBody);
}
