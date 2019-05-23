package de.rainlessrouting.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrecipitationReadingGrid  implements Serializable {

	private static final long serialVersionUID = 1L;

	private PrecipitationReadingPoint[][] prGrid;
	
	public PrecipitationReadingGrid(int longGridSize, int latGridSize) 
	{
		prGrid = new PrecipitationReadingPoint[latGridSize][longGridSize];
	}
	
	public void addPrecipitationReadingPoint(int longGridCellIndex, int latGridCellIndex, PrecipitationReadingPoint prp)
	{
		prGrid[latGridCellIndex][longGridCellIndex] = prp;
	}
	
	public void addPrecipitationReadingPoint(int overallGridCellIndex, PrecipitationReadingPoint prp)
	{
		int lon = overallGridCellIndex % getLongGridSize();
		int lat = overallGridCellIndex / getLongGridSize();
		
		prGrid[lat][lon] = prp;
	}
	
	public PrecipitationReadingPoint getPrecipitationReadingPoint(int longGridCellIndex, int latGridCellIndex)
	{
		return prGrid[latGridCellIndex][longGridCellIndex];
	}

	public int getLongGridSize()
	{
		return prGrid[0].length;
	}
	
	public int getLatGridSize()
	{
		return prGrid.length;
	}
	
	public List<PrecipitationReadingPoint> getPrecipitationReadingPoints() 
	{
		return getPrecipitationReadingPoints(0, getLongGridSize() * getLatGridSize());
	}
	
	public List<PrecipitationReadingPoint> getPrecipitationReadingPoints(int fromIndex, int toIndex) 
	{
		List<PrecipitationReadingPoint> list = new ArrayList<PrecipitationReadingPoint>();
		int i = 0;
		for (int latIndex=0; latIndex < getLatGridSize(); latIndex++)
		{
			for (int lonIndex=0; lonIndex < getLongGridSize(); lonIndex++)
			{
				if ((i >= fromIndex) && (i <= toIndex))
				{
					PrecipitationReadingPoint prp = getPrecipitationReadingPoint(lonIndex, latIndex);
					list.add(prp);
				}
				i++;				
			}
		}
		
		return list;
	}
	
	public String toString() {
		return "PrecipitationReadingGrid (lon x lat): " + getLongGridSize() + " x " + getLatGridSize();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(prGrid);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrecipitationReadingGrid other = (PrecipitationReadingGrid) obj;
		if (!Arrays.deepEquals(prGrid, other.prGrid))
			return false;
		return true;
	}
	
	public String verify() {

		String str = "";

		int nullCount = 0;
		for (int latIndex=0; latIndex < getLatGridSize(); latIndex++)
		{
			for (int longIndex=0; longIndex < getLongGridSize(); longIndex++)
			{
				if (prGrid[latIndex][longIndex] == null)
					nullCount++;
			}
		}

		str += "NullCount=" + nullCount + "\n";

		return str;
	}
}
