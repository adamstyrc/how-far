package pl.adamstyrc.howfar.app;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RouteParser {

    public Route parse(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray routes = jsonObject.getJSONArray("routes");
            JSONObject routesJSONObject = routes.getJSONObject(0);

            JSONArray legs = routesJSONObject.getJSONArray("legs");


            ArrayList<LatLng> stepLocations = new ArrayList<LatLng>();


            JSONObject leg = legs.getJSONObject(0);
            String distance = leg.getJSONObject("distance").getString("text");
            String duration = leg.getJSONObject("duration").getString("text");

            JSONArray steps = leg.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                JSONObject startLocation = step.getJSONObject("start_location");
//                stepLocations.add(
//                        new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"))
//                );

                JSONObject polyline = step.getJSONObject("polyline");
                ArrayList<LatLng> polylinePoints = decodePoly(polyline.getString("points"));
                stepLocations.addAll(polylinePoints);

                JSONObject endLocation = step.getJSONObject("end_location");
//                stepLocations.add(
//                        new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng"))
//                );
            }

            return new Route(distance, duration, stepLocations);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
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

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
//            Log.d("Lat", String.valueOf(position.latitude));
//            Log.d("Lng", String.valueOf(position.longitude));
            poly.add(position);
        }

        return poly;
    }
}