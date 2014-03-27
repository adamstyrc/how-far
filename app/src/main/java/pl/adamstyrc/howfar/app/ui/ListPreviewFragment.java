package pl.adamstyrc.howfar.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import pl.adamstyrc.howfar.app.Place;
import pl.adamstyrc.howfar.app.R;

public class ListPreviewFragment extends Fragment {

    private static final String LIST_FRAGMENT = "list fragment";
    private static final String PREVIEW_FRAGMENT = "preview fragment";

    private boolean mIsPhone;
    private View mListLayout;
    private View mPreviewLayout;

    private ListFragment mListFragment;
    private Fragment mPreviewFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.list_preview, null);

        mIsPhone = view.findViewById(R.id.right) == null;
        if (mIsPhone) {
            mListLayout = view.findViewById(R.id.left);
            mPreviewLayout = view.findViewById(R.id.left);
        } else {
            mListLayout = view.findViewById(R.id.left);
            mPreviewLayout = view.findViewById(R.id.right);
        }

        if (savedInstanceState == null) {
            mListFragment = new PlaceListFragment();
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            mPreviewFragment = new SupportMapFragment();
            ft.add(mListLayout.getId(), mListFragment, LIST_FRAGMENT)
                    .add(mPreviewLayout.getId(), mPreviewFragment, PREVIEW_FRAGMENT);
            if (mIsPhone) {
                ft.hide(mPreviewFragment);
            }
            ft.commit();
        } else {
            mListFragment = (ListFragment) getChildFragmentManager().findFragmentByTag(LIST_FRAGMENT);
            mPreviewFragment = getChildFragmentManager().findFragmentByTag(PREVIEW_FRAGMENT);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((SupportMapFragment)mPreviewFragment).getMap().setMyLocationEnabled(true);
    }

    public void showMap(Place place) {
        getMap().clear();

        List<LatLng> route = place.getRoute();
        if (route != null) {
            LatLng finalLocation = route.get(route.size() - 1);
            getMap().addMarker(new MarkerOptions()
                    .position(finalLocation)
                    .title(place.getName())).showInfoWindow();

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(route);
            polylineOptions.width(7);
            polylineOptions.color(getResources().getColor(R.color.map_path));
            getMap().addPolyline(polylineOptions);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(route.get(0))      // Sets the center of the map to location user
                    .zoom(9)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        if (mIsPhone) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.show(mPreviewFragment)
                    .remove(mListFragment)
                    .addToBackStack(null).commit();
        }
    }

    public void onBackPressed() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();
        } else {
            getActivity().finish();
        }
    }

    public GoogleMap getMap() {
        return ((SupportMapFragment) mPreviewFragment).getMap();
    }
}
