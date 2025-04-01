package com.example.paisehpay_iris;

public class Person {
    //class used when loading up both select group /select person in addpeople page (or expense details)
    //since layout same

    private String personName;
    private boolean isSelected;

    public Person(String personName){
        this.personName = personName;
        this.isSelected = false;
    }

    public String getPersonName() {
        return personName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
