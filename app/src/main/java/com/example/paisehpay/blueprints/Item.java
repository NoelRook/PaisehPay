package com.example.paisehpay.blueprints;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Item implements Parcelable {
    //used in recycler view of item in each expense (add people and receipt overview page)
    private String itemId;
    private String itemName;
    private double itemPrice;
    private String expenseId;
    private boolean settled = false;
    private HashMap<String, Double> debtPeople ;// {userid: settled or not settled}, not paid
    // if for all items in totalOwed == 0, user is settled
    private String itemPeopleString = ""; // not stored in DB
    private double itemIndividualPrice = 0.0; // not stored in DB
    private ArrayList<User> itemPeopleArray = new ArrayList<>(); // not stored in DB

    // constructors
    public Item(){
        //default constructor required for firebase
    }

    public Item(String itemId,
                String itemName,
                double itemPrice,
                String expenseId,
                String itemPeopleString, HashMap<String, Double> debtpeople){
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.expenseId = expenseId;
        this.itemPeopleString = itemPeopleString;
        this.debtPeople = debtpeople;
    }

    protected Item(Parcel in) { // constructor to create item from parcel
        itemId = in.readString();
        itemName = in.readString();
        itemPrice = in.readDouble();
        itemPeopleString = in.readString();
        itemIndividualPrice = in.readDouble();
        expenseId = in.readString();
        settled = in.readByte() != 0;

        // Read the size of the debtPeople map first
        int size = in.readInt();
        if (size > 0) {
            debtPeople = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                String key = in.readString();
                double value = in.readDouble();
                debtPeople.put(key, value);
            }
        }
    }

    // getters
    public String getItemName() {
        return itemName;
    }
    public String getItemPeopleString() {
        return itemPeopleString;
    }
    public ArrayList<User> getItemPeopleArray() {
        return itemPeopleArray;
    }
    public String getItemPriceString() {
        return "$" + itemPrice;
    }
    public double getItemPrice() {
        return itemPrice;
    }
    public String getItemId() {
        return itemId;
    }

    public String getExpenseId() {
        return expenseId;
    }
    public HashMap<String, Double> getDebtPeople() {
        return debtPeople;
    }


    // setters
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }
    public void setSettled(boolean settled) {
        this.settled = settled;
    }
    public void setDebtPeople(HashMap<String, Double> debtPeople) {this.debtPeople = debtPeople;}

    // functions
    public void setItemPeopleString(ArrayList<User> itemPeopleArray) { //take array of users and convert to string
        StringBuilder sb = new StringBuilder();
        for (User user:itemPeopleArray){
            sb.append(user.getUsername());
            sb.append(", ");
        }
        itemPeopleString = sb.substring(0,sb.length()-2);
    }

    public void setItemPeopleArray(ArrayList<User> arrayList){ // outputs the string of users
        itemPeopleArray = arrayList;
        setItemPeopleString(itemPeopleArray);
    }

    public void calculateDebts() { // calculate debts for each user
        if (itemPeopleArray != null && !itemPeopleArray.isEmpty()) {
            double splitAmount = itemPrice / itemPeopleArray.size();
            for (int i = 0; i<itemPeopleArray.size(); i++) {
                String userId = itemPeopleArray.get(i).getId();
                debtPeople.put(userId, splitAmount);
            }
        }
    }

    // parcelable functions
    public static final Creator<Item> CREATOR = new Creator<>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) { // write item data into a parcel
        parcel.writeString(itemId);
        parcel.writeString(itemName);
        parcel.writeDouble(itemPrice);
        parcel.writeString(itemPeopleString);
        parcel.writeDouble(itemIndividualPrice);
        parcel.writeString(expenseId);
        parcel.writeByte((byte) (settled ? 1 : 0));

        // Write debtPeople map
        if (debtPeople == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(debtPeople.size());
            for (Map.Entry<String, Double> entry : debtPeople.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeDouble(entry.getValue());
            }
        }
    }

    // database functions
    public Map<String, Object> ToMap() { // convert object data to map, useful to pass data to database
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", itemId);
        result.put("itemName", itemName);
        result.put("itemPrice", itemPrice);
        result.put("expenseId", expenseId);
        result.put("settled", settled);

        // Ensure debtPeople is properly converted to a Map
        if (debtPeople != null) {
            result.put("debtpeople", new HashMap<>(debtPeople));
        } else {
            result.put("debtpeople", new HashMap<>());
        }
        return result;
    }

    @NonNull
    public static Item fromDataSnapshot(@NonNull DataSnapshot snapshot) { // rebuilt an item object from database
        Item item = new Item();
        item.itemId = snapshot.getKey();
        item.itemName = snapshot.child("itemName").getValue(String.class);
        item.itemPrice = snapshot.child("itemPrice").getValue(Double.class);
        item.expenseId = snapshot.child("expenseId").getValue(String.class);
        item.settled = Boolean.TRUE.equals(snapshot.child("settled").getValue(Boolean.class));

        // Handle debtpeople
        DataSnapshot debtPeopleSnapshot = snapshot.child("debtpeople");
        if (debtPeopleSnapshot.exists()) {
            item.debtPeople = new HashMap<>();
            for (DataSnapshot child : debtPeopleSnapshot.getChildren()) {
                Double amount = child.getValue(Double.class);
                if (amount != null) {
                    item.debtPeople.put(child.getKey(), amount);
                }
            }
        }
        return item;
    }

    // string of all details of an item object
    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemPeopleString='" + itemPeopleString + '\'' +
                ", itemIndividualPrice=" + itemIndividualPrice +
                ", expenseId='" + expenseId + '\'' +
                ", itemPeopleArray=" + itemPeopleArray +
                ", settled=" + settled +
                ", debtPeople=" + debtPeople +
                '}';
    }
}
