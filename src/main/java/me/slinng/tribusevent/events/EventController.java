package me.slinng.tribusevent.events;

public class EventController {

    private final LMS LMS;

    public EventController() {
        this.LMS = new LMS();
    }

    public LMS getLMS() {
        return LMS;
    }
}
