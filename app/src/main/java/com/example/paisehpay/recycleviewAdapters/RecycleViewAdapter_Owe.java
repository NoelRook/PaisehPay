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
import com.example.paisehpay.blueprints.Owe;
import java.util.ArrayList;

public class RecycleViewAdapter_Owe extends RecyclerView.Adapter<RecycleViewAdapter_Owe.MyViewHolder> {
    //RecycleView adapter used to display Owed(expenses) in OweFragment
    private Context context;
    private ArrayList<Owe> oweArray;


    //default initialization
    public RecycleViewAdapter_Owe(Context context, ArrayList<Owe> oweArray){
        this.context = context;
        this.oweArray = oweArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Owe.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.owe_recycler_view_row,parent,false);
        return new RecycleViewAdapter_Owe.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Owe.MyViewHolder holder, int position) {
        //assign values to the views
        holder.groupText.setText(oweArray.get(position).getGroupName());
        holder.personText.setText(oweArray.get(position).getPerson());
        holder.amountText.setText(oweArray.get(position).formatAsDollars());
    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return oweArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView personText;
        TextView groupText;
        TextView amountText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            personText = itemView.findViewById(R.id.person_text);
            groupText = itemView.findViewById(R.id.group_text);
            amountText = itemView.findViewById(R.id.amount_text);
        }
    }

    //function to update oweArray with a entirely new arraylist
    public void updateOweArray(ArrayList<Owe> oweArray){
        this.oweArray = oweArray;
        notifyDataSetChanged();
        Log.d("update adapter", "after sorting: "+ oweArray.toString());
    }

}
