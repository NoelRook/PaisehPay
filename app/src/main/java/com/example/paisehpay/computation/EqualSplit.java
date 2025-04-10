package com.example.paisehpay.computation;

import android.util.Log;

import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class EqualSplit implements Split{ // implements a parent precent split to child equal split
    public EqualSplit(){

    }
//    @Override
//    public void splitTotal(Item item) {
//        ArrayList<String> people = item.getItemPeopleArray();
//
//        BigDecimal price = BigDecimal.valueOf(item.getItemPrice());
//        BigDecimal numberOfPeople = BigDecimal.valueOf(people.size());
//        BigDecimal indivPrice = price.divide(numberOfPeople, 2, RoundingMode.HALF_UP);
//
//        item.setItemIndividualPrice(indivPrice.doubleValue());
//
//        //checker
//        for (String person : people) {
//            Log.d("EqualSplit", person + " owes $" + indivPrice);
//        }
//    }
    @Override
    public void splitTotal(Item item) {
        if (item.getItemPeopleArray() != null && !item.getItemPeopleArray().isEmpty()) {
            BigDecimal splitAmount = BigDecimal.valueOf(item.getItemPrice() / Double.valueOf(item.getItemPeopleArray().size()));
            HashMap<String, Double> debts = new HashMap<>();

            for (User user : item.getItemPeopleArray()) {
                String userId = user.getId();
                debts.put(userId, (splitAmount).doubleValue());
            }

            item.setDebtPeople(debts);
        }
    }
}
