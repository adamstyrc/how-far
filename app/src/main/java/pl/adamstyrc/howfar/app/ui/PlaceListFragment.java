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
import android.widget.Toast;

import java.io.IOException;

import pl.adamstyrc.howfar.app.DirectionsService;
import pl.adamstyrc.howfar.app.Place;
import pl.adamstyrc.howfar.app.PlaceManager;
import pl.adamstyrc.howfar.app.R;
import pl.adamstyrc.howfar.app.Route;
import pl.adamstyrc.howfar.app.TransportManager;
import pl.adamstyrc.howfar.app.adapters.PlaceAdapter;

public class PlaceListFragment extends ListFragment {

    private PlaceAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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
            Location myLocation = ((ListPreviewFragment) getParentFragment()).getMap().getMyLocation();
            if (myLocation != null) {
                new DirectionDownloadTask(myLocation).execute();
            } else {
                Toast.makeText(getActivity(), "Can't get current location", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new PlaceAdapter(getActivity(), PlaceManager.getInstance().getPlaces());
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
            int meanOfTransport = new TransportManager(getActivity()).getMeanOfTransport();
            try {
                for (Place place : PlaceManager.getInstance().getPlaces()) {
                    Route route = DirectionsService.getInstance().getRoute(mUserLocation, place.getAddress(), meanOfTransport);
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
