package com.example.paisehpay.recycleviewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.User;

import java.util.ArrayList;

public class RecycleViewAdapter_UserSelect extends RecyclerView.Adapter<RecycleViewAdapter_UserSelect.MyViewHolder> {
    //RecycleView Adapter when selecting users to associate with certain items in AddPeople.java
    private Context context;
    private ArrayList<User> userArray;
    private final RecycleViewInterface recycleViewInterface;

    //default initialization
    public RecycleViewAdapter_UserSelect(Context context, ArrayList<User> userArray, RecycleViewInterface recycleViewInterface){
        this.context = context;
        this.userArray = userArray;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_UserSelect.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.person_recycle_view_row,parent,false);

        return new RecycleViewAdapter_UserSelect.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_UserSelect.MyViewHolder holder, int position) {
        //assign values to the views
        holder.nameText.setText(userArray.get(position).getUsername());
        //this allows the user to select and deselect users and when doing so, user's icons would update accordingly
        if(userArray.get(position).isSelected()){
            holder.groupButton.setImageResource(R.drawable.added_icon);
            holder.groupButton.setSelected(false);
        } else {
            holder.groupButton.setImageResource(R.drawable.add_icon);
        }
        holder.groupButton.requestLayout();;
    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return userArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate
        TextView nameText;
        ImageButton groupButton;
        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);

           nameText = itemView.findViewById(R.id.person_name);
           groupButton = itemView.findViewById(R.id.add_button);
           groupButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (recycleViewInterface != null){
                       int position = getAdapterPosition();

                       if (position != RecyclerView.NO_POSITION){
                           recycleViewInterface.onButtonClick(position);
                       }
                   }
               }
           });

        }
    }
}
