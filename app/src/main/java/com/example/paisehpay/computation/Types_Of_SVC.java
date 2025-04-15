package com.example.paisehpay.computation;

public abstract class Types_Of_SVC {
    double sct_percent;
    //constructor
    Types_Of_SVC(double percent){
        this.sct_percent = percent;
    }

    //must implement this method in TEN_SVC
    abstract double calc_sct_percentage();
}

