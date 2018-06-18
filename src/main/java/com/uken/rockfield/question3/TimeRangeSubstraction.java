package com.uken.rockfield.question3;

import java.util.*;

public class TimeRangeSubstraction {

    public static String substraction(String minuend, String subtrahend) {
        List<TimeRange> minuendRanges = transformToTimeRanges(minuend);
        List<TimeRange> subtrahendRanges = transformToTimeRanges(subtrahend);
        substract(minuendRanges, subtrahendRanges);
        return convertToString(minuendRanges);
    }

    private static List<TimeRange> transformToTimeRanges(String time) {
        String[] stringRanges = time.replace("(", "").replace(")", "").replace(" ", "").split(",");
        TimeRange[] timeRanges = new TimeRange[stringRanges.length];
        for (int i = 0; i < stringRanges.length; i++) {
            timeRanges[i] = new TimeRange(stringRanges[i]);
        }
        return new ArrayList(Arrays.asList(timeRanges));
    }

    private static void substract(List<TimeRange> minuendRanges, List<TimeRange> subtrahendRanges) {
        Iterator<TimeRange> minuendIterator = minuendRanges.iterator();
        while (minuendIterator.hasNext()) {
            TimeRange minuend = minuendIterator.next();
            Iterator<TimeRange> subtrahendIterator = subtrahendRanges.iterator();
            while (subtrahendIterator.hasNext()) {
                TimeRange subtrahend = subtrahendIterator.next();
                if (minuend.start >= subtrahend.end) {
                    // If the subtrahend range ends before the ordered minuend starts
                    // it can be removed from the subtrahend list, as it will end 
                    // before all other minuends
                    subtrahendIterator.remove();
                } else if (minuend.end > subtrahend.start) {
                    // If the current minuend ends before the subtrahend starts
                    // it cannot be substracted, so it will be ignored
                    minuend.substract(subtrahend);
                    if (minuend.start >= minuend.end) {
                        // If the start of the range is equal or bigger than the end of the range
                        // it means the range is invalid and it should be removed from the list
                        minuendIterator.remove();
                        break;
                    }
                }
            }
        }
    }

    private static String convertToString(List<TimeRange> difference) {
        StringBuilder sb = new StringBuilder("(");
        difference.forEach((time) -> {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(time);
        });
        return sb.append(")").toString();
    }
}
