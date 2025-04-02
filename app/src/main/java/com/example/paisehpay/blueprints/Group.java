package com.example.paisehpay.blueprints;

public class Group {
    //expense object used in recycleview of grouphomepage

    String groupName;
    String groupCreatedDate;
    String groupAmount;

    public Group(String groupName, String groupCreatedDate, String groupAmount){
        this.groupName = groupName;
        this.groupCreatedDate = groupCreatedDate;
        this.groupAmount = groupAmount;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupCreatedDate() {
        return groupCreatedDate;
    }

    public String getGroupAmount() {
        return groupAmount;
    }
}