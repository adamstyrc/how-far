package pl.adamstyrc.howfar.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.SupportMapFragment;

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
                    .add(mPreviewLayout.getId(), mPreviewFragment, PREVIEW_FRAGMENT)
                    .commit();
        } else {
            mListFragment = (ListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT);
            mPreviewFragment = (Fragment) getFragmentManager().findFragmentByTag(PREVIEW_FRAGMENT);
        }

        return view;
    }

    public void showMap() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.show(mPreviewFragment).commit();
    }

    public void hideMap() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.hide(mPreviewFragment).commit();
    }

    public void onBackPressed() {
        if (mPreviewFragment.isHidden()) {
            getActivity().finish();
        } else {
            hideMap();
        }
    }
}
