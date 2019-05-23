package de.rainlessrouting.common.coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.upokecenter.cbor.CBORObject;

import de.rainlessrouting.common.model.PrecipitationReadingPoint;

public class CborCoder {

	public static byte[] encode(List<PrecipitationReadingPoint> list) {
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int i = 0; i < list.size(); i++) {
				PrecipitationReadingPoint prp = list.get(i);
				CBORObject.Write(prp.getLat(), baos);
				CBORObject.Write(prp.getLon(), baos);
				CBORObject.Write(prp.getPVC(), baos);
				CBORObject.Write(serializeBitSet(prp.getBs()), baos);
				CBORObject.Write(Base64Coder.encode(floatArrayToByteArray(prp.getPV())), baos);
			}
			return baos.toByteArray();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		return null;
	}
	
	public static List<PrecipitationReadingPoint> decode(byte[] b) {
		try
		{
			List<PrecipitationReadingPoint> list = new ArrayList<PrecipitationReadingPoint>();
			
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			
			while(bais.available() > 0) 
			{
				PrecipitationReadingPoint prp = new PrecipitationReadingPoint();
				
				prp.setLat(CBORObject.Read(bais).AsDouble());
				prp.setLon(CBORObject.Read(bais).AsDouble());
				prp.setPVC(CBORObject.Read(bais).AsInt16());
				prp.setBs(deserializeBitSet(CBORObject.Read(bais).AsInt64()));
				prp.setPV(byteArrayToFloatArray(Base64Coder.decode(CBORObject.Read(bais).AsString())));
				
				list.add(prp);
			}
			
			return list;
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
		return null;
	}
	
//	public static byte[] encode(List<PrecipitationReadingPoint> list) {
//		CborBuilder builder = new CborBuilder();
//		for (int i = 0; i < list.size(); i++) {
//			PrecipitationReadingPoint prp = list.get(i);
//
//			builder
//			.add(prp.getLat())
//			.add(prp.getLon())
//			.add(new byte[] { prp.getPVC() })
//			.add(serializeBitSet(prp.getBs()))
//			.add(prp.getPV());
//		}
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		try {
//			new CborEncoder(baos).encode(builder.build());
//		} catch (CborException e) {
//			e.printStackTrace();
//		}
//
//		return baos.toByteArray();
//	}
//
//	public static List<PrecipitationReadingPoint> decode(byte[] b)
//	{
//		List<PrecipitationReadingPoint> list = new ArrayList<PrecipitationReadingPoint>();
//		
//		ByteArrayInputStream bais = new ByteArrayInputStream(b);
//		try 
//		{
//			List<DataItem> dataItems = new CborDecoder(bais).decode();
//			
//			for (int i=0; i < dataItems.size();) 
//			{
//				PrecipitationReadingPoint prp = new PrecipitationReadingPoint();
//				
//				prp.setLat(((DoublePrecisionFloat)dataItems.get(i++)).getValue());
//				prp.setLon(((DoublePrecisionFloat)dataItems.get(i++)).getValue());
//				prp.setPVC(((ByteString)dataItems.get(i++)).getBytes()[0]);
//				prp.setBs(deserializeBitSet(((co.nstant.in.cbor.model.Number)dataItems.get(i++)).getValue().longValue()));
//				prp.setPV(((ByteString)dataItems.get(i++)).getBytes());
//				
//				list.add(prp);
//			}
//		} catch (CborException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return list;
//	}
	
	public static byte[] floatArrayToByteArray(float[] floatArray) {
		byte byteArray[] = new byte[floatArray.length * 4];

		// wrap the byte array to the byte buffer
		ByteBuffer byteBuf = ByteBuffer.wrap(byteArray);

		// create a view of the byte buffer as a float buffer
		FloatBuffer floatBuf = byteBuf.asFloatBuffer();

		// now put the float array to the float buffer,
		// it is actually stored to the byte array
		floatBuf.put(floatArray);

		return byteArray;
	}
	
	public static float[] byteArrayToFloatArray(byte byteArray[]) {
		float floatArray[] = new float[byteArray.length / 4];

		// wrap the source byte array to the byte buffer
		ByteBuffer byteBuf = ByteBuffer.wrap(byteArray);

		// create a view of the byte buffer as a float buffer
		FloatBuffer floatBuf = byteBuf.asFloatBuffer();

		// now get the data from the float buffer to the float array,
		// it is actually retrieved from the byte array
		floatBuf.get(floatArray);

		return floatArray;
	}

	private static BitSet deserializeBitSet(long value) {
		BitSet bits = new BitSet();
		int index = 0;
		while (value != 0L) {
			if (value % 2L != 0) {
				bits.set(index);
			}
			++index;
			value = value >>> 1;
		}
		return (BitSet)bits.clone();
	}

	private static long serializeBitSet(BitSet bits) {
		long value = 0L;
		for (int i = 0; i < bits.length(); ++i) {
			value += bits.get(i) ? (1L << i) : 0L;
		}
		return value;
	}

}
