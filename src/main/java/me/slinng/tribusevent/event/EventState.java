package me.slinng.tribusevent.event;

import java.util.Arrays;

public enum EventState {

    UNREACHABLE(""), WAITING("I Lobby"), STARTING("Startar"), RUNNING("Körs");


    private final String text;
    EventState(String paramText) {
        this.text = paramText;
    }

    public String getText() {
        return text;
    }

    public EventState fetchByTag(String tag) {
        return Arrays.stream(EventState.values()).filter(es -> es.name().equalsIgnoreCase(tag)).findFirst().orElse(null);
    }
}
