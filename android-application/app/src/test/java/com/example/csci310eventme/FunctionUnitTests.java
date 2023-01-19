package com.example.csci310eventme;

import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionUnitTests {
    @Test
    public void correctTimeFormat_ReturnsTrue() {
        Time testTime = new Time("am", 9, 30);
        assertEquals(testTime.toString(), "9:30 am");
    }

    @Test
    public void addZeroToMinuteTest() {
        Time testTime = new Time("am", 4, 0);
        assertEquals(testTime.toString(), "4:00 am");
    }

    @Test
    public void addZeroToHourTest() {
        Time testTime = new Time("pm", 7, 0);
        assertEquals(testTime.toStringHour(), "07:00 pm");
    }

    @Test
    public void reduceHoursTest() {
        Time testTime = new Time("pm", 36, 0);

        assertEquals(testTime.toStringHour(), "12:00 pm");
    }

    @Test
    public void reduceMinTest() {
        Time testTime = new Time("pm", 7, 120);
        assertEquals(testTime.toStringMin(), "07:00 pm");
    }


}
