package com.example.paisehpay_iris;

import android.widget.ImageButton;

public class Category {
    //class for category recycle view
    String categoryName;
    int categoryIcon;

    Category(String categoryName,int categoryIcon){
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }
}
