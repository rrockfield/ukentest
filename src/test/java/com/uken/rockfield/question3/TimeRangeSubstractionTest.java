package com.uken.rockfield.question3;

import org.junit.Assert;
import org.junit.Test;

public class TimeRangeSubstractionTest {

    @Test
    public void substractAllTest() {
        executeTest("(9:00-10:00)",
                "(9:00-10:00)",
                "()");
    }

    @Test
    public void substractARangeTest() {
        executeTest("(9:00-10:00)",
                "(9:00-9:30)",
                "(9:30-10:00)");
    }

    @Test
    public void substractOutsideRangeTest() {
        executeTest("(9:00-9:30)",
                "(9:30-15:00)",
                "(9:00-9:30)");
    }

    @Test
    public void substractFromRangeArrayTest() {
        executeTest("(9:00-9:30, 10:00-10:30)",
                "(9:15-10:15)",
                "(9:00-9:15, 10:15-10:30)");
    }

    @Test
    public void fullTest() {
        executeTest("(9:00-11:00, 13:00-15:00)",
                "(9:00-9:15, 10:00-10:15, 12:30-16:00)",
                "(9:15-10:00, 10:15-11:00)");
    }

    @Test
    public void MultiSplitTest() {
        executeTest("(0:00-12:00)",
                "(1:00-2:00, 3:00-4:00, 5:00-6:00, 7:00-8:00, 9:00-10:00)",
                "(0:00-1:00, 2:00-3:00, 4:00-5:00, 6:00-7:00, 8:00-9:00, 10:00-12:00)");
    }

    public void executeTest(String minuend, String subtrahend, String expectedDifference) {
        String difference = TimeRangeSubstraction.substraction(minuend, subtrahend);
        Assert.assertEquals(expectedDifference, difference);
    }
}
