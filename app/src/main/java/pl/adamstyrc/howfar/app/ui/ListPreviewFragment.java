package pl.adamstyrc.howfar.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.adamstyrc.howfar.app.R;

public class ListPreviewFragment extends Fragment {

    private static final String LIST_FRAGMENT = "list fragment";
    private boolean mIsPhone;
    private View mListLayout;
    private View mPreviewLayout;

    private PlaceListFragment mListFragment;

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
            getChildFragmentManager().beginTransaction().add(mListLayout.getId(), mListFragment, LIST_FRAGMENT).commit();
        }


        return view;
    }

}
