package pl.adamstyrc.howfar.app;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DirectionsService {
    public static final String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=%s,%s" +
            "&destination=%s,%s" +
            "&sensor=false&units=metric&mode=driving";
    private static DirectionsService sInstance;

    public static synchronized DirectionsService getInstance() {
        if (sInstance == null) {
            sInstance = new DirectionsService();
        }

        return sInstance;
    }

    private DirectionsService() {

    }

    public Route getRoute(LatLng origin, LatLng destination) throws IOException {
        URL url = makeUrl(origin, destination);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-length", "0");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setAllowUserInteraction(false);
//            httpURLConnection.setConnectTimeout(timeout);
//            httpURLConnection.setReadTimeout(timeout);
        httpURLConnection.connect();
        int status = httpURLConnection.getResponseCode();

        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                RouteParser routesParser = new RouteParser();
                String json = sb.toString();
                Route route = routesParser.parse(json);
                return route;
        }

        return null;
    }

    private URL makeUrl(LatLng origin, LatLng destination) throws MalformedURLException {
        String originLat = String.valueOf(origin.latitude);
        String originLng = String.valueOf(origin.longitude);
        String destinationLat = String.valueOf(destination.latitude);
        String destinationLng = String.valueOf(destination.longitude);
        String url = String.format(BASE_URL, originLat, originLng, destinationLat, destinationLng);
        return new URL(url);
    }
}
