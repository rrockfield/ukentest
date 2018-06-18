package com.uken.rockfield.question3;

import org.junit.Assert;
import org.junit.Test;

public class TimeRangeTest {
    
    @Test
    public void testTimeRanges() {
        Assert.assertEquals("0:00-24:00",new TimeRange("0:00-24:00").toString());
        Assert.assertEquals("",new TimeRange("1:00-1:00").toString());
    }
    
}
