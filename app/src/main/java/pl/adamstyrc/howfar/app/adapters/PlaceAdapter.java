package pl.adamstyrc.howfar.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.adamstyrc.howfar.app.models.Place;
import pl.adamstyrc.howfar.app.R;


public class PlaceAdapter extends ArrayAdapter<Place> {

    public PlaceAdapter(Context context, List<Place> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);

        Place place = getItem(position);
        TextView nameText = (TextView) view.findViewById(R.id.name);
        nameText.setText(place.getName());

        if (place.getTime() != null) {
            TextView distanceText = (TextView) view.findViewById(R.id.distance);
            distanceText.setText(place.getDistance());

            TextView timeText = (TextView) view.findViewById(R.id.time);
            timeText.setText(place.getTime());
        }


        return view;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
