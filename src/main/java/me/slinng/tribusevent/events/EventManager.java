package me.slinng.tribusevent.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventManager {

    private final List<Event> events = new ArrayList<>();


    public EventManager() {
    }

    public Event fetchEventByID(int id ) {
        return events.stream().filter(event -> event.getID() == id).findFirst().orElse(null);
    }
    public Event fetchEventByName(String name) {
        return events.stream().filter(event -> event.getEventName().equals(name)).findFirst().orElse(null);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void unregisterEvent(Event event) {
        events.remove(event);
    }
    public void registerEvent(Event event) {
        events.add(event);
    }
}
