package com.example.csci310eventme;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void badBirthdayTest_ReturnsTrue() {
        User testUser = new User("testing11", "testUser", "testFirst", "testLast", "0505");
        assertEquals(testUser.getBirthdayFormat(testUser.getBirthday()), "0505");
    }

    @Test
    public void birthdayFormat1_ReturnsTrue() {
        User testUser = new User("testing11", "testUser", "testFirst", "testLast", "05052002");
        assertEquals(testUser.getBirthdayFormat(testUser.getBirthday()), "May 5, 2002");
    }

    @Test
    public void birthdayFormat2_ReturnsTrue() {
        User testUser = new User("testing11", "testUser", "testFirst", "testLast", "12252002");
        assertEquals(testUser.getBirthdayFormat(testUser.getBirthday()), "December 25, 2002");
    }

    @Test
    public void validDateTest_ReturnsTrue() {
        Date test = new Date(20,  "November", 2022, 11);
        Date begTest = new Date(3, "April", 2022, 4);
        Date endTest = new Date(25, "December", 2022, 12);
        assertTrue(test.validDate(begTest, endTest));
    }

    @Test
    public void validMinute_ReturnsTrue() {
        Time test = new Time("PM", 4, 0);
        assertEquals(test.addZeroToMinute(test.getMinute()), "00");
    }

}

