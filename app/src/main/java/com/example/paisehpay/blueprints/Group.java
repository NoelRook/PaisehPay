package com.example.paisehpay.blueprints;

import java.util.HashMap;
import java.util.Map;

public class Group {
    //expense object used in recycleview of grouphomepage

    private String groupId;
    private String groupName;
    private String groupCreatedDate;
    private String groupAmount;
    private String createdBy;
    private HashMap<String, String> members;
    private boolean isSelected;
    public Group() {}
    public Group(String id, String groupName, String groupCreatedDate, String groupAmount, String createdBy,HashMap<String, String> members) {
        this.groupId = id;
        this.groupName = groupName;
        this.groupCreatedDate = groupCreatedDate;
        this.groupAmount = groupAmount;
        this.createdBy = createdBy;
        this.members = members;
        this.isSelected = false;
    }
    //todo implement builder for this in the future

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

    public HashMap<String, String> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, String> members) {
        this.members = members;
    }
    public boolean containsUser(String userId) {
        return members != null && members.containsValue(userId);
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupName", groupName);
        result.put("groupCreatedDate", groupCreatedDate);
        result.put("groupAmount", groupAmount);
        result.put("createdBy", createdBy);
        result.put("members", members);
        return result;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}