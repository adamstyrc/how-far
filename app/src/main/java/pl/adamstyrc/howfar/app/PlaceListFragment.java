package pl.adamstyrc.howfar.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import java.util.ArrayList;

public class PlaceListFragment extends ListFragment {

    private PlaceAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Place> places = new ArrayList<Place>();
        Place place = new Place("Dom");
        place.setDistance("15km");
        place.setTime("5 min");
        places.add(place);

        place = new Place("Praca");
        place.setDistance("3km");
        place.setTime("1 min");
        places.add(place);

        place = new Place("Asiula");
        place.setDistance("50km");
        place.setTime("1 h 10 min");
        places.add(place);

        mAdapter = new PlaceAdapter(getActivity(), places);
        setListAdapter(mAdapter);
    }
}
