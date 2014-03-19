package pl.adamstyrc.howfar.app;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {

    private String mTotalDistance;
    private String mTotalDuration;
    private List<LatLng> mStepsLocations;

    public Route(String totalDistance, String totalDuration, List<LatLng> stepsLocations) {
        mTotalDistance = totalDistance;
        mTotalDuration = totalDuration;
        mStepsLocations = stepsLocations;
    }

    public String getTotalDistance() {
        return mTotalDistance;
    }

    public String getTotalDuration() {
        return mTotalDuration;
    }

    public List<LatLng> getStepsLocations() {
        return mStepsLocations;
    }
}