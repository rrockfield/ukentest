package com.uken.rockfield.question3;

import java.util.ArrayList;
import java.util.List;

class TimeRange {

    int start;
    int end;
    private List<TimeRange> children;

    TimeRange(String range) {
        int dash = range.indexOf("-");
        start = parse(range.substring(0, dash));
        end = parse(range.substring(dash + 1));
    }

    TimeRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    private int parse(String time) {
        int colon = time.indexOf(":");
        int hours = Integer.parseInt(time.substring(0, colon));
        int mins = Integer.parseInt(time.substring(colon + 1));
        return (hours * 60) + mins;
    }

    @Override
    public String toString() {
        if (end > start) {
            StringBuilder sb = new StringBuilder();
            if (children != null) {
                printChildren(sb);
            } else {
                printThisRange(sb);
            }
            return sb.toString();
        }
        return "";
    }

    private void printChildren(StringBuilder sb) {
        children.forEach((child) -> {
            String string = child.toString();
            if (!string.isEmpty() && sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(child);
        });
    }

    private void printThisRange(StringBuilder sb) {
        sb.append(start / 60).append(":").append(parseMins(start));
        sb.append("-");
        sb.append(end / 60).append(":").append(parseMins(end));
    }

    private String parseMins(int time) {
        int mins = time % 60;
        if (mins < 10) {
            return "0" + mins;
        }
        return Integer.toString(mins);
    }

    void substract(TimeRange subtrahend) {
        if (this.start >= subtrahend.end) {
            return;
        }
        if (this.end <= subtrahend.start) {
            return;
        }
        if (children == null) {
            susbtractFromThisRange(subtrahend);
        } else {
            children.forEach((child) -> {
                child.substract(subtrahend);
            });
        }
    }

    private void susbtractFromThisRange(TimeRange subtrahend) {
        if (this.start >= subtrahend.start) {
            this.start = subtrahend.end;
        } else {
            if (this.end <= subtrahend.end) {
                this.end = subtrahend.start;
            } else {
                split(subtrahend);
            }
        }
    }

    private void split(TimeRange subtrahend) {
        children = new ArrayList<>();
        children.add(new TimeRange(this.start, subtrahend.start));
        children.add(new TimeRange(subtrahend.end, this.end));
    }
}
