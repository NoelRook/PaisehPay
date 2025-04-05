package com.example.paisehpay.dialogFragments;

public interface DialogFragmentListener<T> {
    //this interface allows data from a DialogFragment to populate a RecycleView
    void onDataSelected(int position, T data);
}
