package pl.adamstyrc.howfar.app;

import java.util.ArrayList;

/**
 * Created by Light on 19.03.14.
 */
public class PlacesManager {

    private static PlacesManager sInstance;

    public static synchronized PlacesManager getInstance() {
        if (sInstance == null) {
            sInstance = new PlacesManager();
        }

        return sInstance;
    }

    private ArrayList<Place> mPlaces;

    private PlacesManager() {
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
    }

    public ArrayList<Place> getPlaces() {
        return mPlaces;
    }
}
