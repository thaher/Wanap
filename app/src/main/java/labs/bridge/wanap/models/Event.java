package labs.bridge.wanap.models;

import java.io.Serializable;

public class Event implements Serializable {
    private String id;
    private String eventName;
    private String eventDescription;
    private String timeStamp;
    private String eventActivity;


    public Event() {
    }

    public Event(String eventName, String eventDescription, String timeStamp, String eventActivity) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.timeStamp = timeStamp;
        this.eventActivity = eventActivity;
    }

    public String getId() {
            if(id==null) return "0";
            else
                return id;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEventActivity() {
        return eventActivity;
    }

    public void setEventActivity(String eventActivity) {
        this.eventActivity = eventActivity;
    }
}
