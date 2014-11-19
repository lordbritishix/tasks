package com.simpletaskmanager.activity.settings;

import java.util.HashMap;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.simpletaskmanager.calendar.CalendarManager;
import com.simpletaskmanager.R;

public class SettingsActivity extends PreferenceActivity {
	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			addPreferencesFromResource(R.xml.settings_preference);
		
			ListPreference calendars = (ListPreference) findPreference(getResources().getString(R.string.key_calendar));
			if (calendars != null) {
				HashMap<Long, String> values = CalendarManager.getInstance(getActivity()).getCalendars();
				CharSequence entries[] = new String[values.size()];
				CharSequence entryValues[] = new String[values.size()];
				
				int ctr = 0;
				for (Long key : values.keySet()) {
					entries[ctr] = values.get(key);
					entryValues[ctr] = String.valueOf(key);
					ctr++;
					Log.i("Info", values.get(key) + " " + String.valueOf(key));
				}
				
				calendars.setEntryValues(entryValues);
				calendars.setEntries(entries);
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(android.R.id.content, new SettingsFragment());
		ft.commit();
	}
}
