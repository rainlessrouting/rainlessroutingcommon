package de.rainlessrouting.common.db;

import java.io.Serializable;
import java.util.Arrays;

public class DBGeoGrid implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double[] latitudes;
	private double[] longitudes;
	
	public DBGeoGrid(double[] latitudes, double[] longitude)
	{
		setLatitudes(latitudes);
		setLongitudes(longitude);
	}

	public double[] getLatitudes() {
		return latitudes;
	}

	public void setLatitudes(double[] latitudes) {
		this.latitudes = latitudes;
	}

	public double[] getLongitudes() {
		return longitudes;
	}

	public void setLongitudes(double[] longitudes) {
		this.longitudes = longitudes;
	}
	
	public int getWidth() {
		return longitudes.length;
	}
	
	public int getHeight() {
		return latitudes.length;
	}

	/**
	 * Get a grid of DBGeoPoints, rows first, i.e. DBGeoPoints[rows][cols], i.e. DBGeoPoints[longitude.size()][latitude.size()]
	 * @return
	 */
	public DBGeoPoint[][] getGeoGrid()
	{
		// rows first
		DBGeoPoint[][] result = new DBGeoPoint[getHeight()][getWidth()];
		
		// iterate through columns 0 to longSize and then process the next row
		// longtitudes are vertical lines, latitude horizontal lines. latitude is increased only after all longitudes (columns) for a latitude (row) have been processed
		for (int latIndex=0; latIndex < getHeight(); latIndex++)
		{
			for (int longIndex=0; longIndex < getWidth(); longIndex++)
			{
				double latValue = latitudes[latIndex]; //+ this.LATDIFF / 2.0 ;
				double lonValue =  longitudes[longIndex]; //+ this.LONDIFF / 2.0;
				
				DBGeoPoint geoPoint = new DBGeoPoint(latValue, lonValue);
				result[latIndex][longIndex] = geoPoint;
			}
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return "DBGeoGrid w=" + getWidth() + " x h=" + getHeight() + " [latitudes=" + Arrays.toString(latitudes) + ", longitudes=" + Arrays.toString(longitudes) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(latitudes);
		result = prime * result + Arrays.hashCode(longitudes);
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
		DBGeoGrid other = (DBGeoGrid) obj;
		if (!Arrays.equals(latitudes, other.latitudes))
			return false;
		if (!Arrays.equals(longitudes, other.longitudes))
			return false;
		return true;
	}
}
