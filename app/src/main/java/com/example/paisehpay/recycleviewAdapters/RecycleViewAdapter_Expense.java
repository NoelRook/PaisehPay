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

import java.util.ArrayList;

public class RecycleViewAdapter_Expense extends RecyclerView.Adapter<RecycleViewAdapter_Expense.MyViewHolder> {
    private Context context;
    ArrayList<Expense> expenseArray;
    String groupId;



    public RecycleViewAdapter_Expense(Context context, ArrayList<Expense> expenseArray,String groupId){
        this.context = context;
        this.expenseArray = expenseArray;
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Expense.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expense_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Expense.MyViewHolder(view,context,groupId);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Expense.MyViewHolder holder, int position) {
        //assign values to the views
        Log.d("AdapterBind", "Binding expense: " + expenseArray.get(position).getDescription());
        holder.expenseTitleText.setText(expenseArray.get(position).getDescription());
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
        public MyViewHolder(@NonNull View itemView,Context context,String groupId) {
            super(itemView);


            expenseTitleText = itemView.findViewById(R.id.expense_title);
            expenseDateText = itemView.findViewById(R.id.expense_date);
            expensePaidByText = itemView.findViewById(R.id.expense_paid_by);
            expenseActionText = itemView.findViewById(R.id.expense_action);
            expenseAmountText = itemView.findViewById(R.id.expense_amount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SettleUp.class);
                    intent.putExtra("GROUP_ID",groupId);
                    context.startActivity(intent);

                    if (context instanceof GroupHomepage) {
                        ((GroupHomepage) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        ((GroupHomepage) context).finish();
                    }
                }
            });


        }
    }

    public void updateData(ArrayList<Expense> newData) {
        this.expenseArray = newData;
    }

}
