package pl.adamstyrc.howfar.app;

public class Route {

    private String mTotalDistance;
    private String mTotalDuration;
    private String mPoints;

    public Route(String totalDistance, String totalDuration, String points) {
        mTotalDistance = totalDistance;
        mTotalDuration = totalDuration;
        mPoints = points;
    }

    public String getTotalDistance() {
        return mTotalDistance;
    }

    public String getTotalDuration() {
        return mTotalDuration;
    }

    public String getPoints() {
        return mPoints;
    }
}