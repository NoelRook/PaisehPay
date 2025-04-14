package com.example.paisehpay.computation;

import com.example.paisehpay.blueprints.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Receipts {

    private ArrayList<String> array_of_items = new ArrayList<>();
    private ArrayList<Double> array_of_item_prices = new ArrayList<>();
    private String subtotal = "NA";
    private String subtotal_gst="NA";
    private String subtotal_sct="NA";
    private String total;
    private boolean GST = false;
    private boolean SCT = false;
    private Types_Of_GST gst_amt;
    private Types_Of_SVC sct_amt;

    public Receipts(){
    }

    public void clearAll(){
        array_of_items.clear();
        array_of_item_prices.clear();
    }

    //change item gst cost
    public void loadItemWithGST(){
        if (GST && SCT){ //have gst and sct
            for (int i = 0; i < array_of_item_prices.size();i++){
                double itemPrice = array_of_item_prices.get(i);
                BigDecimal bigDecimal = new BigDecimal(itemPrice*(1+0.09+0.1)).setScale(2, RoundingMode.HALF_UP);
                array_of_item_prices.set(i, bigDecimal.doubleValue());
            }
        }
        else if (GST){ //have gst
            for (int i = 0; i < array_of_item_prices.size();i++){
                double itemPrice = array_of_item_prices.get(i);
                BigDecimal bigDecimal = new BigDecimal(itemPrice*(1+0.09)).setScale(2, RoundingMode.HALF_UP);
                array_of_item_prices.set(i, bigDecimal.doubleValue());
            }
        }else if (SCT){ //have sct
            for (int i = 0; i < array_of_item_prices.size();i++){
                double itemPrice = array_of_item_prices.get(i);
                BigDecimal bigDecimal = new BigDecimal(itemPrice*(1+0.1)).setScale(2, RoundingMode.HALF_UP);
                array_of_item_prices.set(i, bigDecimal.doubleValue());
            }
        }
    }


    public void addToItemArray(String itemName){
        array_of_items.add(itemName);
    }
    public void addToItemPrice(Double itemPrice){
        array_of_item_prices.add(itemPrice);
    }

    //ADD FUNCTION
    public void addToReceipt(Item item){
        String itemName = item.getItemName();
        double itemPrice = item.getItemPrice();
        this.array_of_items.add(itemName);
        this.array_of_item_prices.add(itemPrice);
    }


    //DELETE FUNCTION
    public void removeFromReceipt(int position){ //ask IRIS how to get the position index out
        this.array_of_items.remove(position);
        this.array_of_item_prices.remove(position);
    }
    public void calculate_receipt_subtotal(){
        double total_amount=0;
        for (int i=0;i<this.array_of_items.size();i++){
            total_amount+=array_of_item_prices.get(i);
        }
        this.subtotal = Double.toString(total_amount);
    }

    public void calculate_receipt_subtotal_gst(){
        if (this.GST){
            double result = Double.parseDouble(this.subtotal) * (this.gst_amt.calc_gst_percentage());
            this.subtotal_gst = Double.toString(result);
        }
        else{
            this.subtotal_gst = "NA";
        }

    }

    public void calculate_receipt_subtotal_sct(){
        if(this.SCT){
            double result = Double.parseDouble(this.subtotal) * (this.sct_amt.calc_sct_percentage());
            this.subtotal_sct = Double.toString(result);
        }
        else{
            this.subtotal_sct = "NA";
        }
    }

    public void calculate_receipt_total(){
        double subtotal_gst;
        double subtotal_sct;

        if(this.subtotal_gst=="NA"){
            subtotal_gst = 0.0;
        }
        else{
            subtotal_gst = Double.parseDouble(this.subtotal_gst);
        }
        if(this.subtotal_sct=="NA"){
            subtotal_sct = 0.0;
        }
        else{
            subtotal_sct = Double.parseDouble(this.subtotal_sct);
        }

        this.total = Double.toString(Math.round((Double.parseDouble(this.subtotal) + subtotal_gst+subtotal_sct)*100)/100.0);

    }

    public String getSubtotal(){
        if(this.subtotal=="NA"){
            return this.subtotal;
        }
        return Double.toString(Math.round(Double.parseDouble(this.subtotal)*100)/100.0);
    }
    public String getSubtotal_gst(){
        if(this.subtotal_gst=="NA"){
            return this.subtotal_gst;
        }
        else{
            return Double.toString(Math.round(Double.parseDouble(this.subtotal_gst)*100)/100.0);
        }
    }

    public String getSubtotal_sct(){
        if(this.subtotal_sct=="NA"){
            return this.subtotal_sct;
        }
        else{
            return Double.toString(Math.round(Double.parseDouble(this.subtotal_sct)*100)/100.0);
        }
    }

    public String getTotal(){
        return this.total;
    }

    public ArrayList<Double> getArray_of_item_prices() {
        return array_of_item_prices;
    }

    public ArrayList<String> getArray_of_items() {
        return array_of_items;
    }

    public void setGST(boolean gst){
        this.GST = gst;
    }

    public void setSCT(boolean sct){
        this.SCT = sct;
    }

    public void set_gstamt(Types_Of_GST amt){
        this.gst_amt = amt;
    }

    public void set_sctamt(Types_Of_SVC amt){
        this.sct_amt = amt;
    }

}
