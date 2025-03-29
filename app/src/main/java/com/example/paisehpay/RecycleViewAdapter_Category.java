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

public class RecycleViewAdapter_Category extends RecyclerView.Adapter<RecycleViewAdapter_Category.MyViewHolder> {
     //adapter for category recycle view

    Context context;
    ArrayList<Category> categoryArray;
    private int selectedPosition = RecyclerView.NO_POSITION; // Stores the selected button index


    public RecycleViewAdapter_Category(Context context, ArrayList<Category> categoryArray){
        this.context = context;
        this.categoryArray = categoryArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Category.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Category.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Category.MyViewHolder holder, int position) {
        Category category = categoryArray.get(position);
        holder.nameText.setText(category.getCategoryName());

        // Set the image for the current item based on selection
        if (holder.getAdapterPosition() == selectedPosition) {
            holder.categoryButton.setImageResource(R.drawable.added_icon);  // Image for selected state
        } else {
            holder.categoryButton.setImageResource(category.getCategoryIcon());  // Default image
        }

        // Set click listener for the ImageButton
        holder.categoryButton.setOnClickListener(v -> {
            // If the clicked item is already selected, unselect it
            if (holder.getAdapterPosition() == selectedPosition) {
                selectedPosition = -1; // Unselect the item
            } else {
                selectedPosition = holder.getAdapterPosition() ; // Select the new item
            }
            notifyItemChanged(holder.getAdapterPosition()); // Update the item at the given position
            if (selectedPosition != -1 && selectedPosition != holder.getAdapterPosition()) {
                // If another item was selected, notify that item as well
                notifyItemChanged(selectedPosition);
            } // Refresh the RecyclerView to update the images
        });
    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return categoryArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView nameText;
        ImageButton categoryButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.category_name);
            categoryButton = itemView.findViewById(R.id.category_button);
        }
    }


}
