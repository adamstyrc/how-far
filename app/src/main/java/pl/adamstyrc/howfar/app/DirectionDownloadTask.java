package pl.adamstyrc.howfar.app;

import android.location.Location;
import android.os.AsyncTask;

/**
 * Created by Light on 18.03.14.
 */
public class DirectionDownloadTask extends AsyncTask<Void, Void, Void> {

    private Location mUserLocation;

    public DirectionDownloadTask(Location userLocation) {
        mUserLocation = userLocation;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }
}
