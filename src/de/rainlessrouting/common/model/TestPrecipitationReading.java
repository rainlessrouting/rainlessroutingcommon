package de.rainlessrouting.common.model;

public class TestPrecipitationReading {

	private static PrecipitationReadingGrid generateGrid1()
	{
		int lonCount = 400;
		int latCount = 240;

		PrecipitationReadingGrid grid = new PrecipitationReadingGrid(400, 240);
		PrecipitationReadingGrid grid2 = new PrecipitationReadingGrid(400, 240);
		
		int i=0;
		
		for (int latIndex=0; latIndex < latCount; latIndex++)
		{
			for (int lonIndex=0; lonIndex < lonCount; lonIndex++)
			{
//				System.out.println("Lon=" + lonIndex + " Lat=" + latIndex);
				
				PrecipitationReadingPoint prp = new PrecipitationReadingPoint(latIndex, lonIndex, new float[] {3});
				grid.addPrecipitationReadingPoint(lonIndex, latIndex, prp);
				grid2.addPrecipitationReadingPoint(i, prp);
				
				i++;
			}
		}
		
		if (grid.equals(grid2))
			System.out.println("Grids gleich");
		else
			System.out.println("Grids nicht gleich");
		
		return grid;
	}
	
	public static void main(String[] args)
	{
		generateGrid1();
	}
}
