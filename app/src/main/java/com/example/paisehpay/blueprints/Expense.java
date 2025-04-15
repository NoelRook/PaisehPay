package com.example.paisehpay.blueprints;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.firebase.database.PropertyName;
import java.util.HashMap;
import java.util.Map;

public class Expense implements Parcelable {

    private String expenseId;
    @PropertyName("description")
    private String description;
    @PropertyName("created_at")
    private String expenseDate;
    @PropertyName("creator_id")
    private String expensePaidBy;
    @PropertyName("group_id")
    private String associatedGroup;
    private String expenseAction; // Optional field
    private String expenseAmount;
    @PropertyName("category")
    private String expenseCategory;
    @PropertyName("totalAmount")
    private double expenseOwed = 0.0;

    public Expense() {
        // Default constructor required for Firebase
    }

    // constructor
    public Expense(String expenseId, String description, String expenseDate,
                   String expensePaidBy, String associatedGroup, String expenseAction,
                   String expenseAmount, String expenseCategory) {
        this.expenseId = expenseId;
        this.description = description;
        this.expenseDate = expenseDate;
        this.expensePaidBy = expensePaidBy;
        this.associatedGroup = associatedGroup;
        this.expenseAction = expenseAction;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
    }

    // parcelable constructor
    protected Expense(Parcel in) {
        expenseId = in.readString();
        description = in.readString();
        expenseDate = in.readString();
        expensePaidBy = in.readString();
        associatedGroup = in.readString();
        expenseAction = in.readString();
        expenseAmount = in.readString();
        expenseCategory = in.readString();
    }


    // Getters
    public String getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public String getExpensePaidBy() {
        return expensePaidBy;
    }

    public String getAssociatedGroup() {return associatedGroup;}

    public String getExpenseAmount() {return expenseAmount;}

    public double getExpenseOwed() {return expenseOwed;}

    public String getExpenseCategory() {
        return expenseCategory != null ? expenseCategory : "Uncategorized";
    }

    // Setters
    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setExpensePaidBy(String expensePaidBy) {
        this.expensePaidBy = expensePaidBy;
    }

    public void setAssociatedGroup(String associatedGroup) {
        this.associatedGroup = associatedGroup;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public void setExpenseOwed(double expenseOwed) {
        this.expenseOwed = expenseOwed;
    }


    // functions
    public Map<String, Object> toMap() { // convert object data to map, useful to pass data to database
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("created_at", expenseDate);
        result.put("creator_id", expensePaidBy);
        result.put("currency", "SGD");
        result.put("category", expenseCategory);
        result.put("group_id", associatedGroup);
        result.put("totalAmount", expenseAmount);
        return result;
    }

    // parcelable functions
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Expense> CREATOR = new Creator<>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) { // write expense data into a parcel
        dest.writeString(expenseId);
        dest.writeString(description);
        dest.writeString(expenseDate);
        dest.writeString(expensePaidBy);
        dest.writeString(associatedGroup);
        dest.writeString(expenseAction);
        dest.writeString(expenseAmount);
        dest.writeString(expenseCategory);
    }

    // string of all details of an expense object
    @Override
    public String toString() {
        return "Expense{" +
                "expenseId='" + expenseId + '\'' +
                ", description='" + description + '\'' +
                ", expenseDate='" + expenseDate + '\'' +
                ", expensePaidBy='" + expensePaidBy + '\'' +
                ", associatedGroup='" + associatedGroup + '\'' +
                ", expenseAction='" + expenseAction + '\'' +
                ", expenseAmount='" + expenseAmount + '\'' +
                ", expenseCategory='" + expenseCategory + '\'' +
                '}';
    }
}

