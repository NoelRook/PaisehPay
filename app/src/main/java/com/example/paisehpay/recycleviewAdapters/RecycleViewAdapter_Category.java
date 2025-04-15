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
import com.example.paisehpay.blueprints.Category;

import java.util.ArrayList;

public class RecycleViewAdapter_Category extends RecyclerView.Adapter<RecycleViewAdapter_Category.MyViewHolder> {
     //RecycleView adapter for category.
    private Context context;
    private ArrayList<Category> categoryArray; //used to store the RecycleView objects
    private int selectedPosition = RecyclerView.NO_POSITION; // Stores the selected button index
    private String selectedCategoryName = null; //default value
    private RecycleViewListener listener;


    //default initialization
    public RecycleViewAdapter_Category(Context context, ArrayList<Category> categoryArray,RecycleViewListener listener){
        this.context = context;
        this.categoryArray = categoryArray;
        this.listener = listener;
    }


    //inflate the layout
    @NonNull
    @Override
    public RecycleViewAdapter_Category.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Category.MyViewHolder(view);
    }

    //used to assign values to the items
    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Category.MyViewHolder holder, int position) {
        Category category = categoryArray.get(position);
        holder.nameText.setText(category.getCategoryName());

        // Set the image for the current item based on selection
        if (holder.getAdapterPosition() == selectedPosition) {
            holder.categoryButton.setImageResource(R.drawable.added_icon);  // Image for selected state
        } else {
            holder.categoryButton.setImageResource(category.getCategoryIcon());  // Default image in category view
        }

         //when user selects a category, it will reflect to show the category selected. the user can unselect and select
        holder.categoryButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            // If the clicked item is already selected, unselect it
            if (currentPosition == selectedPosition) {
                selectedPosition = -1; // Unselect the item
                selectedCategoryName = null;
                listener.onSelected(null);
            } else {
                int previousSelected = selectedPosition;
                selectedPosition = currentPosition;
                selectedCategoryName = categoryArray.get(currentPosition).getCategoryName();
                notifyItemChanged(previousSelected); // Unselect previous
                notifyItemChanged(currentPosition);
                if (listener != null) {
                    listener.onSelected(selectedCategoryName); //
                }
            }
        });

    }

    //get item count to display in RecycleView
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
