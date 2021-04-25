package me.slinng.tribusevent.events;

import java.util.Arrays;

public enum EventState {

    UNREACHABLE, WAITING, STARTING, RUNNING;



    public EventState fetchByTag(String tag) {
        return Arrays.stream(EventState.values()).filter(es -> es.name().equalsIgnoreCase(tag)).findFirst().orElse(null);
    }
}
