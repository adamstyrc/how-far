package pl.adamstyrc.howfar.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pl.adamstyrc.howfar.app.R;
import pl.adamstyrc.howfar.app.TransportManager;

public class SelectorAdapter extends ArrayAdapter<String> {

    public SelectorAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        switch (position) {
            case TransportManager.WALK:
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.walk, 0, 0, 0);
                break;
            case TransportManager.BIKE:
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bike, 0, 0, 0);
                break;
            case TransportManager.CAR:
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.car, 0, 0, 0);
                break;
        }

        return view;
    }
}
