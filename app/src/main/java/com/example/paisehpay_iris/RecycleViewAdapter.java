package com.example.paisehpay_iris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Notification> notificationArray;


    public RecycleViewAdapter(Context context, ArrayList<Notification> notificationArray){
        this.context = context;
        this.notificationArray = notificationArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);

        return new RecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.MyViewHolder holder, int position) {
        //assign values to the views
        holder.groupText.setText(notificationArray.get(position).getGroupName());
        holder.notificationText.setText(notificationArray.get(position).getNotificationString());
    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return notificationArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView notificationText;
        TextView groupText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationText = itemView.findViewById(R.id.notification);
            groupText = itemView.findViewById(R.id.group_name);
        }
    }

}
