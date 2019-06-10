package de.rainlessrouting.common.util;

public class Logger {

	private static StringBuffer log = new StringBuffer();
	
	public static void log(String msg)
	{
		log.append(msg);
	}
	
	public static void logln(String msg)
	{
		log.append(msg).append("\n");
	}

	public static void clear()
	{
		log = new StringBuffer();
	}
	
	public static String getString()
	{
		return log.toString();
	}
}
