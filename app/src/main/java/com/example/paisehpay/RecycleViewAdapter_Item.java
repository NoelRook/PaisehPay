package com.example.paisehpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter_Item extends RecyclerView.Adapter<RecycleViewAdapter_Item.MyViewHolder> {

    Context context;
    ArrayList<Item> itemArray;

    private final RecycleViewInterface recycleViewInterface;


    public RecycleViewAdapter_Item(Context context, ArrayList<Item> itemArray, RecycleViewInterface recycleViewInterface){
        this.context = context;
        this.itemArray = itemArray;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Item.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.items_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Item.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Item.MyViewHolder holder, int position) {
        //assign values to the views
        holder.nameText.setText(itemArray.get(position).getItemName());
        holder.priceText.setText(itemArray.get(position).getItemPrice());
        holder.peopleText.setText(itemArray.get(position).getItemPeople());
        holder.itemButton.setOnClickListener(v -> {
            if (recycleViewInterface != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    recycleViewInterface.onButtonClick(pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return itemArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView nameText;
        TextView priceText;
        TextView peopleText;

        ImageButton itemButton;
        public MyViewHolder(@NonNull View itemView,RecycleViewInterface recycleViewInterface) {
            super(itemView);

            nameText = itemView.findViewById(R.id.item_name);
            priceText = itemView.findViewById(R.id.item_price);
            peopleText =itemView.findViewById(R.id.item_people);
            itemButton = itemView.findViewById(R.id.add_people_button);

        }
    }

    public void updateItem(int position, String newName){
        itemArray.get(position).setItemPeople(newName);
        notifyItemChanged(position);
    }

}
