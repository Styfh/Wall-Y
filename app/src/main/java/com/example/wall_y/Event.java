package com.example.wall_y;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Event {

    private String userId;
    private Timestamp eventDate;
    private String eventName;
    private boolean isDeduct;
    private int amount;
    private int repeat; // 0 = one-time, 1 = weekly, 2 = monthly, 3 = annual

    public Event(String userId, Timestamp eventDate, String eventName, boolean isDeduct, int amount, int repeat) {
        this.userId = userId;
        this.eventDate = eventDate;
        this.eventName = eventName;
        this.isDeduct = isDeduct;
        this.amount = amount;
        this.repeat = repeat;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public void setEventDate(Timestamp eventDate) {
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
