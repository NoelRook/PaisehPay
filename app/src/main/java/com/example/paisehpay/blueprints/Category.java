package com.example.paisehpay.blueprints;

// class used to populate category recycler view and for category recycler view adapter
public class Category {
    String categoryName;
    int categoryIcon;

    // constructor
    public Category(String categoryName, int categoryIcon){
        this.categoryName = categoryName;
        this.categoryIcon =categoryIcon;
    }

    // getters
    public String getCategoryName() {
        return categoryName;
    }
    public int getCategoryIcon() {
        return categoryIcon;
    }
}
