package pl.adamstyrc.howfar.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import pl.adamstyrc.howfar.app.R;

public class ListPreviewFragment extends Fragment {

    private static final String LIST_FRAGMENT = "list fragment";
    private static final String PREVIEW_FRAGMENT = "preview fragment";

    private boolean mIsPhone;
    private View mListLayout;
    private View mPreviewLayout;

    private ListFragment mListFragment;
    private PlacePreviewFragment mPreviewFragment;

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
            mPreviewFragment = new PlacePreviewFragment();
            ft.add(mListLayout.getId(), mListFragment, LIST_FRAGMENT)
                    .add(mPreviewLayout.getId(), mPreviewFragment, PREVIEW_FRAGMENT);
            if (mIsPhone) {
                ft.hide(mPreviewFragment);
            }
            ft.commit();
        } else {
            mListFragment = (ListFragment) getChildFragmentManager().findFragmentByTag(LIST_FRAGMENT);
            mPreviewFragment = (PlacePreviewFragment) getChildFragmentManager().findFragmentByTag(PREVIEW_FRAGMENT);

            if (getChildFragmentManager().getBackStackEntryCount() == 0) {
                getChildFragmentManager().beginTransaction().hide(mPreviewFragment).commit();
            }
        }

        return view;
    }

    public void showMap(int placeId) {
        mPreviewFragment.showPlace(placeId);

        if (mIsPhone) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.show(mPreviewFragment)
                    .remove(mListFragment)
                    .addToBackStack(null).commit();

            ((MainActivity) getActivity()).showHomeAsUp(true);
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
