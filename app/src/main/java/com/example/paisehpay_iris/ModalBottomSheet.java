package com.example.paisehpay_iris;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class ModalBottomSheet extends BottomSheetDialogFragment {
    //popup from below when u click add expense button on button nav page

    Button manualButton;
    Button scanButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.modal_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        manualButton = view.findViewById(R.id.manual_button);
        scanButton = view.findViewById(R.id.scan_button);

        manualButton.setOnClickListener(v -> {
            v.setEnabled(false);
            navigateToAddFragment();
            new Handler(Looper.getMainLooper()).postDelayed(this::dismissAllowingStateLoss, 50);
        });

        scanButton.setOnClickListener(v -> {
            v.setEnabled(false);
            navigateToAddFragment();
            new Handler(Looper.getMainLooper()).postDelayed(this::dismissAllowingStateLoss, 50);
        });
    }

    private void navigateToAddFragment() {
        // Get activity context
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.replaceFragment(new AddFragment()); // Switch fragment
            mainActivity.binding.bottomNavigationView.setSelectedItemId(R.id.add); // Update bottom nav
        }
        dismiss(); // Close Bottom Sheet
    }
}

// <!-- TODO: 1. if manual scan, skip camera page + reciept overview page and jump straight to expense details page -->
// <!-- TODO: 2. if auto scan, go to camera page  -->

