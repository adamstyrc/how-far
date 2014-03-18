package pl.adamstyrc.howfar.app;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {

    private long mTotalDistance;
    private long mTotalDuration;
    private List<LatLng> mStepsLocations;

    public Route(long mTotalDistance, long mTotalDuration, List<LatLng> mStartLocations) {
        this.mTotalDistance = mTotalDistance;
        this.mTotalDuration = mTotalDuration;
        this.mStepsLocations = mStartLocations;
    }

    public long getTotalDistance() {
        return mTotalDistance;
    }

    public long getTotalDuration() {
        return mTotalDuration;
    }

    public List<LatLng> getStepsLocations() {
        return mStepsLocations;
    }
}