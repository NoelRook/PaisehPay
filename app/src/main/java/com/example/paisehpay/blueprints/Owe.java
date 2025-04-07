package com.example.paisehpay.blueprints;

import java.util.Date;

public class Owe {
    //used in recycleview owe/owed in homefragment after pressing view details buttons
    String groupName;
    String person;
    String amount;

    private Date date;
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

    public Date getDate(){ return this.date;}

    public Double getDoubleAmount(){
        return Double.parseDouble(getAmount().replace("$", ""));
    }
}
