package com.example.paisehpay_iris;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DialogFragment_SelectGroup extends androidx.fragment.app.DialogFragment {
    //popup when u select group during addpeople page

    View rootView;
    RecyclerView personView;

    ArrayList<Person> personArray = new ArrayList<>();



    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showPersonList() {
        String[] nameList = getResources().getStringArray(R.array.dummy_group_name_list);
        for (int i = 0; i<nameList.length; i++){
            personArray.add(new Person(nameList[i]));

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("DialogFragment", "Fragment attached successfully.");
    }

    // <!-- TODO: 1. reformat string with user data  -->
    // <!-- TODO: 2. currently never include the imagebutton in the layout -->
    // <!-- TODO: 3. figure out function that only lets u choose 1 group, and when touched changes image from + to tick (added_icon.png)-->
    // <!-- TODO: 4. above function may not be here  -->



}
