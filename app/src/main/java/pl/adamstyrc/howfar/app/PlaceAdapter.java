package pl.adamstyrc.howfar.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


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

        TextView valueText = (TextView) view.findViewById(R.id.value);
        valueText.setText(place.getDistance() + "(" + place.getTime() + ")");

        return view;
    }
}
