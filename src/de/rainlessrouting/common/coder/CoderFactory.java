package de.rainlessrouting.common.coder;

public class CoderFactory {

	public final static String JSON = "JSON";
	public final static String CBOR = "CBOR";
	public final static String ZIPPED_CBOR = "CBOR_ZIP";
	public final static String CBOR_BASE64 = "CBOR_BASE64";
	
	public static String encode(Object o, String encoding)
	{
		if (encoding.equals(JSON))
			return JSONCoder.encode(o);
		else 
			throw new RuntimeException("Not supported yet");
	}
	
	public static Object decode(String s, String encoding, Class c)
	{
		if (encoding.equals(JSON))
			return JSONCoder.decode(s, c);
		else 
			throw new RuntimeException("Not supported yet");
	}
}
