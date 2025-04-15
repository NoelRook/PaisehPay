package com.example.paisehpay.dialogFragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.paisehpay.R;
import com.example.paisehpay.activities.ReceiptOverview;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.computation.ReceiptInstance;


public class DialogFragment_AddItem extends androidx.fragment.app.DialogFragment{
    //popup class when u press create group

    View rootView;
    EditText itemNameText;
    EditText itemPriceText;
    Button confirmButton;
    DialogFragmentListener<Item> listener;




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
                String itemName = itemNameText.getText().toString().trim(); //process textview
                String itemPrice = itemPriceText.getText().toString().trim();


                //if user clicks confirm
                if (itemPrice.isEmpty()){
                    Toast.makeText(getActivity(),"never input price bro",Toast.LENGTH_LONG).show();
                } else if (itemName.isEmpty()){
                    Toast.makeText(getActivity(),"never input name bro", Toast.LENGTH_LONG).show();
                } else if (listener != null){
                    //if the DialogFragmentListener is attached properly to ReceiptOverview
                    //we will add to the Item RecycleView
                    Log.d("DialogFragment",  itemName + " : " + itemPrice);

                    //we create a new Item
                    Item item = new Item(null, itemName,Double.parseDouble(itemPrice),null, null, null);

                    //get the receipt instance
                    ReceiptInstance.getInstance().addToReceipt(item);
                    //call the update computation method
                    ((ReceiptOverview) requireActivity()).updateReceiptComputation();
                    //implement DialogFragmentListener's method so to attach the data back to ReceiptOverview
                    listener.onDataSelected(0,item);
                    dismiss();
                }

            }
        });
        return rootView;
    }

    //set custom layout
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }




    //check if we are able to pass data from DialogFragment back to RecieptOverview page
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogFragmentListener<Item>) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogFragmentListener");
        }
    }


}

