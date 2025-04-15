package com.example.paisehpay.recycleviewAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.paisehpay.R;
import com.example.paisehpay.activities.GroupHomepage;
import com.example.paisehpay.activities.SettleUp;
import com.example.paisehpay.blueprints.User;
import java.util.ArrayList;

public class RecycleViewAdapter_GroupMember extends RecyclerView.Adapter<RecycleViewAdapter_GroupMember.MyViewHolder> {

    private Context context;
    private ArrayList<User> groupMemberArray;
    private String queryFrom;
    private String groupID;
    private final RecycleViewListener recycleViewListener;

    //default initialization
    public RecycleViewAdapter_GroupMember(Context context, ArrayList<User> groupMemberArray, String queryFrom, String groupID,RecycleViewListener recycleViewListener){
        this.context = context;
        this.groupMemberArray = groupMemberArray;
        this.queryFrom = queryFrom;
        this.groupID = groupID;
        this.recycleViewListener = recycleViewListener;
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
        User user = groupMemberArray.get(position);
        //if the adapter is being used in GroupSettings.java
        if (queryFrom.equals("GroupSettings") || queryFrom.equals("FriendsFragment")){
            //user can only delete group members
            holder.nameText.setText(user.getUsername());
            holder.emailText.setText(user.getEmail());
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    //remove item if its a valid position
                    if (position >= 0 && position < groupMemberArray.size()) {
                        groupMemberArray.remove(position);
                        notifyItemRemoved(position);

                        if(recycleViewListener != null){
                            recycleViewListener.onSelected(user.getId());
                        }
                        notifyItemRangeChanged(position, groupMemberArray.size());
                    }
                }
            });
        }
        //if the adapter is being used in GroupHomepage.java
        else if (queryFrom.equals("GroupHomepage")){
            //user can settle up with the corresponding user, pressing delete button would lead to SettleUp.java
            holder.nameText.setText(user.getUsername());
            holder.emailText.setVisibility(View.GONE);
            holder.deleteButton.setText(R.string.settle);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SettleUp.class);
                    intent.putExtra("FRIEND_ID", user.getId());
                    intent.putExtra("GROUP_ID",groupID);
                    context.startActivity(intent);
                    if (context instanceof GroupHomepage) {
                        ((GroupHomepage) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
            });

        }
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
        Button deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.member_name);
            emailText = itemView.findViewById(R.id.member_email);
            deleteButton = itemView.findViewById(R.id.member_status);
        }
    }

}
