package pl.adamstyrc.howfar.app;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

public class PlaceManager {

    private static PlaceManager sInstance;

    private List<Place> mPlaces;
    private DatabaseHelper mDatabaseHelper = null;

    public static synchronized PlaceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PlaceManager(context);
        }

        return sInstance;
    }

    public void clear() {
        OpenHelperManager.releaseHelper();
        mDatabaseHelper = null;
        sInstance = null;
    }

    private PlaceManager(Context context) {
        mPlaces = new ArrayList<Place>();
        mDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        mPlaces = mDatabaseHelper.getPlaceDao().queryForAll();
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }
}
