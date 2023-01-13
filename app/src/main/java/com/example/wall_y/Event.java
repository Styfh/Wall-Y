package com.example.wall_y;

import java.util.Date;

public class Event {

    private String userId;
    private Date eventDate;
    private String eventName;
    private boolean isDeduct;
    private int repeat; // 0 = one-time, 1 = weekly, 2 = monthly, 3 = annual

    public Event(String userId, Date eventDate, String eventName, boolean isDeduct, int repeat) {
        this.userId = userId;
        this.eventDate = eventDate;
        this.eventName = eventName;
        this.isDeduct = isDeduct;
        this.repeat = repeat;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean isDeduct() {
        return isDeduct;
    }

    public void setDeduct(boolean deduct) {
        isDeduct = deduct;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventDate=" + eventDate +
                ", eventName='" + eventName + '\'' +
                ", isDeduct=" + isDeduct +
                ", repeat=" + repeat +
                '}';
    }
}
