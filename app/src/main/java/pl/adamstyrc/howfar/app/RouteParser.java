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
            JSONObject leg = legs.getJSONObject(0);
            String distance = leg.getJSONObject("distance").getString("text");
            String duration = leg.getJSONObject("duration").getString("text");

            JSONObject overviewPolyline = routesJSONObject.getJSONObject("overview_polyline");
            String points = overviewPolyline.getString("points");
            return new Route(distance, duration, points);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<LatLng> decodeRouteSteps(String encoded) {
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
            poly.add(position);
        }

        return poly;
    }
}