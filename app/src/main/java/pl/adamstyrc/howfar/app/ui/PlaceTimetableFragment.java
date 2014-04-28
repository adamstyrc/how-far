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

import pl.adamstyrc.howfar.app.utils.DirectionsService;
import pl.adamstyrc.howfar.app.utils.EventBus;
import pl.adamstyrc.howfar.app.models.Place;
import pl.adamstyrc.howfar.app.utils.PlaceManager;
import pl.adamstyrc.howfar.app.R;
import pl.adamstyrc.howfar.app.models.Route;
import pl.adamstyrc.howfar.app.utils.TransportManager;
import pl.adamstyrc.howfar.app.adapters.PlaceAdapter;
import pl.adamstyrc.howfar.app.events.PlaceListChangedEvent;
import pl.adamstyrc.howfar.app.utils.NetworkUtils;

public class PlaceTimetableFragment extends ListFragment {

    private PlaceAdapter mAdapter;
    private MenuItem mRefreshItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main, menu);
        mRefreshItem = menu.findItem(R.id.action_refresh);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {

            if (!NetworkUtils.isOnline(getActivity())) {
                Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                return true;
            }

            Location myLocation = ((MainActivity) getActivity()).getMap().getMyLocation();
            if (myLocation == null) {
                Toast.makeText(getActivity(), getString(R.string.unknown_location_warning), Toast.LENGTH_SHORT).show();
                return true;
            }

            new DirectionDownloadTask(myLocation).execute();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
        protected void onPreExecute() {
            super.onPreExecute();

            mRefreshItem.setVisible(false);
            getActivity().setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int meanOfTransport = new TransportManager(getActivity()).getMeanOfTransport();
            try {
                PlaceManager placeManager = PlaceManager.getInstance(getActivity());

                for (Place place : placeManager.getPlaces()) {
                    Route route = DirectionsService.getInstance().getRoute(mUserLocation, place.getAddress(), meanOfTransport);
                    if (route != null) {
                        place.setRoute(route);
                        placeManager.updatePlace(place);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getActivity().setProgressBarIndeterminateVisibility(false);
            mRefreshItem.setVisible(true);
            update(null);
        }
    }
}
