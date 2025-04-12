package com.example.paisehpay.recycleviewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.DebtPersonHelper;
import com.example.paisehpay.blueprints.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class RecycleViewAdapter_ExpenseDescription extends RecyclerView.Adapter<RecycleViewAdapter_ExpenseDescription.MyViewHolder> {

    Context context;
    ArrayList<Item> itemArray;


    public RecycleViewAdapter_ExpenseDescription(Context context, ArrayList<Item> itemArray){
        this.context = context;
        this.itemArray = itemArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_ExpenseDescription.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_description_recycle_view_row,parent,false);

        return new RecycleViewAdapter_ExpenseDescription.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull RecycleViewAdapter_ExpenseDescription.MyViewHolder holder, int position) {
        Item item = itemArray.get(position);

        holder.nameText.setText(item.getItemName());
        holder.priceText.setText(item.getItemPriceString());

        ArrayList<DebtPersonHelper> localDebtList = new ArrayList<>();
        if (item.getDebtPeople() != null) {
            for (Map.Entry<String, Double> entry : item.getDebtPeople().entrySet()) {
                String username = entry.getKey(); //need replace with username, current user id
                Double individualPrice = BigDecimal.valueOf(entry.getValue()).setScale(3,BigDecimal.ROUND_UP).doubleValue();
                localDebtList.add(new DebtPersonHelper(username,individualPrice));
            }
        }

        RecycleViewAdapter_ExpenseItems adapterSettleUser = new RecycleViewAdapter_ExpenseItems(context, localDebtList);
        holder.peopleView.setLayoutManager(new LinearLayoutManager(context));
        holder.peopleView.setAdapter(adapterSettleUser);
        //Log.d("debtList", "Item: " + item.getItemName() + ", Debtors: " + item.getDebtPeople());
    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return itemArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView nameText;
        TextView priceText;

        RecyclerView peopleView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.item_name);
            priceText = itemView.findViewById(R.id.item_price);
            peopleView = itemView.findViewById(R.id.settle_people_recycle_view_row);

        }
    }

}
