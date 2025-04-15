package com.example.paisehpay.computation;

import android.util.Log;

import java.util.Collections;
import java.util.List;

import com.example.paisehpay.blueprints.Owe;
public class HeapSortHelper {
    //sort by date (ascending: Earliest)
    public static void sortByDateEarliest(List<Owe> owes){
        int n = owes.size();
        for (int i = n / 2 - 1; i >= 0; i--){
            heapifyDate(owes, n, i, true);
        }
        for (int i = n - 1; i > 0; i--){
            Collections.swap(owes, 0, i);
            heapifyDate(owes, i, 0, true);
        }
        Log.d("Heapsort", "earliest after sorting: "+ owes.toString());
        for (Owe owe : owes) {
            Log.d("Heapsort", "amount: " + owe.getDate().toString()+owe.getPerson());
        }
    }

    //sort by date (descending: Latest)
    public static void sortByDateLatest(List<Owe> owes){
        int n = owes.size();
        for (int i = n / 2 - 1; i >= 0; i--){
            heapifyDate(owes, n, i, false);
        }
        for (int i = n - 1; i > 0; i--){
            Collections.swap(owes, 0, i);
            heapifyDate(owes, i, 0, false);
        }
        Log.d("Heapsort", "latest after sorting: "+ owes.toString());
        for (Owe owe : owes) {
            Log.d("Heapsort", "amount: " + owe.getDate().toString()+owe.getPerson());
        }
    }

    //sort by amount (ascending: lowest first)
    public static void sortByAmount(List<Owe> owes){
        Log.d("Heapsort", " amount before sorting: "+ owes.toString());
        int n = owes.size();
        for (int i = n / 2 - 1; i >= 0; i--){
            heapifyAmount(owes, n, i);
        }
        for (int i = n - 1; i > 0; i--){
            Collections.swap(owes, 0, i);
            heapifyAmount(owes, i, 0);
        }

        //Log.d("Heapsort", "after sorting: "+ owes.toString());
    }

    //heapify for date (ascending/ descending)
    private static void heapifyDate(List<Owe> owes, int n, int i, boolean ascending){
        int current = i;
        int largest = current;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n){
            int compareResult = owes.get(left).getDate().compareTo(owes.get(largest).getDate());
            if (ascending ? compareResult > 0 : compareResult < 0){
                largest = left;
            }
        }
        if (right<n){
            int compareResult = owes.get(right).getDate().compareTo(owes.get(largest).getDate());
            if (ascending ? compareResult > 0 : compareResult < 0){
                largest = right;
            }
        }
        if (largest != current){
            Collections.swap(owes, current, largest);
            current = largest;
            //heapifyDate(owes, n, largest, ascending);
        }
    }

    //heapify for amount
    private static void heapifyAmount(List<Owe> owes, int n, int i){
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && owes.get(left).getAmount() > owes.get(largest).getAmount()){
            largest = left;
        }
        if (right < n && owes.get(right).getAmount() > owes.get(largest).getAmount()){
            largest = right;
        }
        if (largest != i){
            Collections.swap(owes, i, largest);
            heapifyAmount(owes, n, largest);
        }
    }
}


