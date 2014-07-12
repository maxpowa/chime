package com.maxpowa.chime.util;

public class Notification {

    public String title;
    public String desc;
    public int unread = 1;
    public NotificationType type = NotificationType.MESSAGE;
    
    public Notification(String title, String desc, int unread, NotificationType type) {
        this.title = title;
        this.desc = desc;
        this.unread = unread;
        this.type = type;
    }

    public enum NotificationType {
        MESSAGE, FRIENDREQUEST, ONLINE, OFFLINE, STATUS
    }
}

