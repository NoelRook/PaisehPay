package com.example.paisehpay.computation;

public abstract class Types_Of_SVC {
    double sct_percent;
    Types_Of_SVC(double percent){
        this.sct_percent = percent;
    }
    abstract double calc_sct_percentage();
}

