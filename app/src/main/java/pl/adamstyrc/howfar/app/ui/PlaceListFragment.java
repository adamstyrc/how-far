package pl.adamstyrc.howfar.app.ui;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.List;

import pl.adamstyrc.howfar.app.DirectionsService;
import pl.adamstyrc.howfar.app.EventBus;
import pl.adamstyrc.howfar.app.Place;
import pl.adamstyrc.howfar.app.PlaceManager;
import pl.adamstyrc.howfar.app.R;
import pl.adamstyrc.howfar.app.Route;
import pl.adamstyrc.howfar.app.TransportManager;
import pl.adamstyrc.howfar.app.adapters.PlaceAdapter;
import pl.adamstyrc.howfar.app.events.PlaceListChangedEvent;

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
            Location myLocation = ((MainActivity) getActivity()).getMap().getMyLocation();
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

        setListAdapter(null);

        View headerView = View.inflate(getActivity(), R.layout.place_list_header, null);
        getListView().addHeaderView(headerView, null, false);

        mAdapter = new PlaceAdapter(getActivity(), PlaceManager.getInstance(getActivity()).getPlaces());
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ((MainActivity) getActivity()).showMap((int) id);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getInstance().unregister(this);
    }

    @Subscribe
    public void update(PlaceListChangedEvent event) {
        getListView().setAdapter(null);
        List<Place> places = PlaceManager.getInstance(getActivity()).getPlaces();
        mAdapter = new PlaceAdapter(getActivity(), places);
        getListView().setAdapter(mAdapter);
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
                for (Place place : PlaceManager.getInstance(getActivity()).getPlaces()) {
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
