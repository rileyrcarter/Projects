package com.example.csci310eventme;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventUnitTests extends Event{

    //create event for testing purposes
    Date date = new Date(05, 24, 2000);
    Time time = new Time("PM", 6, 00);
    Event event = new Event(1, "Potluck", (float)123.000, (float)123.000, date, time, 20, "USC", "Bring food!", "Food", "no parking", 20, "USC Village");

    @Test
    public void valid_get_eventId()
    {
        assertEquals(1, event.getEventId());
    }

    @Test
    public void valid_get_eventName()
    {
        assertEquals("Potluck", event.getEventName());
    }

    @Test
    public void valid_get_event_date()
    {
        assertEquals(date, event.getDate());
    }

    @Test
    public void valid_get_event_time()
    {
        assertEquals(time, event.getTime());
    }

    @Test
    public void valid_set_get_event_cost()
    {
        event.setCost(40);
        assertEquals(40, event.getCost());
    }

    @Test
    public void valid_set_get_event_sponsor()
    {
        event.setSponsor("UCLA");
        assertEquals("UCLA", event.getSponsor());
    }
    @Test
    public void valid_set_get_event_description()
    {
        event.setDescription("Bring A LOT of food!");
        assertEquals("Bring A LOT of food!", event.getDescription());
    }
}
