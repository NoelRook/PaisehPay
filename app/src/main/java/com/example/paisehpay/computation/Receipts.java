package com.example.paisehpay.computation;

import java.util.ArrayList;

public class Receipts {

    private ArrayList<String> array_of_items;
    private ArrayList<Double> array_of_items_prices;
    private String subtotal;
    private String subtotal_gst="NA";
    private String subtotal_sct="NA";
    private String total;
    private boolean GST;
    private boolean SCT;
    private Types_Of_GST gst_amt;
    private Types_Of_SVC sct_amt;

    private Receipts(receipt_builder receiptBuilder){
        this.array_of_items = receiptBuilder.array_of_items;
        this.GST = receiptBuilder.GST;
        this.SCT = receiptBuilder.SCT;
        this.gst_amt = receiptBuilder.gst_amt;
        this.sct_amt = receiptBuilder.sct_amt;

    }
    public void calculate_receipt_subtotal(){
        double total_amount=0;
        for (int i=0;i<this.array_of_items.size();i++){
            total_amount+=array_of_items_prices.get(i);
        }
        this.subtotal = Double.toString(total_amount);
    }

    public void calculate_receipt_subtotal_gst(){
        if (this.GST){
            double result = Double.parseDouble(this.subtotal) * (this.gst_amt.calc_gst_percentage());
            this.subtotal_gst = Double.toString(result);
        }
    }

    public void calculate_receipt_subtotal_sct(){
        if(this.SCT){
            double result = Double.parseDouble(this.subtotal) * (this.sct_amt.calc_sct_percentage());
            this.subtotal_sct = Double.toString(result);
        }
    }

    public void calculate_receipt_total(){
        double subtotal_gst;
        double subtotal_sct;

        if(this.subtotal_gst.equals("NA")){
            subtotal_gst = 0.0;
        }
        else{
            subtotal_gst = Double.parseDouble(this.subtotal_gst);
        }
        if(this.subtotal_sct.equals("NA")){
            subtotal_sct = 0.0;
        }
        else{
            subtotal_sct = Double.parseDouble(this.subtotal_sct);
        }

        this.total = Double.toString(Math.round((Double.parseDouble(this.subtotal) + subtotal_gst+subtotal_sct)*100)/100.0);

    }

    public String getSubtotal(){
        return Double.toString(Math.round(Double.parseDouble(this.subtotal)*100)/100.0);
    }
    public String getSubtotal_gst(){
        if(this.subtotal_gst.equals("NA")){
            return this.subtotal_gst;
        }
        else{
            return Double.toString(Math.round(Double.parseDouble(this.subtotal_gst)*100)/100.0);
        }
    }

    public String getSubtotal_sct(){
        if(this.subtotal_sct.equals("NA")){
            return this.subtotal_sct;
        }
        else{
            return Double.toString(Math.round(Double.parseDouble(this.subtotal_sct)*100)/100.0);
        }
    }

    public String getTotal(){
        return this.total;
    }

    static class receipt_builder{
        private ArrayList<String> array_of_items;
        private ArrayList<Double> array_of_items_prices;
        private boolean GST = false;
        private boolean SCT = false;
        private Types_Of_GST gst_amt;
        private Types_Of_SVC sct_amt;

        receipt_builder(){}

        public receipt_builder setArray_Of_Items(ArrayList<String> arrayList){
            this.array_of_items = arrayList;
            return this;
        }

        public receipt_builder setArray_Of_Item_Prices(ArrayList<Double> arrayList){
            this.array_of_items_prices = arrayList;
            return this;
        }

        public receipt_builder setGST(boolean gst){
            this.GST = gst;
            return this;
        }

        public receipt_builder setSCT(boolean sct){
            this.SCT = sct;
            return this;
        }

        public receipt_builder set_gstamt(Types_Of_GST amt){
            this.gst_amt = amt;
            return this;
        }

        public receipt_builder set_sctamt(Types_Of_SVC amt){
            this.sct_amt = amt;
            return this;
        }

        public Receipts build(){
            return new Receipts(this);
        }

    }
}
