package me.slinng.tribusevent.event;

import me.slinng.tribusevent.miscelleanous.TimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class EventOccasion {


    private LocalDateTime localDateTime;


    public EventOccasion(String formatTime) { this.localDateTime = LocalDateTime.parse(LocalDate.now().toString() + "T" + formatTime); }

    public boolean isEventReadyToStart() { return LocalDateTime.now().isAfter(localDateTime); }



    public String getDuration() { return TimeFormat.formated(ChronoUnit.SECONDS.between(LocalDateTime.now(), localDateTime)); }

    public void update() { localDateTime = localDateTime.plusDays(1); }





}
