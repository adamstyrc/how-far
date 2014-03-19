package pl.adamstyrc.howfar.app;

import android.location.Location;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class DirectionsService {
    public static final String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=%s,%s" +
            "&destination=%s" +
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

    public Route getRoute(Location origin, String destination) throws IOException {
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

    private URL makeUrl(Location origin, String destination) throws MalformedURLException {
        String originLat = String.valueOf(origin.getLatitude());
        String originLng = String.valueOf(origin.getLongitude());
        try {
            destination = URLEncoder.encode(destination, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        String url = String.format(BASE_URL, originLat, originLng, destination);
        return new URL(url);
    }
}
