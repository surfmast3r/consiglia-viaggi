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

    private List<Category> values;

    public CategorySpinnerAdapter(Context context, int textViewResourceId,
                       List<Category> values) {
        super(context, textViewResourceId, values);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getCategoryLabel());

        return label;
    }

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
