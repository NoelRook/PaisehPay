package com.example.paisehpay.computation;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateBase {
    //for date formatting, receives string date and returns date date
    //parent class of DateOwed and DateDebt
    public Date dateformatting(String strdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(strdate);
        } catch (ParseException e) {
            Log.e("DateDebt", "Error parsing date: " + strdate, e);
            return new Date(); // Return current date as fallback
        }
    }
}
