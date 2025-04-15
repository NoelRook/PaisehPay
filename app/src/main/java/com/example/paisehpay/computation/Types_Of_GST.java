package com.example.paisehpay.computation;

public abstract class Types_Of_GST {
    double gst_percent;
    //constructor
    Types_Of_GST(double percent){
        this.gst_percent = percent;
    }

    //must implement this method in nine gst
    abstract double calc_gst_percentage();
}
