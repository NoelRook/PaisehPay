package com.example.paisehpay;

public class Person {
    //class used when loading up both select group /select person in addpeople page (or expense details)
    //since layout same

    String personName;

    public Person(String personName){
        this.personName = personName;
    }

    public String getPersonName() {
        return personName;
    }
}
