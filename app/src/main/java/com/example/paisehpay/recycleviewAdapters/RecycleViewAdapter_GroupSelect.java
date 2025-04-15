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
import com.example.paisehpay.blueprints.Group;
import java.util.ArrayList;

public class RecycleViewAdapter_GroupSelect extends RecyclerView.Adapter<RecycleViewAdapter_GroupSelect.MyViewHolder> {
    //RecycleView Adapter used when selecting group to add expense to in AddPeople

    private Context context;
    private ArrayList<Group> groupArray;
    private final RecycleViewInterface recycleViewInterface;

    //default initialization
    public RecycleViewAdapter_GroupSelect(Context context, ArrayList<Group> groupArray, RecycleViewInterface recycleViewInterface){
        this.context = context;
        this.groupArray = groupArray;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_GroupSelect.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.person_recycle_view_row,parent,false);
        return new RecycleViewAdapter_GroupSelect.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_GroupSelect.MyViewHolder holder, int position) {
        //assign values to the views
        //user can select a certain group and their corresponding icon would update
        holder.nameText.setText(groupArray.get(position).getGroupName());
        if(groupArray.get(position).isSelected()){
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
        return groupArray.size();
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
