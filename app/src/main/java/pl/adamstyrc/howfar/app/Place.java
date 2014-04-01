package pl.adamstyrc.howfar.app;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "place")
public class Place {

    @DatabaseField(generatedId = true)
    private int mId;
    @DatabaseField(unique = true)
    private String mName;
    @DatabaseField
    private String mAddress;

    private String mDistance;
    private String mTime;
    private List<LatLng> mRoute;

    public Place() {
    }

    public Place(String name) {
        mName = name;
    }

    public Place(String name, String address) {
        mName = name;
        mAddress = address;
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

    public int getId() {
        return mId;
    }
}
