package de.rainlessrouting.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeFormatter {

	public static String getDateTime(long timestamp)
	{
		DateFormat formatter = new SimpleDateFormat("EEE, dd.MM kk:mm");
		return formatter.format(new Date(timestamp));
	}
	
	public static String getDateTimeSeconds(long timestamp)
	{
		DateFormat formatter = new SimpleDateFormat("dd.MM kk:mm:ss");
		return formatter.format(new Date(timestamp));
	}
	
	public static String getTimeSeconds(long timestamp)
	{
		DateFormat formatter = new SimpleDateFormat("kk:mm:ss");
		return formatter.format(new Date(timestamp));
	}
}
