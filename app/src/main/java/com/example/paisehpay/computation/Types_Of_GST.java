package com.example.paisehpay.computation;

public abstract class Types_Of_GST {
    double gst_percent;
    Types_Of_GST(double percent){
        this.gst_percent = percent;
    }
    abstract double calc_gst_percentage();
}
