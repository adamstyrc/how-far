package pl.adamstyrc.howfar.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class TransportManager {

    public static final int WALK = 0;
    public static final int BIKE = 1;
    public static final int CAR = 2;

    private static final String PREFERENCE = "Preference";
    private static final String MEAN_OF_TRANSPORT = "mean of transport";
    private final String[] mTransportOptions;

    private SharedPreferences mSharedPreferences;


    public TransportManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        mTransportOptions = context.getResources().getStringArray(R.array.transport_options);
    }

    public void setMeanOfTransport(int meanOfTransport) {
        mSharedPreferences.edit().putInt(MEAN_OF_TRANSPORT, meanOfTransport).commit();
    }

    public int getMeanOfTransport() {
        return mSharedPreferences.getInt(MEAN_OF_TRANSPORT, WALK);
    }

    public String[] getTransportOptions() {
        return mTransportOptions;
    }
}
