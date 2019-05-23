package de.rainlessrouting.common.db;

import java.util.Arrays;

/**
 * An instance of a reading. A reading is represented as a grid. A reading may be an actual sensor reading or a calculated forecast. Each grid cell has its own value. Each cell corresponds to a geopoint. 
 */
public class DBValueGrid {

	/**
	 * Timestamp of this reading.
	 */
	private long timestamp;
	
	/**
	 * Offset in millisecs. 
	 * If offset == 0, this reading represents the current state (i.e. it is a real sensor reading).
	 * If offset > 0, this reading is a calculated forecast.
	 */
	private long timeOffset;
	
	/**
	 * String representing a GridInfo that contains metadata like width, height and lat/lon.
	 */
	private String gridId;
	
	/**
	 * Values are stores in a 2d-array. Horizontal corresponds to latitude, vertical corresponds to longitude.
	 * Example: values[0][x] corresponds to the first line of values
	 */
	private double[][] values;
	
	public DBValueGrid(long timestamp, long timeOffset, String gridId, double[][] values)
	{
		setTimestamp(timestamp);
		setTimeOffset(timeOffset);
		setGridId(gridId);
		setValues(values);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(long timeOffset) {
		this.timeOffset = timeOffset;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public double[][] getValues() {
		return values;
	}

	public void setValues(double[][] values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "DBValueGrid [timestamp=" + timestamp + ", timeOffset=" + timeOffset + ", gridId=" + gridId;
	}
	
	public String toFullString() {
		String str = "DBValueGrid [timestamp=" + timestamp + ", timeOffset=" + timeOffset + ", gridId=" + gridId + ", values=";
		for (int row=0; row < values.length; row++)
		{
			str += "#" + row + ":";
			for (int col=0; col < values[0].length; col++)
			{
				 str += (int) values[row][col] + ",";
			}
			str += "\n";
		}
		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gridId == null) ? 0 : gridId.hashCode());
		result = prime * result + (int) (timeOffset ^ (timeOffset >>> 32));
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + Arrays.deepHashCode(values);
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
		DBValueGrid other = (DBValueGrid) obj;
		if (gridId == null) {
			if (other.gridId != null)
				return false;
		} else if (!gridId.equals(other.gridId))
			return false;
		if (timeOffset != other.timeOffset)
			return false;
		if (timestamp != other.timestamp)
			return false;
		if (!Arrays.deepEquals(values, other.values))
			return false;
		return true;
	}
	
	
}
