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
import com.example.paisehpay.activities.ReceiptOverview;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.computation.ReceiptInstance;

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
        holder.priceText.setText(itemArray.get(position).getItemPriceString());
        holder.peopleText.setText(itemArray.get(position).getItemPeople());
        if (recycleViewInterface != null){
            holder.deleteItemButton.setVisibility(View.GONE);
            holder.itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        recycleViewInterface.onButtonClick(pos);
                    }
                }
            });
        } else {
            holder.peopleText.setVisibility(View.GONE);
            holder.itemButton.setVisibility(View.GONE);
            holder.deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    if (position >= 0 && position < itemArray.size()) {
                        itemArray.remove(position);


                        ReceiptInstance.getInstance().removeFromReceipt(position);
                        if (context instanceof ReceiptOverview){
                        ((ReceiptOverview) context).updateReceiptComputation();
                        }
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, itemArray.size());
                    }
                }
            });
        }

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
        ImageButton deleteItemButton;

        public MyViewHolder(@NonNull View itemView,RecycleViewInterface recycleViewInterface) {
            super(itemView);

            nameText = itemView.findViewById(R.id.item_name);
            priceText = itemView.findViewById(R.id.item_price);
            peopleText =itemView.findViewById(R.id.item_people);
            itemButton = itemView.findViewById(R.id.add_people_button);
            deleteItemButton = itemView.findViewById(R.id.delete_item);

        }
    }


}
