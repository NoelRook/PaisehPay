package com.example.paisehpay_iris;

public class Owe {
    String groupName;
    String person;
    String amount;
    public Owe(String groupName,String person, String amount){
        this.groupName = groupName;
        this.person = person;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getPerson() {
        return person;
    }
}
