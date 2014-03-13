package pl.adamstyrc.howfar.app;

public class Place {

    private String mName;
    private String mDistance;
    private String mTime;

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
}
