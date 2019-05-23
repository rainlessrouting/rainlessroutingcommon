package de.rainlessrouting.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

	public static byte[] serializeObject(Object obj) throws IOException {
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bytesOut);
		oos.writeObject(obj);
		oos.flush();
		byte[] bytes = bytesOut.toByteArray();
		bytesOut.close();
		oos.close();
		return bytes;
	}
	
	public static Object deserializeBytes(byte[] bytes) throws IOException, ClassNotFoundException
	{
	   ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
	   ObjectInputStream ois = new ObjectInputStream(bytesIn);
	   Object obj = ois.readObject();
	   ois.close();
	   return obj;
	}
	
	
}
