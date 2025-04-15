package com.example.paisehpay.recycleviewAdapters;

import android.content.Context;
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
import java.util.Locale;

public class RecycleViewAdapter_ExpenseItems extends RecyclerView.Adapter<RecycleViewAdapter_ExpenseItems.MyViewHolder> {
    //RecycleView adapter for all ExpenseItems, which is the inner RecycleView in ExpenseDescription.java that displays the users who are involved in that item
    Context context;
    ArrayList<DebtPersonHelper> personArray;
    PreferenceManager preferenceManager;



    //default initialization
    public RecycleViewAdapter_ExpenseItems(Context context, ArrayList<DebtPersonHelper> personArray){
        this.context = context;
        this.personArray = personArray;
    }

    //where we inflate the layout
    @NonNull
    @Override
    public RecycleViewAdapter_ExpenseItems.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.debt_recycle_view_row,parent,false);
        preferenceManager = new PreferenceManager(context);
        return new RecycleViewAdapter_ExpenseItems.MyViewHolder(view);
    }

    //assign values to the views
    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_ExpenseItems.MyViewHolder holder, int position) {
        //for users who are involved in the expense, retrieve their username and amount owed to populate the RecycleVoew
        DebtPersonHelper person = personArray.get(position);
        String userId = person.getUserId();
        String username = preferenceManager.getOneFriend(userId);
        holder.nameText.setText(String.format("%s owes",username));
        holder.priceText.setText(String.format(Locale.ENGLISH,"%s",person.getAmount()));
    }

    //number of items want displayed
    @Override
    public int getItemCount() {
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
