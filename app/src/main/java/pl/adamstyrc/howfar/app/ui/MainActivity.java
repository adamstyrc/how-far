package pl.adamstyrc.howfar.app.ui;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import pl.adamstyrc.howfar.app.R;
import pl.adamstyrc.howfar.app.TransportManager;
import pl.adamstyrc.howfar.app.adapters.SelectorAdapter;


public class MainActivity extends FragmentActivity {

    private static final String LIST_FRAGMENT = "list fragment";
    private static final String PREVIEW_FRAGMENT = "preview fragment";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean mIsPhone;
    private View mListLayout;
    private View mPreviewLayout;

    private ListFragment mListFragment;
    private PlaceMapPreviewFragment mPreviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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

        setListPreview(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        PlaceManager.getInstance(this).clear();
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    private void setListPreview(Bundle savedInstanceState) {
        mIsPhone = findViewById(R.id.right) == null;

        if (mIsPhone) {
            mListLayout = findViewById(R.id.left);
            mPreviewLayout = findViewById(R.id.left);
        } else {
            mListLayout = findViewById(R.id.left);
            mPreviewLayout = findViewById(R.id.right);
        }

        if (savedInstanceState == null) {
            mListFragment = new PlaceTimetableFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            mPreviewFragment = new PlaceMapPreviewFragment();
            ft.add(mListLayout.getId(), mListFragment, LIST_FRAGMENT)
                    .add(mPreviewLayout.getId(), mPreviewFragment, PREVIEW_FRAGMENT);
            if (mIsPhone) {
                ft.hide(mPreviewFragment);
            }
            ft.commit();
        } else {
            mListFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT);
            mPreviewFragment = (PlaceMapPreviewFragment) getSupportFragmentManager().findFragmentByTag(PREVIEW_FRAGMENT);

            if (mIsPhone && getSupportFragmentManager().getBackStackEntryCount() == 0) {
                getSupportFragmentManager().beginTransaction().hide(mPreviewFragment).commit();
            }
        }
    }

    public void showMap(int placeId) {
        mPreviewFragment.showPlace(placeId);

        if (mIsPhone) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.show(mPreviewFragment)
                    .remove(mListFragment)
                    .addToBackStack(null).commit();

            showHomeAsUp(true);
        }
    }


    public GoogleMap getMap() {
        return ((SupportMapFragment) mPreviewFragment).getMap();
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
