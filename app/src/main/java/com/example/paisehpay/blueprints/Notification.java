package com.example.paisehpay.blueprints;

public class Notification {
    //notification reycleview on notification fragment

    String groupName;
    String notificationString;


    public Notification(String groupName, String notificationString) {
        this.groupName = groupName;
        this.notificationString = notificationString;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getNotificationString() {
        return notificationString;
    }
}


