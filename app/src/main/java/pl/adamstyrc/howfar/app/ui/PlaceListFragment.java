package pl.adamstyrc.howfar.app.ui;

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
import android.widget.AdapterView;

import java.io.IOException;

import pl.adamstyrc.howfar.app.DirectionsService;
import pl.adamstyrc.howfar.app.Place;
import pl.adamstyrc.howfar.app.PlacesManager;
import pl.adamstyrc.howfar.app.R;
import pl.adamstyrc.howfar.app.Route;
import pl.adamstyrc.howfar.app.adapters.PlaceAdapter;

public class PlaceListFragment extends ListFragment {

    private PlaceAdapter mAdapter;

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

        mAdapter = new PlaceAdapter(getActivity(), PlacesManager.getInstance().getPlaces());
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Place place = mAdapter.getItem(position);
                if (place != null) {
                    ((ListPreviewFragment) getParentFragment()).showMap(place);
                }
            }
        });
    }

    public class DirectionDownloadTask extends AsyncTask<Void, Void, Void> {

        private Location mUserLocation;

        public DirectionDownloadTask(Location userLocation) {
            mUserLocation = userLocation;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (Place place : PlacesManager.getInstance().getPlaces()) {
                    Route route = DirectionsService.getInstance().getRoute(mUserLocation, place.getAddress());
                    if (route != null) {
                        place.setTime(route.getTotalDuration());
                        place.setDistance(route.getTotalDistance());
                        place.setRoute(route.getStepsLocations());
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
