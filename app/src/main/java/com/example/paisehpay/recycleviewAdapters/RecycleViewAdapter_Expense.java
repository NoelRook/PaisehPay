package com.example.paisehpay.recycleviewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.activities.GroupHomepage;
import com.example.paisehpay.activities.SettleUp;
import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.tabBar.ExpenseFragment;

import java.util.ArrayList;

public class RecycleViewAdapter_Expense extends RecyclerView.Adapter<RecycleViewAdapter_Expense.MyViewHolder> {
    private Context context;
    ArrayList<Expense> expenseArray;
    private String expenseId;



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

        return new RecycleViewAdapter_Expense.MyViewHolder(view,context,expenseId);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Expense.MyViewHolder holder, int position) {
        //assign values to the views
        Expense currentExpense = expenseArray.get(position);
        holder.expenseTitleText.setText(expenseArray.get(position).getDescription());
        holder.expenseDateText.setText(expenseArray.get(position).getExpenseDate());
        holder.expensePaidByText.setText(expenseArray.get(position).getExpensePaidBy());
        holder.expenseActionText.setText(expenseArray.get(position).getExpenseAction());
        holder.expenseAmountText.setText(expenseArray.get(position).getExpenseAmount());

        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, SettleUp.class);
            intent.putExtra("EXPENSE_ID", currentExpense.getexpenseId());
            context.startActivity(intent);

            if (context instanceof GroupHomepage) {
                ((GroupHomepage) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });








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
        public MyViewHolder(@NonNull View itemView,Context context,String expenseId) {
            super(itemView);


            expenseTitleText = itemView.findViewById(R.id.expense_title);
            expenseDateText = itemView.findViewById(R.id.expense_date);
            expensePaidByText = itemView.findViewById(R.id.expense_paid_by);
            expenseActionText = itemView.findViewById(R.id.expense_action);
            expenseAmountText = itemView.findViewById(R.id.expense_amount);

        }
    }

    public void updateData(ArrayList<Expense> newData) {
        this.expenseArray = newData;
    }

}
