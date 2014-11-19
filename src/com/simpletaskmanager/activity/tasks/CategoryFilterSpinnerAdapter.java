package com.simpletaskmanager.activity.tasks;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simpletaskmanager.activity.tasks.CategoryFilterSpinnerAdapter.CategoryFilter;

public class CategoryFilterSpinnerAdapter extends ArrayAdapter<CategoryFilter> {
	public enum CategoryFilter {
		ALL,
		WORK,
		PERSONAL,
		OTHERS
	}

	public CategoryFilterSpinnerAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1, CategoryFilter.values());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        ((TextView) v).setTextColor(Color.LTGRAY);
        
        return v;
		
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = super.getDropDownView(position, convertView,
                parent);

        ((TextView)v).setTextColor(Color.WHITE);

        return v;
	}
}
