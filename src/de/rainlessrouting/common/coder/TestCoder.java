package de.rainlessrouting.common.coder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.rainlessrouting.common.model.PrecipitationReadingPoint;

public class TestCoder 
{

	private static List<PrecipitationReadingPoint> generateList1()
	{
		List<PrecipitationReadingPoint> list = new ArrayList<PrecipitationReadingPoint>();
		
		PrecipitationReadingPoint prp1 = new PrecipitationReadingPoint(1.234, 5.6789, new float[] {1,2,3,0,4,0,5,0,6,7,8,9});
		PrecipitationReadingPoint prp2 = new PrecipitationReadingPoint(1.234, 5.6789, new float[] {});
		PrecipitationReadingPoint prp3 = new PrecipitationReadingPoint(1.234, 5.6789, new float[] {0,0,0});
		
		list.add(prp1);
		list.add(prp2);
		list.add(prp3);
		
		return list;
	}
	
	private static List<PrecipitationReadingPoint> generateList2()
	{
		List<PrecipitationReadingPoint> list = new ArrayList<PrecipitationReadingPoint>();
		
		PrecipitationReadingPoint prp1 = new PrecipitationReadingPoint(1.234, 5.6789, new float[] {1,2,3,0,4,0,5,0,6,7,8,9});
		
		list.add(prp1);
		
		return list;
	}
	
	private static List<PrecipitationReadingPoint> generateRandomList()
	{
		Random random = new Random();
		List<PrecipitationReadingPoint> list = new ArrayList<PrecipitationReadingPoint>();
		
		for (int i=0; i < 100000; i++)
		{
			float[] randomData = new float[10];
			for (int j=0; j < randomData.length; j++)
			{
				randomData[j] = random.nextFloat()*10;
			}
			PrecipitationReadingPoint prp = new PrecipitationReadingPoint(random.nextDouble(), random.nextDouble(), randomData);
		
			list.add(prp);
		}
		
		return list;
	}
	
	public static void main(String[] args)
	{
		List<PrecipitationReadingPoint> prpListOriginal = generateRandomList();
		
		long startTime = System.currentTimeMillis();
		byte[] cbor = CborCoder.encode(prpListOriginal);
		long cBorEncodeTime = System.currentTimeMillis();
		byte[] zip = CompressionCoder.compress(cbor);
		long zipTime = System.currentTimeMillis();
		
		System.out.println("CBOR vs ZippedCBOR: " + cbor.length + " vs " + zip.length + "\n");
		
		byte[] unzip = CompressionCoder.decompress(zip);
		long unzipTime = System.currentTimeMillis();
		List<PrecipitationReadingPoint> prpListClone = CborCoder.decode(unzip);
		long cBorDecodeTime = System.currentTimeMillis();
		
		System.out.println("cBorEncodeTime: " + (cBorEncodeTime - startTime));
		System.out.println("zipTime       : " + (zipTime - cBorEncodeTime));
		System.out.println("unzipTime     : " + (unzipTime - zipTime));
		System.out.println("cBorDecodeTime: " + (cBorDecodeTime - unzipTime));
		
//		for (int i=0; i < prpListClone.size(); i++)
//		{
//			System.out.println("Original " + i + ": " + prpListOriginal.get(i));
//			System.out.println("Clone    " + i + ": " + prpListClone.get(i));
//		}
		
		if (prpListOriginal.equals(prpListClone))
			System.out.println("\nLists are equal");
		else
			System.err.println("\nLists are not equal");
	}
}
