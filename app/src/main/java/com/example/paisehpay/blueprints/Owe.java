package com.example.paisehpay.blueprints;

import java.util.Date;
public class Owe {
    //used in recycler view owe/owed in home fragment after pressing view details buttons
    String groupName;
    String person; //is this id or username? @faust
    Double amount;
    Date date;

    // constructor
    public Owe(String groupName,String person, Double amount, Date date){
        this.groupName = groupName;
        this.person = person;
        this.amount = amount;
        this.date = date;
    }

    // getters
    public Double getAmount() {
        return amount;
    }
    public String getGroupName() {
        return groupName;
    }
    public String getPerson() {
        return person;
    }

    public Date getDate(){ return this.date;}

    // function
    public String formatAsDollars() { //  format amount as dollar currency string
        return String.format("$%.2f", this.amount);
    }

}
