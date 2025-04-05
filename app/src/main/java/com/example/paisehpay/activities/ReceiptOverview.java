package com.example.paisehpay.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paisehpay.R;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.computation.Nine_GST;
import com.example.paisehpay.computation.ReceiptInstance;
import com.example.paisehpay.computation.Receipts;
import com.example.paisehpay.computation.Ten_SVC;
import com.example.paisehpay.computation.Types_Of_GST;
import com.example.paisehpay.computation.Types_Of_SVC;
import com.example.paisehpay.dialogFragments.DialogFragmentListener;
import com.example.paisehpay.dialogFragments.DialogFragment_AddItem;
import com.example.paisehpay.dialogFragments.DialogFragment_SelectGroup;
import com.example.paisehpay.recycleviewAdapters.RecycleViewAdapter_Item;

import java.util.ArrayList;

public class ReceiptOverview extends AppCompatActivity implements DialogFragmentListener<Item> {
    //shows receipt details after camera ocr
    ImageView backArrow;
    Button addPeopleButton;
    TextView toolbarTitleText;
    RecyclerView itemView;
    ArrayList<Item> itemArray = new ArrayList<>();
    Button addItemButton;
    DialogFragment_AddItem addItemFragment;
    private RecycleViewAdapter_Item adapter_items;

    SwitchCompat gstToggle;
    SwitchCompat svcToggle;
    ConstraintLayout subTotalLayout;
    ConstraintLayout finalTotalLayout;
    TextView subTotalText;
    TextView gstText;
    TextView svcText;
    TextView finalTotalText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receipt_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.receipt_overview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //modify toolbar text based on page
        toolbarTitleText = findViewById(R.id.toolbar_title);
        toolbarTitleText.setText(R.string.receipt_details);

        //press add button to addPeople page
        //user needs to key in at least one item in order to proceed
        addPeopleButton = findViewById(R.id.add_to_split);
        addPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalTotalText.getText().toString().equals("NA")){
                    Toast.makeText(ReceiptOverview.this, "U never input any items bro",Toast.LENGTH_LONG).show();
                } else{
                    Intent intent = new Intent(ReceiptOverview.this, AddPeople.class);
                    startActivity(intent);
                }

            }
        });


        //press back arrow lead back to home fragment
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceiptOverview.this, MainActivity.class);
                intent.putExtra("fragmentToLoad","HomeFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });

        //press add button add new item to reycleview
        addItemButton = findViewById(R.id.add_item);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemFragment = new DialogFragment_AddItem();
                addItemFragment.show(getSupportFragmentManager(),"DialogFragment_AddItem");

            }
        });

        //show item list
        itemView = findViewById(R.id.recycle_view_items);
        adapter_items = new RecycleViewAdapter_Item(this,itemArray,null);
        itemView.setAdapter(adapter_items);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        adapter_items.notifyDataSetChanged();





        //Instantiate the ReceiptInstance Singleton Class
        Receipts instance = ReceiptInstance.getInstance();
        Nine_GST gst = new Nine_GST();
        Ten_SVC svc = new Ten_SVC();
        instance.set_gstamt(gst);
        instance.set_sctamt(svc);


        //allow user to toggle on and off for GST
        gstToggle = findViewById(R.id.include_gst_switch);
        gstToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isToggled) {
                if (isToggled){
                    Log.d("SwitchCompat","gst set on");
                    instance.setGST(true);
                    updateReceiptComputation();

                } else {
                    Log.d("SwitchCompat","gst set off");
                    instance.setGST(false);
                    updateReceiptComputation();
                }
            }
        });

        //allow user to toggle and off SVC
        svcToggle = findViewById(R.id.include_svc_switch);
        svcToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isToggled) {
                if (isToggled){
                    Log.d("SwitchCompat","svc set on");
                    instance.setSCT(true);
                    updateReceiptComputation();
                } else {
                    Log.d("SwitchCompat","svc set off");
                    instance.setSCT(false);
                    updateReceiptComputation();
                }
            }
        });


        //used to assign values to the dynamic textViews which we would edit in updateReceiptComputation
        subTotalLayout = findViewById(R.id.total_layout);
        subTotalText = subTotalLayout.findViewById(R.id.sub_total_amount);
        gstText = subTotalLayout.findViewById(R.id.gst_total_amount);
        svcText = subTotalLayout.findViewById(R.id.svc_total_amount);
        finalTotalLayout = findViewById(R.id.final_total);
        finalTotalText = finalTotalLayout.findViewById(R.id.grand_total_amount);

    }


    //when user presses add item, we would instantiate a DialogFragment_AddItem which would pass out a Item object
    //we will implement DialogFragmentListener.OnDataSelected to attach the fragment to this button and update the RecycleView
    @Override
    public void onDataSelected(int position, Item data) {
        itemArray.add(data);
        adapter_items.notifyItemInserted(itemArray.size() - 1);

    }


    //computations. Since ReceiptInstance has 1 instance, we need to call that instance.
    public void updateReceiptComputation(){
        ReceiptInstance.getInstance().calculate_receipt_subtotal();
        ReceiptInstance.getInstance().calculate_receipt_subtotal_gst();
        ReceiptInstance.getInstance().calculate_receipt_subtotal_sct();
        ReceiptInstance.getInstance().calculate_receipt_total();

        subTotalText.setText(ReceiptInstance.getInstance().getSubtotal());
        gstText.setText(ReceiptInstance.getInstance().getSubtotal_gst());
        svcText.setText(ReceiptInstance.getInstance().getSubtotal_sct());
        finalTotalText.setText(ReceiptInstance.getInstance().getTotal());
    }



}