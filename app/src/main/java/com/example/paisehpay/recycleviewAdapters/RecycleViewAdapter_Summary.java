package com.example.paisehpay.recycleviewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Summary;

import java.util.ArrayList;

public class RecycleViewAdapter_Summary extends RecyclerView.Adapter<RecycleViewAdapter_Summary.MyViewHolder> {

    Context context;
    ArrayList<Summary> summaryArray;
    private int selectedPosition = RecyclerView.NO_POSITION; // Stores the selected button index


    public RecycleViewAdapter_Summary(Context context, ArrayList<Summary> summaryArray){
        this.context = context;
        this.summaryArray = summaryArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Summary.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.summary_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Summary.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Summary.MyViewHolder holder, int position) {
        //assign values to the views
        holder.nameText.setText(summaryArray.get(position).getSummaryText());
    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return summaryArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView nameText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.you_paid);
        }
    }

}
