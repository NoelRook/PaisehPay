package com.example.paisehpay;

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
