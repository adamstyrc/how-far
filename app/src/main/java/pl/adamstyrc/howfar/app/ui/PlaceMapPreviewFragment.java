package pl.adamstyrc.howfar.app.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import pl.adamstyrc.howfar.app.Place;
import pl.adamstyrc.howfar.app.PlaceManager;
import pl.adamstyrc.howfar.app.R;

public class PlaceMapPreviewFragment extends SupportMapFragment {

    public static final String PLACE_ID = "place_id";
    public static final int NO_ID = -1;

    private int mId = NO_ID;
    private Place mPlace;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMap().setMyLocationEnabled(true);

        if (savedInstanceState != null) {
            mId = savedInstanceState.getInt(PLACE_ID, NO_ID);
        }

        if (mId != NO_ID) {
            showPlace(mId);
        }
    }

    public void showPlace(int placeId) {
        mId = placeId;
        mPlace = PlaceManager.getInstance(getActivity()).getPlaceById(placeId);

        getMap().clear();

        List<LatLng> route = mPlace.getRoute();
        if (route != null) {
            LatLng finalLocation = route.get(route.size() - 1);
            getMap().addMarker(new MarkerOptions()
                    .position(finalLocation)
                    .title(mPlace.getName())).showInfoWindow();

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(route);
            polylineOptions.width(7);
            polylineOptions.color(getResources().getColor(R.color.map_path));
            getMap().addPolyline(polylineOptions);


            zoomCameraToRoute(route);
        }
    }

    private void zoomCameraToRoute(List<LatLng> route) {
        final LatLngBounds.Builder bc = new LatLngBounds.Builder();
        for (LatLng item : route) {
            bc.include(item);
        }

        getMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), getPadding()));
                getMap().setOnCameraChangeListener(null);
            }
        });
        getMap().moveCamera(CameraUpdateFactory.zoomOut());
    }

    private int getPadding() {
        Resources r = getResources();
        int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 84, r.getDisplayMetrics());
        return pixels;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PLACE_ID, mId);
    }

}
