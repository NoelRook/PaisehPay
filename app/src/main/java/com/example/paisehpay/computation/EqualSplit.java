package com.example.paisehpay.computation;

import android.util.Log;

import com.example.paisehpay.blueprints.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class EqualSplit implements Split{
    public EqualSplit(){

    }
    @Override
    public void splitTotal(Item item) {
        ArrayList<String> people = item.getItemPeopleArray();

        BigDecimal price = BigDecimal.valueOf(item.getItemPrice());
        BigDecimal numberOfPeople = BigDecimal.valueOf(people.size());
        BigDecimal indivPrice = price.divide(numberOfPeople, 2, RoundingMode.HALF_UP);

        item.setItemIndividualPrice(indivPrice.doubleValue());

        //checker
        for (String person : people) {
            Log.d("EqualSplit", person + " owes $" + indivPrice);
        }
    }
}
