package de.rainlessrouting.common.db;

public class DBGridInfo {

	private String id;
	private int width;
	private int height;
	private DBGeoGrid grid;
	
	public DBGridInfo(String id, int width, int height, DBGeoGrid grid)
	{
		setId(id);
		setWidth(width);
		setHeight(height);
		setGrid(grid);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public DBGeoGrid getGrid() {
		return grid;
	}

	public void setGrid(DBGeoGrid grid) {
		this.grid = grid;
	}

	@Override
	public String toString() {
		return "GridInfo [id=" + id + ", width=" + width + ", height=" + height + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grid == null) ? 0 : grid.hashCode());
		result = prime * result + height;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + width;
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
		DBGridInfo other = (DBGridInfo) obj;
		if (grid == null) {
			if (other.grid != null)
				return false;
		} else if (!grid.equals(other.grid))
			return false;
		if (height != other.height)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	
}
