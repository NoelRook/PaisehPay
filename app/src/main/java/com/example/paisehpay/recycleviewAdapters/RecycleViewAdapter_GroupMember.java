package com.example.paisehpay.recycleviewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.GroupMember;

import java.util.ArrayList;

public class RecycleViewAdapter_GroupMember extends RecyclerView.Adapter<RecycleViewAdapter_GroupMember.MyViewHolder> {

    Context context;
    ArrayList<GroupMember> groupMemberArray;


    public RecycleViewAdapter_GroupMember(Context context, ArrayList<GroupMember> groupMemberArray){
        this.context = context;
        this.groupMemberArray = groupMemberArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_GroupMember.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.group_member_recycle_view_row,parent,false);

        return new RecycleViewAdapter_GroupMember.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_GroupMember.MyViewHolder holder, int position) {
        //assign values to the views
        holder.nameText.setText(groupMemberArray.get(position).getName());
        holder.emailText.setText(groupMemberArray.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return groupMemberArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView nameText;
        TextView emailText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.member_name);
            emailText = itemView.findViewById(R.id.member_email);
        }
    }

}
