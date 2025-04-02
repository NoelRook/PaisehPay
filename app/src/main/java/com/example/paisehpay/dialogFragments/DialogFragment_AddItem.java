package com.example.paisehpay.dialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Item;

import java.util.ArrayList;

public class DialogFragment_AddItem extends androidx.fragment.app.DialogFragment{
    //popup class when u press create group

    View rootView;
    EditText itemNameText;
    EditText itemPriceText;
    Button confirmButton;
    DialogFragmentListener listener;

    ArrayList<String> itemArray = new ArrayList<>();
    ArrayList<Double> itemPriceArray = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        itemNameText = rootView.findViewById(R.id.item_name_input);
        itemPriceText = rootView.findViewById(R.id.item_price_input);

        confirmButton = rootView.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemNameText == null || itemPriceText == null) {
                    Log.e("DialogFragment", "EditTexts are null");
                    return;
                }
                String itemName = itemNameText.getText().toString().trim();
                String itemPrice = itemPriceText.getText().toString().trim();
                String itemPeople = "Add people to item";


                //stuff data into array for computation
                itemArray.add(itemName);
                //itemPriceArray.add(itemPrice);



                //populate recycleview
                if (itemPrice.isEmpty()){
                    Toast.makeText(getActivity(),"never input price bro",Toast.LENGTH_LONG).show();
                } else if (itemName.isEmpty()){
                    Toast.makeText(getActivity(),"never input name bro", Toast.LENGTH_LONG).show();
                } else if (listener != null){
                    Log.d("DialogFragment",  itemName + " : " + itemPrice);
                    Item item = new Item(itemName,itemPrice,itemPeople);
                    listener.onDataSelected(-3,item);
                    dismiss();
                }

            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogFragmentListener");
        }
    }

    // <!--

}

