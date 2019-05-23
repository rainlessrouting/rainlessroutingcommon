package de.rainlessrouting.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyTest {

	private SortedMap<Long, SortedMap<Long, double[][]>> values;
	
	public void init()
	{
		SortedMap<Long, double[][]> offsetMap0 = new TreeMap();
		offsetMap0.put(0L, new double[][] {{1,2,3},{4,5,6}});
		
		SortedMap<Long, double[][]> offsetMap1 = new TreeMap();
		offsetMap1.put(0L, new double[][] {{1,2,3},{4,5,6}});
		offsetMap1.put(1L, new double[][] {{1,2,3},{4,5,6}});
		
		SortedMap<Long, double[][]> offsetMap2 = new TreeMap();
		offsetMap2.put(0L, new double[0][0]);
		
		SortedMap<Long, double[][]> offsetMap3 = new TreeMap();
		offsetMap3.put(0L, null);
		
		
		values = new TreeMap<Long, SortedMap<Long,double[][]>>();
		
		values.put(5L, offsetMap3);
		
		values.put(4L, offsetMap1);
		
		values.put(3L, null);
		
		values.put(1L, offsetMap2);
		values.put(2L, offsetMap3);
	}
	
	
	public List<Long> generateTopList(int topN, boolean withValues)
    {
        // Note: Timestamp and offset is coded into one value. Therefore, timestamp is * 1000 and offset is added
        List<Long> topList = new ArrayList<>(topN);

        // Prio 1: Neuester Timestamp, offset=0
        // Prio 2: Neuester Timestamp, offset=1...(n/2)
        // Prio 3: n/2-1 vorige Timestamps, offset=0

        long mostRecentTimestamp = values.lastKey();

        // Prio 1
        boolean containsOffset = values.get(mostRecentTimestamp) != null && values.get(mostRecentTimestamp).containsKey(0L);
        boolean containsValueMap = containsOffset && (values.get(mostRecentTimestamp).get(0L) != null) && values.get(mostRecentTimestamp).get(0L).length > 0;

        if (withValues && containsValueMap) // has a value map for offset 0
        {
            topList.add(mostRecentTimestamp * 1000 + 0); // + 0 is the offset
        }
        else if (!withValues && !containsValueMap) // has not an offset 0 or no value map for offset 0
        {
            topList.add(mostRecentTimestamp * 1000 + 0); // + 0 is the offset
        }

        // Prio 2
        Iterator<Long> iterOffset = values.get(mostRecentTimestamp).keySet().iterator();
        if (iterOffset.hasNext())
            iterOffset.next(); // skip first element, because we already included offset 0
        while (iterOffset.hasNext())
        {
            long nextOffset = iterOffset.next();
            if (topList.size() < (topN / 2) + 1) 
            {
                containsValueMap = values.get(mostRecentTimestamp).get(nextOffset).length > 0;
                if (withValues && containsValueMap)
                    topList.add(mostRecentTimestamp * 1000 + nextOffset);
                else if (!withValues && !containsValueMap)
                    topList.add(mostRecentTimestamp * 1000 + nextOffset);
            }
            else
                break;
        }

        // Prio 3
        Long[] timestampArray = new Long[values.keySet().size()];
        values.keySet().toArray(timestampArray);
        
        for (int i=timestampArray.length-2; i >= 0; i--) // -2, because we do not need the last (most recent timestamp), because we already added it in Prio 1
        {
        	if ((i >= timestampArray.length) || (topList.size() >= topN)) // check i) array size is exceeded and ii) topList-size already reached
        		break;
        	
            long t = timestampArray[i];
            containsOffset = values.get(t) != null && values.get(t).containsKey(0L);
            containsValueMap = containsOffset && values.get(t).get(0L) != null && values.get(t).get(0L).length > 0;

            if (withValues && containsValueMap) // has a value map for offset 0
            {
                topList.add(t * 1000 + 0); // + 0 is the offset
            }
            else if (!withValues && !containsValueMap) // has not an offset 0 or no value map for offset 0
            {
                topList.add(t * 1000 + 0); // + 0 is the offset
            }
        }
        
        return topList;
    }
	
	public static void main(String[] args)
	{
		MyTest test = new MyTest();
		test.init();
		List<Long> l = test.generateTopList(5, false);

		System.out.println(l);
	}
}
