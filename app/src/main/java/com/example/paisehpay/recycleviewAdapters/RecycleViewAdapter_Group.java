package com.example.paisehpay.recycleviewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.activities.GroupHomepage;
import com.example.paisehpay.blueprints.Group;

import java.util.ArrayList;

public class RecycleViewAdapter_Group extends RecyclerView.Adapter<RecycleViewAdapter_Group.MyViewHolder> {
    Context context;
    ArrayList<Group> groupArray;



    public RecycleViewAdapter_Group(Context context, ArrayList<Group> groupArray){
        this.context = context;
        this.groupArray = groupArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Group.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.group_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Group.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Group.MyViewHolder holder, int position) {
        //assign values to the views
        holder.groupNameText.setText(groupArray.get(position).getGroupName());
        holder.groupDateText.setText(groupArray.get(position).getGroupCreatedDate());
        holder.groupAmountText.setText(groupArray.get(position).getGroupAmount());

        holder.groupHomepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context1 = view.getContext();
                Intent intent = new Intent(context1,GroupHomepage.class);
                intent.putExtra("GROUP_ID", groupArray.get(holder.getAdapterPosition()).getGroupId());
                context1.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return groupArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView groupNameText;
        TextView groupDateText;
        TextView groupAmountText;
        Button groupHomepageButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            groupNameText = itemView.findViewById(R.id.group_name);
            groupDateText= itemView.findViewById(R.id.group_date);
            groupAmountText = itemView.findViewById(R.id.group_amount);
            groupHomepageButton = itemView.findViewById(R.id.group_homepage);
        }
    }

}
