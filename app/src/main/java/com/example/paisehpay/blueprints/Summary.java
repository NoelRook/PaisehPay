package com.example.paisehpay.blueprints;

public class Summary {
    //used in summary recycle view
    String avatar;
    String summaryText;

    public Summary(String summaryText){
        this.summaryText = summaryText;
    }

    public String getSummaryText() {
        return summaryText;
    }
}
