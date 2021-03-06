package pl.adamstyrc.howfar.app.models;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import pl.adamstyrc.howfar.app.utils.RouteParser;

@DatabaseTable(tableName = "place")
public class Place {

    @DatabaseField(generatedId = true)
    private int mId;
    @DatabaseField(unique = true)
    private String mName;
    @DatabaseField
    private String mAddress;
    @DatabaseField
    private String mDistance;
    @DatabaseField
    private String mTime;
    @DatabaseField
    private String mRoute;

    public Place() {}

    public Place(String name) {
        mName = name;
    }

    public Place(String name, String address) {
        mName = name;
        mAddress = address;
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

    public void setRoute(Route route) {
        mDistance = route.getTotalDistance();
        mTime = route.getTotalDuration();
        mRoute = route.getPoints();
    }

    public List<LatLng> getRoutePoints() {
        RouteParser routeParser = new RouteParser();
        if (mRoute != null) {
            return routeParser.decodeRouteSteps(mRoute);
        } else {
            return null;
        }
    }

    public int getId() {
        return mId;
    }
}
