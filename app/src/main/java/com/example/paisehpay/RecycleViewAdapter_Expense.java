package com.example.paisehpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter_Expense extends RecyclerView.Adapter<RecycleViewAdapter_Expense.MyViewHolder> {
    Context context;
    ArrayList<Expense> expenseArray;


    public RecycleViewAdapter_Expense(Context context, ArrayList<Expense> expenseArray){
        this.context = context;
        this.expenseArray = expenseArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Expense.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expense_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Expense.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Expense.MyViewHolder holder, int position) {
        //assign values to the views
        holder.expenseTitleText.setText(expenseArray.get(position).getExpenseTitle());
        holder.expenseDateText.setText(expenseArray.get(position).getExpenseDate());
        holder.expensePaidByText.setText(expenseArray.get(position).getExpensePaidBy());
        holder.expenseActionText.setText(expenseArray.get(position).getExpenseAction());
        holder.expenseAmountText.setText(expenseArray.get(position).getExpenseAmount());

    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return expenseArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView expenseTitleText;
        TextView expenseDateText;
        TextView expensePaidByText;
        TextView expenseActionText;
        TextView expenseAmountText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseTitleText = itemView.findViewById(R.id.expense_title);
            expenseDateText = itemView.findViewById(R.id.expense_date);
            expensePaidByText = itemView.findViewById(R.id.expense_paid_by);
            expenseActionText = itemView.findViewById(R.id.expense_action);
            expenseAmountText = itemView.findViewById(R.id.expense_amount);
        }
    }

}
