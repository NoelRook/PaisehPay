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
import java.util.List;

public class RecycleViewAdapter_Item extends RecyclerView.Adapter<RecycleViewAdapter_Item.MyViewHolder> {
    //RecycleView Adapter used to display items in ReceiptOverview
    private Context context;
    private ArrayList<Item> itemArray;
    private final RecycleViewInterface recycleViewInterface;
    private String queryFrom;


    //default initialization
    public RecycleViewAdapter_Item(Context context, ArrayList<Item> itemArray, RecycleViewInterface recycleViewInterface, String queryFrom){
        this.context = context;
        this.itemArray = itemArray;
        this.recycleViewInterface = recycleViewInterface;
        this.queryFrom = queryFrom;
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
        holder.peopleText.setText(itemArray.get(position).getItemPeopleString());

        //since we are using the Item RecycleView multiple times but showcasing different attributes of class Item each time
        //we will use a common layout but hide some of the widgets based on where we are instantiating from
        //we will also need a way to differentiate where we are instantiating from
        if (queryFrom.equals("AddPeople")){
            //if we are adding people to items on AddPeople.java, RecycleView -> DialogFragment -> RecycleView
            //thus we require recycleViewInterface
            //We don't want users to delete items, only add people
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
        } else if (queryFrom.equals("ReceiptOverview")) {
            //if we are adding items on ReceiptOverview.java, DialogFragment -> RecycleView
            //we don't need recycleViewInterface, and we can't have users add people
            holder.peopleText.setVisibility(View.GONE);
            holder.itemButton.setVisibility(View.GONE);
            holder.deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    //remove item if its a valid postion
                    if (position >= 0 && position < itemArray.size()) {
                        itemArray.remove(position);
                        //get the instance of the receipt, then delete item
                        ReceiptInstance.getInstance().removeFromReceipt(position);
                        //check if the RecycleView exists in the ReceiptOverview page
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
    // Call this method to update the list
    public void setItems(List<Item> newItems) {
        this.itemArray.clear();
        this.itemArray.addAll(newItems);
        notifyDataSetChanged();  // Notify that data has changed
    }



}
