package com.example.paisehpay.recycleviewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.activities.ExpenseDescription;
import com.example.paisehpay.activities.GroupHomepage;
import com.example.paisehpay.activities.SettleUp;
import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.example.paisehpay.tabBar.ExpenseFragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class RecycleViewAdapter_Expense extends RecyclerView.Adapter<RecycleViewAdapter_Expense.MyViewHolder> {
    private Context context;
    ArrayList<Expense> expenseArray;
    private String queryFrom;
    private PreferenceManager preferenceManager;
    private final ActivityResultLauncher<Intent> launcher;



    public RecycleViewAdapter_Expense(Context context, ArrayList<Expense> expenseArray,String queryFrom,ActivityResultLauncher<Intent> launcher){
        this.context = context;
        this.expenseArray = expenseArray;
        this.queryFrom = queryFrom;
        this.launcher = launcher;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Expense.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expense_recycle_view_row,parent,false);

        preferenceManager = new PreferenceManager(context);

        return new RecycleViewAdapter_Expense.MyViewHolder(view,context,queryFrom);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Expense.MyViewHolder holder, int position) {
        //assign values to the views
        Expense currentExpense = expenseArray.get(position);
        holder.expenseTitleText.setText(currentExpense.getDescription());
        holder.expenseDateText.setText(currentExpense.getExpenseDate());

        // retrieve the category icon
        String category = currentExpense.getExpenseCategory();
        String[] categoryNames = context.getResources().getStringArray(R.array.category_name_array);
        TypedArray categoryIcons = context.getResources().obtainTypedArray(R.array.category_item_array);

        int index = -1;
        for (int i = 0; i < categoryNames.length; i++) {
            if (categoryNames[i].equalsIgnoreCase(category)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            int imageResId = categoryIcons.getResourceId(index, -1);
            holder.expenseCategoryImage.setImageResource(imageResId);
        } else {
            holder.expenseCategoryImage.setImageResource(R.drawable.nav_groups);
        }
        categoryIcons.recycle();

        //set name of person paid
        String userId = currentExpense.getExpensePaidBy();
        String username = preferenceManager.getOneFriend(userId);
        holder.expensePaidByText.setText(username + " paid");


        if (queryFrom.equals("SettleUp")){
            if (!Objects.equals(userId, preferenceManager.getUser().getId())){
                holder.expenseActionText.setText(R.string.you_borrowed);
            } else{
                holder.expenseActionText.setText(R.string.you_lent);
            }

            BigDecimal bigDecimal = BigDecimal.valueOf(currentExpense.getExpenseOwed()).setScale(2, RoundingMode.HALF_UP);
            holder.expenseAmountText.setText(String.format(Locale.ENGLISH,"$ %s",bigDecimal));


        }else if (queryFrom.equals("ExpenseFragment")) {

            holder.expenseActionText.setText(R.string.amount);
            holder.expenseAmountText.setText("$ " + currentExpense.getExpenseAmount());

            holder.itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, ExpenseDescription.class);
                intent.putExtra("EXPENSE_ID", currentExpense.getExpenseId());
                launcher.launch(intent);

                if (context instanceof GroupHomepage) {
                    ((GroupHomepage) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        }








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
        ImageView expenseCategoryImage;
        public MyViewHolder(@NonNull View itemView,Context context,String expenseId) {
            super(itemView);


            expenseTitleText = itemView.findViewById(R.id.expense_title);
            expenseDateText = itemView.findViewById(R.id.expense_date);
            expensePaidByText = itemView.findViewById(R.id.expense_paid_by);
            expenseActionText = itemView.findViewById(R.id.expense_action);
            expenseAmountText = itemView.findViewById(R.id.expense_amount);
            expenseCategoryImage = itemView.findViewById(R.id.category_icon);

        }
    }

    public void updateData(ArrayList<Expense> newData) {
        this.expenseArray = newData;
    }

}
