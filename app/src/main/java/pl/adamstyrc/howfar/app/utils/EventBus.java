package pl.adamstyrc.howfar.app.utils;

import com.squareup.otto.Bus;

public class EventBus extends Bus {

    private static EventBus sEventBus;

    private EventBus() {};

    public static synchronized EventBus getInstance() {
        if (sEventBus == null) {
            sEventBus = new EventBus();
        }

        return sEventBus;
    }
}
