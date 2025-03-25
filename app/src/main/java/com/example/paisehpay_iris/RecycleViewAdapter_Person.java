package com.example.paisehpay_iris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter_Person extends RecyclerView.Adapter<RecycleViewAdapter_Person.MyViewHolder> {

    Context context;
    ArrayList<Person> personArray;


    public RecycleViewAdapter_Person(Context context, ArrayList<Person> personArray){
        this.context = context;
        this.personArray = personArray;
    }

    @NonNull
    @Override
    public RecycleViewAdapter_Person.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where we inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.person_recycle_view_row,parent,false);

        return new RecycleViewAdapter_Person.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter_Person.MyViewHolder holder, int position) {
        //assign values to the views
        holder.nameText.setText(personArray.get(position).getPersonName());


    }

    @Override
    public int getItemCount() {
        //number of items want displayed
        return personArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //recycle view oncreate

        TextView nameText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

           nameText = itemView.findViewById(R.id.person);

        }
    }

}
