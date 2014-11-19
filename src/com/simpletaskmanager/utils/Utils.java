package com.simpletaskmanager.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	public static Date getRandomDate(Timestamp from, Timestamp to) {
		long offset = from.getTime();
		long end = to.getTime();
		long diff = end - offset + 1;
		Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
		
		return new Date(rand.getTime());
	}
	
	public static Date getDateAfter(int after) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		cal.add(Calendar.DAY_OF_MONTH, + after);
		return cal.getTime();
	}
	
	public static Date getDateAfterEndOfDay(int after) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 18);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		cal.add(Calendar.DAY_OF_MONTH, + after);
		return cal.getTime();
	}


}
