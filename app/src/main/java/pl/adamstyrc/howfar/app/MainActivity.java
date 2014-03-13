package pl.adamstyrc.howfar.app;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

    private PlaceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Place> places = new ArrayList<Place>();
        Place place = new Place("Dom");
        place.setDistance("15km");
        place.setTime("5 min");
        places.add(place);

        place = new Place("Praca");
        place.setDistance("3km");
        place.setTime("1 min");
        places.add(place);

        place = new Place("Asiula");
        place.setDistance("50km");
        place.setTime("1 h 10 min");
        places.add(place);

        mAdapter = new PlaceAdapter(this, places);
        setListAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
