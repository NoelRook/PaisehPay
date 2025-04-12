package com.example.paisehpay.recycleviewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.DebtPersonHelper;
import com.example.paisehpay.sessionHandler.PreferenceManager;

import java.util.ArrayList;

public class RecycleViewAdapter_ExpenseItems extends RecyclerView.Adapter<RecycleViewAdapter_ExpenseItems.MyViewHolder> {

    Context context;
    ArrayList<DebtPersonHelper> personArray;
    PreferenceManager preferenceManager;


    public RecycleViewAdapter_ExpenseItems(Context context, ArrayList<DebtPersonHelper> personArray){
        this.context = context;
        this.personArray = personArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_ExpenseItems.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.debt_recycle_view_row,parent,false);
        preferenceManager = new PreferenceManager(context);
        return new RecycleViewAdapter_ExpenseItems.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_ExpenseItems.MyViewHolder holder, int position) {
        //assign values to the views

        DebtPersonHelper person = personArray.get(position);
        String userId = person.getUserId();
        String username = preferenceManager.getOneFriend(userId);
        holder.nameText.setText(username + " owes");

        holder.priceText.setText(Double.toString(person.getAmount()));



    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return personArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView nameText;
        TextView priceText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.person_name);
            priceText = itemView.findViewById(R.id.person_debt);

        }
    }


}
