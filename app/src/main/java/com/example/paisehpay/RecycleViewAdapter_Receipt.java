package com.example.paisehpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter_Receipt extends RecyclerView.Adapter<RecycleViewAdapter_Receipt.MyViewHolder> {

    Context context;
    ArrayList<Receipt> receiptArray;


    public RecycleViewAdapter_Receipt(Context context, ArrayList<Receipt> receiptArray){
        this.context = context;
        this.receiptArray = receiptArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Receipt.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reciept_recycle_view_row,parent,false);
        return new RecycleViewAdapter_Receipt.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Receipt.MyViewHolder holder, int position) {
        //assign values to the views
        holder.itemNameText.setText(receiptArray.get(position).getItemName());
        holder.itemNumberText.setText(receiptArray.get(position).getItemNumber());
        holder.itemPriceText.setText(receiptArray.get(position).getItemPrice());


    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return receiptArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView itemNameText;
        TextView itemNumberText;
        TextView itemPriceText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

           itemNameText = itemView.findViewById(R.id.item_name);
           itemNumberText = itemView.findViewById(R.id.item_number);
           itemPriceText = itemView.findViewById(R.id.item_price);

        }
    }

}
