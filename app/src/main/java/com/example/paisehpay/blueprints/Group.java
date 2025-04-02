package com.example.paisehpay.blueprints;

public class Group {
    //expense object used in recycleview of grouphomepage

    String groupName;
    String groupCreatedDate;
    String groupAmount = "No expenses recorded yet";

    public Group(String groupName, String groupCreatedDate){
        this.groupName = groupName;
        this.groupCreatedDate = groupCreatedDate;
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

    public void setGroupAmount(String groupAmount){
        this.groupAmount = groupAmount;
    }
}