package pl.adamstyrc.howfar.app.ui;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import pl.adamstyrc.howfar.app.utils.EventBus;
import pl.adamstyrc.howfar.app.models.Place;
import pl.adamstyrc.howfar.app.utils.PlaceManager;
import pl.adamstyrc.howfar.app.R;
import pl.adamstyrc.howfar.app.adapters.DrawerAdapter;
import pl.adamstyrc.howfar.app.events.DrawerClosedEvent;
import pl.adamstyrc.howfar.app.events.PlaceListChangedEvent;

public class PlaceManagerFragment extends Fragment {

    private ListView mDrawerList;
    private DrawerAdapter mDrawerAdapter;

    private View mBottomLayout;
    private View mAddButton;
    private View mRemoveButton;

    private View mAddFormView;
    private View mSaveButton;
    private EditText mNewNameEdit;
    private EditText mNewAddressEdit;

    private View mAnimationItem;

    private View mDraggedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_layout, container, false);

        setListView(view);

        mBottomLayout = view.findViewById(R.id.bottom_frame);
        mAddButton = view.findViewById(R.id.add_button);
        mRemoveButton = view.findViewById(R.id.remove_button);

        mAnimationItem = view.findViewById(R.id.animation_item);

        mAddFormView = view.findViewById(R.id.add_form);
        mNewNameEdit = (EditText) view.findViewById(R.id.new_name);
        mNewAddressEdit = (EditText) view.findViewById(R.id.new_address);
        mSaveButton = view.findViewById(R.id.save);

        mAnimationItem.setVisibility(View.INVISIBLE);

        setAddButton();
        setRemoveDragAndDrop();
        setSaveButton();

        return view;
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
    public void hideAddForm(DrawerClosedEvent event) {
        mAddFormView.setVisibility(View.GONE);
        mNewNameEdit.getText().clear();
        mNewAddressEdit.getText().clear();

        mAddButton.setVisibility(View.VISIBLE);
    }

    private void setSaveButton() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PlaceManager placeManager = PlaceManager.getInstance(getActivity());
                try {
                    final Place newPlace = placeManager.addPlace(mNewNameEdit.getText().toString(), mNewAddressEdit.getText().toString());

                    int toYDelta = computeAnimationDestinationY() - mAnimationItem.getTop();
                    TranslateAnimation slide = new TranslateAnimation(0,0,0, toYDelta);
                    slide.setDuration(500);
                    slide.setFillAfter(false);

                    mAnimationItem.startAnimation(slide);
                    slide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            mAddFormView.setVisibility(View.GONE);
                            mNewNameEdit.setText("");
                            mNewAddressEdit.setText("");

                            mAnimationItem.setVisibility(View.VISIBLE);
                            ((TextView) mAnimationItem.findViewById(R.id.name)).setText(newPlace.getName());
                            ((TextView) mAnimationItem.findViewById(R.id.address)).setText(newPlace.getAddress());
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mAnimationItem.setVisibility(View.INVISIBLE);

                            mDrawerAdapter = new DrawerAdapter(getActivity(), placeManager.getPlaces());
                            mDrawerList.setAdapter(mDrawerAdapter);

                            mAddButton.setVisibility(View.VISIBLE);

                            EventBus.getInstance().post(new PlaceListChangedEvent());
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.validation_message) + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setRemoveDragAndDrop() {
        mBottomLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_ENDED:
                        mRemoveButton.setVisibility(View.GONE);
                        mAddButton.setVisibility(View.VISIBLE);
                        if (mDraggedView != null) {
                            mDraggedView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case DragEvent.ACTION_DROP:
                        int position = Integer.parseInt(dragEvent.getClipDescription().getLabel().toString());
                        PlaceManager placeManager = PlaceManager.getInstance(getActivity());
                        placeManager.removePlace(position);

                        mDrawerAdapter = new DrawerAdapter(getActivity(), placeManager.getPlaces());
                        mDrawerList.setAdapter(mDrawerAdapter);

                        EventBus.getInstance().post(new PlaceListChangedEvent());
                        break;
                }
                return true;
            }
        });
    }

    private void setAddButton() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddButton.setVisibility(View.GONE);
                mAddFormView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setListView(View view) {
        mDrawerList = (ListView) view.findViewById(R.id.drawer_list);
        mDrawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                ClipData data = ClipData.newPlainText(String.valueOf(id), "text");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                mDraggedView = view;
                mDraggedView.startDrag(data, shadowBuilder, null, 0);


                view.setVisibility(View.GONE);

                mRemoveButton.setVisibility(View.VISIBLE);
                mAddButton.setVisibility(View.GONE);
                mAddFormView.setVisibility(View.GONE);

                return true;
            }
        });
        mDrawerAdapter = new DrawerAdapter(getActivity(), PlaceManager.getInstance(getActivity()).getPlaces());
        mDrawerList.setAdapter(mDrawerAdapter);
    }

    private int computeAnimationDestinationY() {
        View lastListViewChild = mDrawerList.getChildAt(mDrawerList.getChildCount() - 1);
        if (lastListViewChild == null) {
            return mDrawerList.getTop();
        }
        if (lastListViewChild.getBottom() <= mDrawerList.getBottom()) {
            return lastListViewChild.getBottom();
        } else {
            return mDrawerList.getBottom();
        }
    }
}
