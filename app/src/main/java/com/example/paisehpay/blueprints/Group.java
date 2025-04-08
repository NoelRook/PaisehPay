package com.example.paisehpay.blueprints;

import java.util.HashMap;
import java.util.Map;

public class Group {
    //expense object used in recycleview of grouphomepage

    String groupId;
    String groupName;
    String groupCreatedDate;
    String groupAmount = "No expenses recorded yet";
    private HashMap<String, String> peopleInvolved; // Changed to HashMap to match your structure
    public Group() {}
    public Group(String id, String groupName, String groupCreatedDate, String groupAmount, HashMap<String, String> peopleInvolved) {
        this.groupId = id;
        this.groupName = groupName;
        this.groupCreatedDate = groupCreatedDate;
        this.groupAmount = groupAmount;
        this.peopleInvolved = peopleInvolved;
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

    public HashMap<String, String> getPeopleInvolved() {
        return peopleInvolved;
    }

    public void setPeopleInvolved(HashMap<String, String> peopleInvolved) {
        this.peopleInvolved = peopleInvolved;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupName", groupName);
        result.put("groupCreatedDate", groupCreatedDate);
        result.put("groupAmount", groupAmount);
        result.put("peopleInvolved", peopleInvolved);
        return result;
    }
    public boolean containsUser(String userId) {
        return peopleInvolved != null && peopleInvolved.containsValue(userId);
    }
}