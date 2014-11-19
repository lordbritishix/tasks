package com.simpletaskmanager.datetimepicker;

import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements OnTimeSetListener  {
	private Date m_date;
	private OnTimeSetListener m_listener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        
        if (m_date != null) {
        	c.setTime(m_date);
        }
        
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
	}
	
	public void setTime(Date date) {
		m_date = date;
	}

	public void setOnTimeSetListener(OnTimeSetListener listener) {
		m_listener = listener;
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if (m_listener != null) {
			m_listener.onTimeSet(view, hourOfDay, minute);
		}
	}
}
