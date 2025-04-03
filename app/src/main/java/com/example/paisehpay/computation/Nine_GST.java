package com.example.paisehpay.computation;

public class Nine_GST extends Types_Of_GST {
    public Nine_GST(){
        super(9.0);

    }

    @Override
    public double calc_gst_percentage(){
        return this.gst_percent/100.0;
    }
}

