package de.rainlessrouting.common.db;

import java.util.List;
import java.util.Map;

public interface IDatabaseHandler {

	public void init() throws Exception;
	public void createTables() throws Exception;
	public void dropTables() throws Exception;
	
	public void saveValueGrid(DBValueGrid valueGrid) throws Exception;
	public DBValueGrid loadValueGrid(String gridId, long timestamp, long offset) throws Exception;
	public DBValueGrid[] loadValueGrids(String gridId, long fromTimestamp) throws Exception;
	public DBValueGrid[] loadValueGrids(String gridId) throws Exception;
	public DBValueGrid[] loadValueGrids() throws Exception;
	//public DBValueGrid[] loadValueGridByGridId(String gridId, long timestamp) throws Exception;
	
	public void saveGridInfo(DBGridInfo gridInfo) throws Exception;
	public DBGridInfo loadGridInfo(String gridId) throws Exception;
	public DBGridInfo[] loadGridInfos() throws Exception;
	
	public Map<Long, List<Long>> loadTimes(String gridId, long fromTimestamp, long xHoursBack) throws Exception;
}
