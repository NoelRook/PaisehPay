package com.example.paisehpay.dialogFragments;

public interface DialogFragmentListener<T> {
    void onDataSelected(int position, T data);
}
