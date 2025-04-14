package com.example.paisehpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paisehpay.R;
import com.example.paisehpay.mainActivityFragments.AddFragment;
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

        scanButton.setOnClickListener(v -> {
            v.setEnabled(false);
            navigateToAddFragment();
            new Handler(Looper.getMainLooper()).postDelayed(this::dismissAllowingStateLoss, 50);
            //we set a timer so that the add fragment loads in time
        });

        manualButton.setOnClickListener(v -> {
            v.setEnabled(false);
            navigateToReceiptOverview();
            new Handler(Looper.getMainLooper()).postDelayed(this::dismissAllowingStateLoss, 50);
        });
    }

    //this is used to load the add fragment through the ModalBottomSheet instead of the bottom navigation bar.
    private void navigateToAddFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.replaceFragment(new AddFragment()); // Switch fragment
            mainActivity.binding.bottomNavigationView.setSelectedItemId(R.id.add); // Update bottom nav
        }
        dismiss(); // Close Bottom Sheet
    }

    private void navigateToReceiptOverview() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), ReceiptOverview.class);
            startActivity(intent);
        }
        dismiss();
    }
}

