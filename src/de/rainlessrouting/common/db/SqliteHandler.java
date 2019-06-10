package de.rainlessrouting.common.db;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import de.rainlessrouting.common.util.Serializer;

public class SqliteHandler implements IDatabaseHandler {

	private static final String SQL_DROP_TABLE_VALUE_GRID = 
			"DROP TABLE IF EXISTS value_grid";
	
	private static final String SQL_DROP_TABLE_GRID_INFO = 
			"DROP TABLE IF EXISTS grid_info";
	
	private static final String SQL_CREATE_TABLE_VALUE_GRID = 
			"CREATE TABLE IF NOT EXISTS value_grid ( "
			+ "timestamp INTEGER NOT NULL, "
			+ "time_offset INTEGER NOT NULL, "
			+ "grid_id INTEGER NOT NULL,"
			+ "values_blob BLOB,"
			+ "PRIMARY KEY(timestamp,time_offset)"
			+ ")";
	
	private static final String SQL_CREATE_TABLE_GRID_INFO = 
			"CREATE TABLE IF NOT EXISTS grid_info ( "
			+ "id TEXT PRIMARY KEY, "
			+ "width INTEGER NOT NULL, "
			+ "height INTEGER NOT NULL, "
			+ "grid_blob BLOB )";
	
	private static final String SQL_INSERT_VALUE_GRID = 
			"INSERT INTO value_grid(timestamp,time_offset,grid_id,values_blob) VALUES (?,?,?,?)";
	
	private static final String SQL_INSERT_GRID_INFO = 
			"INSERT INTO grid_info(id,width,height,grid_blob) VALUES (?,?,?,?)";
	
	private static final String SQL_SELECT_VALUE_GRID_BY_TIMESTMP = 
			"SELECT * FROM value_grid WHERE grid_id=? AND timestamp>=? ORDER BY timestamp ASC, time_offset ASC"; // Attention, changing order requires changing ProviderController
	
	private static final String SQL_SELECT_VALUE_GRID_BY_TIMESTMP_AND_OFFSET = 
			"SELECT * FROM value_grid WHERE grid_id=? AND timestamp=? AND time_offset=? ORDER BY timestamp ASC, time_offset ASC"; // Attention, changing order requires changing ProviderController
	
	private static final String SQL_SELECT_VALUE_GRIDS_ALL = 
			"SELECT * FROM value_grid ORDER BY grid_id ASC, timestamp ASC, time_offset ASC"; // Attention, changing order requires changing ProviderController
	
	private static final String SQL_SELECT_VALUE_GRID_BY_GRID_ID = 
			"SELECT * FROM value_grid WHERE grid_id=? ORDER BY timestamp ASC, time_offset ASC"; // Attention, changing order requires changing ProviderController
	
	private static final String SQL_SELECT_GRID_INFO = 
			"SELECT * FROM grid_info WHERE id=?";
	
	private static final String SQL_SELECT_GRID_INFOS_ALL = 
			"SELECT * FROM grid_info";
	
	// private static final String SQL_UPDATE = 
	// 		"UPDATE person SET firstname=?, surname=?,age=? WHERE id=?";
	
	/** 
	 * As gridInfo objects are static, they do not need to be loaded and stored again in every turn.
	 * This variable caches gridInfo objects and hinders storing in every turn */
	private HashMap<String, DBGridInfo> gridInfoCache;
	
	// ToDo: Instead of throwing exception in the method headers I should handle these right within the methods in order to gracefully close the connection
	
	public void init() throws Exception
	{
		gridInfoCache = new HashMap<String, DBGridInfo>();
		Class.forName("org.sqlite.JDBC");
	}	
	
	private Connection getConnection() throws SQLException
	{
		File f = new File(Paths.get(System.getProperty("user.home"), "rainlessrouting").toString());
		if (!f.exists())
			f.mkdir();
		String path = Paths.get(System.getProperty("user.home"), "rainlessrouting", "rainlessrouting.db").toString();
		
//		System.out.println("SqliteHandler.init(): Connect to " + path);
		
		return DriverManager.getConnection("jdbc:sqlite:".concat(path));
	}
	
	private void closeConnection(Connection connection)
	{
		try 
		{
			connection.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void createTables() throws SQLException 
	{
		Connection connection = getConnection();
		
		Statement stmt = connection.createStatement();
		
		System.out.println("SqliteHandler.createTables(): Create Tables ... ");
		stmt.executeUpdate(SQL_CREATE_TABLE_GRID_INFO);
		stmt.executeUpdate(SQL_CREATE_TABLE_VALUE_GRID);
		
		closeConnection(connection);
	}
	
	public void dropTables() throws SQLException
	{
		Connection connection = getConnection();
		
		Statement stmt = connection.createStatement();
		
		System.out.println("SqliteHandler.dropTables(): Drop Tables ... ");
		stmt.executeUpdate(SQL_DROP_TABLE_GRID_INFO);
		stmt.executeUpdate(SQL_DROP_TABLE_VALUE_GRID);
		
		closeConnection(connection);
	}
	
	@Override
	public void saveValueGrid(DBValueGrid valueGrid) throws Exception 
	{
		Connection connection = getConnection();
		
		PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_VALUE_GRID);
		stmt.setLong(1, valueGrid.getTimestamp()); 
		stmt.setLong(2, valueGrid.getTimeOffset());
		stmt.setString(3, valueGrid.getGridId());
		stmt.setBytes(4, Serializer.serializeObject(valueGrid.getValues()));
	
//		System.out.println("SqliteHandler.saveValueGrid(): statement=" + SQL_INSERT_VALUE_GRID);
		stmt.executeUpdate();
		
		closeConnection(connection);
	}

	public DBValueGrid[] loadValueGrids(String gridId, long fromTimestamp) throws Exception 
	{
		Connection connection = getConnection();
		
//		System.out.println("SqliteHandler.loadValueGrid(): statement=" + SQL_SELECT_VALUE_GRID + " timestamp=" + timestamp);
				
		PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_VALUE_GRID_BY_TIMESTMP);
		stmt.setString(1, gridId);
		stmt.setLong(2, fromTimestamp);
		
		ResultSet rs = stmt.executeQuery();
		List<DBValueGrid> list = new ArrayList<DBValueGrid>();
		
		while(rs.next()) {
			
			long timestamp = rs.getLong("timestamp");
			long offset = rs.getLong("time_offset");
			byte[] byteArr = rs.getBytes("values_blob"); 
			
			System.out.println("SqliteHandler: Deserialize " + byteArr.length + " bytes for timestamp " + timestamp + " offset " + offset);
			double[][] values = (double[][])Serializer.deserializeBytes(byteArr);
			
			list.add(new DBValueGrid(timestamp, offset, gridId, values));
		}
	
		closeConnection(connection);
		
		return list.toArray(new DBValueGrid[0]);
	}
	
	public DBValueGrid loadValueGrid(String gridId, long timestamp, long offset) throws Exception 
	{
		Connection connection = getConnection();
		
//		System.out.println("SqliteHandler.loadValueGrid(): statement=" + SQL_SELECT_VALUE_GRID + " timestamp=" + timestamp);
				
		PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_VALUE_GRID_BY_TIMESTMP_AND_OFFSET);
		stmt.setString(1, gridId);
		stmt.setLong(2, timestamp);
		stmt.setLong(3, offset);
		
		ResultSet rs = stmt.executeQuery();
		
		DBValueGrid grid = null;
		while(rs.next()) {
			
			byte[] byteArr = rs.getBytes("values_blob"); 
			
			System.out.println("SqliteHandler: Deserialize " + byteArr.length + " bytes for timestamp " + timestamp + " offset " + offset);
			double[][] values = (double[][])Serializer.deserializeBytes(byteArr);
			
			grid = new DBValueGrid(timestamp, offset, gridId, values);
		}
	
		closeConnection(connection);
		
		return grid;
	}
	
	public DBValueGrid[] loadValueGrids(String gridId) throws Exception 
	{
		Connection connection = getConnection();
			
//		System.out.println("SqliteHandler.loadGridInfo(): statement=" + SQL_SELECT_VALUE_GRID_BY_GRID_ID + " gridId=" + gridId);
		
		PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_VALUE_GRID_BY_GRID_ID);
		stmt.setString(1, gridId);
		
		ResultSet rs = stmt.executeQuery();
		List<DBValueGrid> list = new ArrayList<DBValueGrid>();
		
		while(rs.next()) {
			
			long timestamp = rs.getLong("timestamp");
			long offset = rs.getLong("time_offset");
			byte[] byteArr = rs.getBytes("values_blob"); 
			
			double[][] values = (double[][])Serializer.deserializeBytes(byteArr);
			
			list.add(new DBValueGrid(timestamp, offset, gridId, values));
		}
		
		closeConnection(connection);
		
		return list.toArray(new DBValueGrid[0]);
	}
	
	/**
	 * Load all ValueGrids. This is mainly for debugging purposes.
	 */
	public DBValueGrid[] loadValueGrids() throws Exception 
	{
		Connection connection = getConnection();
		
//		System.out.println("SqliteHandler.loadValueGrid(): statement=" + SQL_SELECT_VALUE_GRID + " timestamp=" + timestamp);
		
		PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_VALUE_GRIDS_ALL);
		
		ResultSet rs = stmt.executeQuery();
		List<DBValueGrid> list = new ArrayList<DBValueGrid>();
		
		while(rs.next()) {
			
			long timestamp = rs.getLong("timestamp");
			long offset = rs.getLong("time_offset");
			String gridId = rs.getString("grid_id");
			byte[] byteArr = rs.getBytes("values_blob"); 
			
			double[][] values = (double[][])Serializer.deserializeBytes(byteArr);
			
			list.add(new DBValueGrid(timestamp, offset, gridId, values));
		}
	
		closeConnection(connection);
		
		return list.toArray(new DBValueGrid[0]);
	}
	
	public void saveGridInfo(DBGridInfo gridInfo) throws Exception
	{
		Connection connection = getConnection();
		
		if (gridInfoCache.containsKey(gridInfo.getId())) // do not store again, already stored
			return;
		
		PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_GRID_INFO);
		stmt.setString(1, gridInfo.getId()); 
		stmt.setInt(2, gridInfo.getWidth());
		stmt.setInt(3, gridInfo.getHeight());
		stmt.setBytes(4, Serializer.serializeObject(gridInfo.getGrid()));
	
//		System.out.println("SqliteHandler.saveGridInfo(): statement=" + SQL_INSERT_GRID_INFO);
		try
		{
			stmt.executeUpdate();
			gridInfoCache.put(gridInfo.getId(), gridInfo);
		}
		catch(SQLException exc)
		{
//			System.err.println("SqliteHandler: GridInfo already stored: gridInfo=" + gridInfo);
		}
		
		closeConnection(connection);
	}
	
	public DBGridInfo loadGridInfo(String gridId) throws Exception
	{
		Connection connection = getConnection();
		
//		System.out.println("SqliteHandler.loadGridInfo(): statement=" + SQL_SELECT_GRID_INFO + " gridId=" + gridId);
		
		PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_GRID_INFO);
		stmt.setString(1, gridId); 
		
		ResultSet rs = stmt.executeQuery();
		
		DBGridInfo gridInfo = null;
		while(rs.next()) {
			
			int width = rs.getInt("width");
			int height = rs.getInt("height");
			byte[] byteArr = rs.getBytes("grid_blob"); 
			
			DBGeoGrid grid = (DBGeoGrid)Serializer.deserializeBytes(byteArr);
			
			gridInfo = new DBGridInfo(gridId, width, height, grid);
			
			gridInfoCache.put(gridId, gridInfo);
		}
		
		closeConnection(connection);
		
		return gridInfo;
	}
	
	public DBGridInfo[] loadGridInfos() throws Exception
	{
		Connection connection = getConnection();
		
//		System.out.println("SqliteHandler.loadGridInfo(): statement=" + SQL_SELECT_GRID_INFO + " gridId=" + gridId);
		
		List<DBGridInfo> list = new ArrayList<DBGridInfo>();
		
		PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_GRID_INFOS_ALL);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()) {
			
			String gridId = rs.getString("id");
			int width = rs.getInt("width");
			int height = rs.getInt("height");
			byte[] byteArr = rs.getBytes("grid_blob"); 
			
			DBGeoGrid grid = (DBGeoGrid)Serializer.deserializeBytes(byteArr);
			
			DBGridInfo gridInfo = new DBGridInfo(gridId, width, height, grid);
			list.add(gridInfo);
			gridInfoCache.put(gridId, gridInfo);
		}
		
		closeConnection(connection);
		
		return list.toArray(new DBGridInfo[0]); // nothing found
	}
	
	/**
	 * Load all relevant times from the database. 
	 * This includes all past times (dating less than x hours back) with offset=0 (because these are real measurements 
	 * and additionally all ffsets of the most recent timestamp.
	 */
	public SortedMap<Long, List<Long>> loadTimes(String gridId, long fromTimestamp, int hoursAgo) throws Exception
	{
		Connection connection = getConnection();
		
		// this list will contain all timestamps of the last x hours and additionally all offsets of the most recent timestamp
		SortedMap<Long, List<Long>> timestampsAndOffsets = new TreeMap<Long, List<Long>>();
		
		PreparedStatement stmt = connection.prepareStatement("SELECT timestamp FROM value_grid WHERE grid_id=? AND timestamp > ? AND time_offset=0 ORDER BY timestamp ASC, time_offset ASC");
		stmt.setString(1, gridId);
		stmt.setLong(2, fromTimestamp - (1000*60*60 * hoursAgo));
		
		ResultSet rs = stmt.executeQuery();

		long mostRecentTimestamp = 0;
		while(rs.next()) {
			
			long timestamp = rs.getLong("timestamp");
			List<Long> list = new ArrayList<Long>();
			list.add(0l); // initially add offset 0 (because for all timestamps this has to be present)
			timestampsAndOffsets.put(timestamp, list);
			mostRecentTimestamp = timestamp;
		}
		
		// then take the most recent timestamp in order to additionally get all offsets for the most recent timestamp
		stmt = connection.prepareStatement("SELECT time_offset FROM value_grid WHERE grid_id=? AND timestamp=? AND time_offset > 0 ORDER BY time_offset ASC");
		stmt.setString(1, gridId);
		stmt.setLong(2, mostRecentTimestamp);
		
		rs = stmt.executeQuery();

		while(rs.next()) {
			
			long offset = rs.getLong("time_offset");
			timestampsAndOffsets.get(mostRecentTimestamp).add(offset);
		}
		
		closeConnection(connection);
		
		return timestampsAndOffsets;
	}
	
	public static void testValueGrid(SqliteHandler sqlite) throws Exception
	{
		double[][] values = new double[3][2];
		for (int i=0; i < values.length; i++)
		{
			for (int j=0; j < values[0].length; j++)
			{
				values[i][j] = i+j;
			}
		}
		DBValueGrid valueGrid = new DBValueGrid(1000000, 30000, "myTestGridInfo", values);

		sqlite.saveValueGrid(valueGrid);
		
		DBValueGrid[] valueGrid2 = sqlite.loadValueGrids("myTestGridInfo", 1000000);
		
		if (valueGrid.equals(valueGrid2[0]))
			System.out.println("ValueGrids are equal! " + valueGrid);
		else
			System.err.println("ValueGrids are NOT equal! valueGrid=" + valueGrid + "  valueGrid2=" + valueGrid2[0]);
		
		DBValueGrid[] valueGrid3 = sqlite.loadValueGrids("myTestGridInfo");
		
		if (valueGrid.equals(valueGrid3[0]))
			System.out.println("ValueGrids are equal! " + valueGrid);
		else
			System.err.println("ValueGrids are NOT equal! valueGrid=" + valueGrid + "  valueGrid3=" + valueGrid3[0]);
	}
	
	public static void testGridInfo(SqliteHandler sqlite) throws Exception
	{
		double[] lats = new double[] {1,2,3};
		double[] longs = new double[] {4,5,6};
		
		DBGeoGrid grid = new DBGeoGrid(lats, longs);
		DBGridInfo gridInfo = new DBGridInfo("myTestGridInfo", 3, 2, grid);
		
		sqlite.saveGridInfo(gridInfo);
		
		DBGridInfo gridInfo2 = sqlite.loadGridInfo("myTestGridInfo");
		
		if (gridInfo.equals(gridInfo2))
			System.out.println("GridInfos are equal! " + gridInfo);
		else
			System.err.println("GridInfos are NOT equal! gridInfo=" + gridInfo + "  gridInfo2=" + gridInfo2);
	}
	
//	public static void main(String[] args)
//	{
//		System.out.println("SqliteHandler.main: start test ...");
//		
//		SqliteHandler sqlite = new SqliteHandler();
//		try {
//			sqlite.init();
//			testValueGrid(sqlite);
//			testGridInfo(sqlite);
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
}
