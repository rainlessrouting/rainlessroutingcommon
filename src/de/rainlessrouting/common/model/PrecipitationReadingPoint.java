package de.rainlessrouting.common.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;

// import com.fasterxml.jackson.annotation.JsonIgnore;


public class PrecipitationReadingPoint implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private double lat;
	private double lon;

	/** This is a bit mask. If a bit is set to 1, a precipitation value > 0 is expected at the corresponding index. The value is stored in precipitationValues. */
	private BitSet bs;
	private float[] precipitationValues;
	private short precipitationValueCount;
	
	public PrecipitationReadingPoint() {}

	public PrecipitationReadingPoint(double lat, double lon, float[] prec) {
		this.setLat(lat);
		this.setLon(lon);
		this.setPrecipitation(prec);
	}
	
	// @JsonIgnore // ignore when creating messages
	public float[] getPrecipitation() {
		float[] b = new float[precipitationValueCount];
		int j = 0;
		for (int i=0; i < b.length; i++)
		{
			if (bs.get(i))
				b[i] = precipitationValues[j++];
			else
				b[i] = 0;
		}
		return b;
	}
	
	private void setPrecipitation(float[] values) 
	{
		precipitationValueCount = (short)values.length;
		bs = new BitSet(precipitationValueCount);
		// iterate over the array and if precipitation value is non-zero update the bit mask
		for (int i=0; i < values.length; i++)
		{
			if (values[i] > 0)
				bs.set(i);
		}
		
		precipitationValues = new float[bs.cardinality()]; // size equals non-zero precipitation values
		int j = 0;
		for (int i=0; i < values.length; i++)
		{
			if (values[i] > 0)
			{
				precipitationValues[j++] = values[i];
			}
		}
	}

	/** Methods used for Spring Message-Generation */
	public BitSet getBs() {
		return bs;
	}
	public void setBs(BitSet bs) {
		this.bs = bs;
	}
	public float[] getPV() {
		return precipitationValues;
	}
	public void setPV(float[] precipitationValues) {
		this.precipitationValues = precipitationValues;
	}
	public short getPVC() {
		return precipitationValueCount;
	}
	public void setPVC(short precipitationValueCount) {
		this.precipitationValueCount = precipitationValueCount;
	}
	public double getLon() {
		return this.lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bs == null) ? 0 : bs.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + precipitationValueCount;
		result = prime * result + Arrays.hashCode(precipitationValues);
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
		PrecipitationReadingPoint other = (PrecipitationReadingPoint) obj;
		if (bs == null) {
			if (other.bs != null)
				return false;
		} else if (!bs.equals(other.bs))
			return false;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
			return false;
		if (precipitationValueCount != other.precipitationValueCount)
			return false;
		if (!Arrays.equals(precipitationValues, other.precipitationValues))
			return false;
		// this is not required, but only used for strict testing
		if (!Arrays.equals(this.getPrecipitation(), other.getPrecipitation()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		// return "Prec.ReadingPoint [" + lat + "/" + lon + ", prec.length=" + bs.size() + ", prec.cardinality=" + bs.cardinality() + " values=" + Arrays.toString(precipitationValues) + "]";
		return "Prec.ReadingPoint [" + lat + "/" + lon + ", prec.count=" + precipitationValueCount + ", prec= " + Arrays.toString(getPrecipitation()) + "]";
	}
	
}

