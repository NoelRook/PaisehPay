package com.example.paisehpay.tabBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.paisehpay.R;
import com.example.paisehpay.computation.FilterListener;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> { // adapter for spinner for owe and owed's more details
    private Context context;
    private List<String> items;
    private int selectedPosition = -1; // Store selected item
    private Spinner spinner;
    private FilterListener listener;
    TextView spinnerText;
    RadioButton spinnerButton;

    public SpinnerAdapter(Context context, List<String> items, Spinner spinner, FilterListener listener) {
        super(context, R.layout.spinner_items, items);
        this.context = context;
        this.items = items;
        this.spinner = spinner;
        this.listener = listener;
    }


    // View for dropdown list (with RadioButton)
    @Override
    public View getDropDownView(int position, View dropdownLayout, @NonNull ViewGroup parent) {
        if (dropdownLayout == null) {
            dropdownLayout = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown, parent, false);
        }

        // Find the TextView and RadioButton
        spinnerText= dropdownLayout.findViewById(R.id.spinner_items);
        spinnerButton= dropdownLayout.findViewById(R.id.spinner_button);

        // Ensure textView is not null and set the item text
        if (spinnerText != null) {
            spinnerText.setText(items.get(position));
        }

        // Make sure the RadioButton is visible and check it if selected
        spinnerButton.setChecked(position == selectedPosition);
        spinnerButton.setClickable(position == selectedPosition);  // Enable clicking for the selected item only
        spinnerButton.setFocusable(position == selectedPosition);  // Enable focus for the selected item only
        spinnerButton.setVisibility(View.VISIBLE); // Ensure the RadioButton is visible


        // Handle item click and update the selected position
        dropdownLayout.setOnClickListener(v -> {
                selectedPosition = position;
                notifyDataSetChanged();  // Refresh the dropdown view

                spinner.setSelection(position);  // Update the spinner selection
                //spinner.performClick();
                if (listener != null) {
                    listener.onFilterSelected(items.get(position));
                }
    });

        return dropdownLayout;
    }

    // View for selected item display (No RadioButton)
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_items, parent, false);
        }

        // Find the TextView
        TextView textView = convertView.findViewById(R.id.spinner_items);
        if (textView != null) {
            textView.setText(items.get(position));  // Set the text of the selected item
        }
        return convertView;
    }

}
