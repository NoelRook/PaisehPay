package com.example.paisehpay.computation;

public class Ten_SVC extends Types_Of_SVC{
    public Ten_SVC(){
        super(10);
    }
    @Override
    public double calc_sct_percentage(){
        return this.sct_percent/100;
    }
}

