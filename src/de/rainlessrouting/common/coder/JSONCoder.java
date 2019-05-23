package de.rainlessrouting.common.coder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONCoder {

	public static String encode(Object o)
	{
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(o);
		return json;
	}
	
	public static Object decode(String s, Class c)
	{
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(s, c);
	}
}
