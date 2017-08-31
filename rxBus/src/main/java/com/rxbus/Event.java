package com.rxbus;


public class Event {
    private Object event;
    private String tag;

    public Event(Object event, String tag) {
        this.event = event;
        this.tag = tag;
    }

    public Object getEvent() {
        return event;
    }

    public String getTag() {
        return tag;
    }
}
