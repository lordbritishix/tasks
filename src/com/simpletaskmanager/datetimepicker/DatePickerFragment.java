package com.simpletaskmanager.datetimepicker;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	private Date m_date;
	private DatePickerDialog.OnDateSetListener m_listener;	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
		
		final Calendar c = Calendar.getInstance();	

		if (m_date != null){
			c.setTime(m_date);
		}
		
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		if (m_listener != null) {
			m_listener.onDateSet(view, year, monthOfYear, dayOfMonth);
		}
	}
	
	public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
		m_listener = listener;
	}
	
	public void setDate(Date date) {
		m_date = date;
	}
}
