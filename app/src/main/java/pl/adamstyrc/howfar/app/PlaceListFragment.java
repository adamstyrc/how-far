package pl.adamstyrc.howfar.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

public class PlaceListFragment extends ListFragment {

    private PlaceAdapter mAdapter;
    private ArrayList<Place> mPlaces;
    private LocationManager mLocationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation != null) {
                new DirectionDownloadTask(lastKnownLocation).execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPlaces = new ArrayList<Place>();
        Place place = new Place("Dom");
        place.setAddress("Racławicka 1, Kraków");
        mPlaces.add(place);

        place = new Place("Praca");
        place.setAddress("Bolesława Czerwieńskiego 8, Kraków");
        mPlaces.add(place);

        place = new Place("Ada");
        place.setAddress("Freidleina 18, Kraków");
        mPlaces.add(place);

        mAdapter = new PlaceAdapter(getActivity(), mPlaces);
        setListAdapter(mAdapter);
    }

    public class DirectionDownloadTask extends AsyncTask<Void, Void, Void> {

        private Location mUserLocation;

        public DirectionDownloadTask(Location userLocation) {
            mUserLocation = userLocation;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (Place place : mPlaces) {
                    Route route = DirectionsService.getInstance().getRoute(mUserLocation, place.getAddress());
                    if (route != null) {
                        place.setTime(route.getTotalDuration());
                        place.setDistance(route.getTotalDistance());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
