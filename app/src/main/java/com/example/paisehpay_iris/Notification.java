package com.example.paisehpay_iris;

public class Notification {

    //class that
    //1. queries from db you owe ppl how much and from what grp
    //2. update msg from string.xml, either you paid xxx $yyy or xxx person nudge you to pay $yyy
    //3. auto updates if u pay someone (NOT SOMEONE PAY SOMEONE ELSE OR SOMEONE PAY U) or person tell u to pay
    //4. put in a recylable view format
    //https://www.youtube.com/watch?v=Mc0XT58A1Z4&t=27s for guide
    //nvm i go do myself bruh no one doing

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
