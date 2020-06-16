package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.models.Category;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<Category> values;

    public CategorySpinnerAdapter(Context context, int textViewResourceId,
                       List<Category> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Category getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getCategoryLabel());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getCategoryLabel());

        return label;
    }
    @Override
    public int getPosition(Category category){
         for(int i=0; i<values.size();i++) {
            if (values.get(i).getCategoryName().equals(category.getCategoryName())){
                return i;
            }
        }
        return -1;
    }
}
