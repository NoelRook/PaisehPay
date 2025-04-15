package com.example.paisehpay.computation;

public class Ten_SVC extends Types_Of_SVC{
    //service charge is 10 percent
    public Ten_SVC(){
        super(10);
    }
    @Override
    //calculate service charge percentage
    public double calc_sct_percentage(){
        return this.sct_percent/100;
    }
}

