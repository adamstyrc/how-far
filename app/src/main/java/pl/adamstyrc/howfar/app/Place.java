package pl.adamstyrc.howfar.app;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Place {

    private String mName;
    private String mAddress;
    private String mDistance;
    private String mTime;
    private List<LatLng> mRoute;


    public Place(String name) {
        mName = name;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getName() {
        return mName;
    }

    public String getDistance() {
        return mDistance;
    }

    public String getTime() {
        return mTime;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setRoute(List<LatLng> route) {
        mRoute = route;
    }

    public List<LatLng> getRoute() {
        return mRoute;
    }
}
