package me.slinng.tribusevent.event;

import me.slinng.tribusevent.event.events.lms.LMS;

public class EventController {

    private final me.slinng.tribusevent.event.events.lms.LMS LMS;

    public EventController() {
        this.LMS = new LMS();
    }

    public LMS getLMS() {
        return LMS;
    }
}
