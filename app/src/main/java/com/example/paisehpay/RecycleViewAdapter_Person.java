package com.example.paisehpay;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter_Person extends RecyclerView.Adapter<RecycleViewAdapter_Person.MyViewHolder> {

    Context context;
    ArrayList<Person> personArray;
    private final com.example.paisehpay.RecycleViewInterface recycleViewInterface;


    public RecycleViewAdapter_Person(Context context, ArrayList<Person> personArray, RecycleViewInterface recycleViewInterface){
        this.context = context;
        this.personArray = personArray;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Person.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.person_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Person.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Person.MyViewHolder holder, int position) {
        //assign values to the views
        holder.nameText.setText(personArray.get(position).getPersonName());
        if(personArray.get(position).isSelected()){
            holder.groupButton.setImageResource(R.drawable.added_icon);
        } else {
            holder.groupButton.setImageResource(R.drawable.add_icon);
        }
        holder.groupButton.requestLayout();;


    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return personArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView nameText;
        ImageButton groupButton;
        public MyViewHolder(@NonNull View itemView, com.example.paisehpay.RecycleViewInterface recycleViewInterface) {
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
