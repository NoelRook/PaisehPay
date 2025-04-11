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

import java.util.ArrayList;

public class RecycleViewAdapter_SettleUser extends RecyclerView.Adapter<RecycleViewAdapter_SettleUser.MyViewHolder> {

    Context context;
    ArrayList<DebtPersonHelper> personArray;


    public RecycleViewAdapter_SettleUser(Context context, ArrayList<DebtPersonHelper> personArray){
        this.context = context;
        this.personArray = personArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_SettleUser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.debt_recycle_view_row,parent,false);

        return new RecycleViewAdapter_SettleUser.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_SettleUser.MyViewHolder holder, int position) {
        //assign values to the views
        DebtPersonHelper person = personArray.get(position);
        holder.nameText.setText(person.getUserId());
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
