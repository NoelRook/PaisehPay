package com.example.paisehpay.blueprints;

public class Notification {
    // used to populate notification recycler view on notification fragment
    // + used for notification recycler view adapter
    String groupName;
    String notificationString;

    // constructor
    public Notification(String groupName, String notificationString) {
        this.groupName = groupName;
        this.notificationString = notificationString;
    }

    // getters
    public String getGroupName() {
        return groupName;
    }
    public String getNotificationString() {
        return notificationString;
    }
}


