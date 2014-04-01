package pl.adamstyrc.howfar.app.ui;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import pl.adamstyrc.howfar.app.Place;
import pl.adamstyrc.howfar.app.PlaceManager;
import pl.adamstyrc.howfar.app.R;
import pl.adamstyrc.howfar.app.TransportManager;
import pl.adamstyrc.howfar.app.adapters.DrawerAdapter;
import pl.adamstyrc.howfar.app.adapters.SelectorAdapter;


public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerAdapter mDrawerAdapter;

    private ListPreviewFragment mListPreviewFragment;
    private boolean mShow;
    private View mAddButton;
    private View mSaveButton;
    private EditText mNewNameEdit;
    private EditText mNewAddressEdit;
    private View mRemoveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionBarSelector();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                ClipData data = ClipData.newPlainText(String.valueOf(id), "text");
                data.addItem(new ClipData.Item("HAHA"));
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, null, 0);

                view.setVisibility(View.GONE);

                mRemoveButton.setVisibility(View.VISIBLE);
                mAddButton.setVisibility(View.GONE);

                return true;
            }
        });
        mDrawerAdapter = new DrawerAdapter(this, PlaceManager.getInstance(this).getPlaces());
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.inflate(R.menu.place_popup_menu);
                popupMenu.show();
                Place place = mDrawerAdapter.getItem(position);
            }
        });

        mAddButton = findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddButton.setVisibility(View.GONE);
                findViewById(R.id.new_item).setVisibility(View.VISIBLE);

            }
        });

        mRemoveButton = findViewById(R.id.remove_button);
        mRemoveButton.setOnDragListener(new View.OnDragListener() {

            private boolean mIsOnRemove;

            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                Log.d("ACHTUNG2", dragEvent.getAction() + "");

                switch (dragEvent.getAction()) {
//                    case DragEvent.ACTION_DRAG_ENTERED:
//                        mIsOnRemove = true;
//                        break;
//                    case DragEvent.ACTION_DRAG_EXITED:
//                        mIsOnRemove = false;
                    case DragEvent.ACTION_DRAG_ENDED:
                        mRemoveButton.setVisibility(View.GONE);
                        mAddButton.setVisibility(View.VISIBLE);

//                        if (mIsOnRemove) {
//                            int position = Integer.parseInt(dragEvent.getClipDescription().getLabel().toString());
//                            PlaceManager placeManager = PlaceManager.getInstance(MainActivity.this);
//                            placeManager.removePlace(position);
//
//                            mDrawerAdapter = new DrawerAdapter(MainActivity.this, placeManager.getPlaces());
//                            mDrawerList.setAdapter(mDrawerAdapter);
//                        }
                        break;
                    case DragEvent.ACTION_DROP:
                        int position = Integer.parseInt(dragEvent.getClipDescription().getLabel().toString());
                        PlaceManager placeManager = PlaceManager.getInstance(MainActivity.this);
                        placeManager.removePlace(position);

                        mDrawerAdapter = new DrawerAdapter(MainActivity.this, placeManager.getPlaces());
                        mDrawerList.setAdapter(mDrawerAdapter);
                        break;
                }
                return true;
            }
        });



        mNewNameEdit = (EditText) findViewById(R.id.new_name);
        mNewAddressEdit = (EditText) findViewById(R.id.new_address);

        mSaveButton = findViewById(R.id.save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PlaceManager placeManager = PlaceManager.getInstance(MainActivity.this);
                placeManager.addPlace(mNewNameEdit.getText().toString(), mNewAddressEdit.getText().toString());

                View newItem = findViewById(R.id.bottom_frame);

                Animation slide = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide);
                newItem.startAnimation(slide);
//                int listViewMiddleY = (mDrawerList.getBottom() - mDrawerList.getTop()) / 2;
//                TranslateAnimation slide = new TranslateAnimation(0,0,0,  listViewMiddleY - newItem.getBottom());
//                slide.setDuration(500);
//                slide.setFillAfter(false);
                slide.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mDrawerAdapter = new DrawerAdapter(MainActivity.this, placeManager.getPlaces());
                        mDrawerList.setAdapter(mDrawerAdapter);

                        mAddButton.setVisibility(View.VISIBLE);
                        findViewById(R.id.new_item).setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        mListPreviewFragment = (ListPreviewFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mListPreviewFragment == null) {
            mListPreviewFragment = new ListPreviewFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, mListPreviewFragment);
            ft.commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PlaceManager.getInstance(this).clear();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !mDrawerToggle.isDrawerIndicatorEnabled()) {
            onBackPressed();
            showHomeAsUp(false);
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mListPreviewFragment != null) {
            mListPreviewFragment.onBackPressed();
        }
    }

    private void setActionBarSelector(){
        final TransportManager transportManager = new TransportManager(this);

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new SelectorAdapter (
                getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                transportManager.getTransportOptions());

        getActionBar().setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long l) {
                transportManager.setMeanOfTransport(position);
                return true;
            }
        });
    }

    public void showHomeAsUp(boolean show) {
        mDrawerToggle.setDrawerIndicatorEnabled(!show);
        mDrawerToggle.syncState();
//        getActionBar().setDisplayHomeAsUpEnabled(show);
    }

}
