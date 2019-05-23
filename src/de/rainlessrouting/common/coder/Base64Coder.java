package de.rainlessrouting.common.coder;

import java.util.Base64;

public class Base64Coder {

	public static String encode(byte[] data)
	{
		String s = Base64.getEncoder().encodeToString(data);
		return s;
	}
	
	public static byte[] decode(String data)
	{
		byte[] b = Base64.getDecoder().decode(data);
		return b;
	}
}
