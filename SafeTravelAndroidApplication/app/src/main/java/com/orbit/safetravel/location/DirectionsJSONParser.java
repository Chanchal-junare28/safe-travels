package com.orbit.safetravel.location;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsJSONParser {

    // Parses the JSON data from Google Directions API and returns a list of routes
    public List<List<HashMap<String, String>>> parse(JSONObject jsonData) {
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jsonRoutes;
        JSONArray jsonLegs;
        JSONArray jsonSteps;

        try {
            // Get the 'routes' array from the JSON data
            jsonRoutes = jsonData.getJSONArray("routes");

            // Loop through all the routes
            for (int i = 0; i < jsonRoutes.length(); i++) {
                jsonLegs = ((JSONObject) jsonRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<>();

                // Loop through all the legs (waypoints) in the current route
                for (int j = 0; j < jsonLegs.length(); j++) {
                    jsonSteps = ((JSONObject) jsonLegs.get(j)).getJSONArray("steps");

                    // Loop through all the steps (coordinates) in the current leg
                    for (int k = 0; k < jsonSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jsonSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        // Loop through all the points and add them to the path list
                        for (LatLng latLng : list) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString(latLng.latitude));
                            hm.put("lng", Double.toString(latLng.longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routes;
    }

    // Decodes the polyline points and returns a list of LatLng objects
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
