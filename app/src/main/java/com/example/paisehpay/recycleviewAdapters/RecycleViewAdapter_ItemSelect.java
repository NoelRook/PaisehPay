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
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.User;

import java.util.ArrayList;

public class RecycleViewAdapter_ItemSelect extends RecyclerView.Adapter<RecycleViewAdapter_ItemSelect.MyViewHolder> {

    Context context;
    ArrayList<Item> itemArray;
    private final RecycleViewInterface recycleViewInterface;


    public RecycleViewAdapter_ItemSelect(Context context, ArrayList<Item> itemArray, RecycleViewInterface recycleViewInterface){
        this.context = context;
        this.itemArray = itemArray;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_ItemSelect.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.person_recycle_view_row,parent,false);

        return new RecycleViewAdapter_ItemSelect.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_ItemSelect.MyViewHolder holder, int position) {
        //assign values to the views
        holder.nameText.setText(itemArray.get(position).getItemName());
        if(itemArray.get(position).isSelected()){
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
        return itemArray.size();
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
