package com.example.paisehpay.blueprints;

public class Group {
    //expense object used in recycleview of grouphomepage

    String groupId;
    String groupName;
    String groupCreatedDate;
    String groupAmount = "No expenses recorded yet";
    private boolean isSelected;

    public Group() {}
    public Group(String groupId,String groupName, String groupCreatedDate){
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCreatedDate = groupCreatedDate;
        this.isSelected = false;
    }

    public String getGroupId() {
        return groupId;
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

    public void setGroupId(String key) {
        this.groupId = key;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
