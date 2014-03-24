package pl.adamstyrc.howfar.app;

import java.util.ArrayList;

/**
 * Created by Light on 19.03.14.
 */
public class PlaceManager {

    private static PlaceManager sInstance;

    public static synchronized PlaceManager getInstance() {
        if (sInstance == null) {
            sInstance = new PlaceManager();
        }

        return sInstance;
    }

    private ArrayList<Place> mPlaces;

    private PlaceManager() {
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
