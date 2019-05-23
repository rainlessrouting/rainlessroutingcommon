package de.rainlessrouting.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
*
* @author Julius Wulk
*
*/
public class Message implements Serializable {
	
	public final static String METADATA_KEY_PACKAGE_COUNT = "pc";
	public final static String METADATA_KEY_LATITUDE_COUNT = "lac";
	public final static String METADATA_KEY_LONGITUDE_COUNT = "loc";
	public final static String METADATA_KEY_INDEX = "idx";
	
	
	private static final long serialVersionUID = 1L;

	private boolean success; 
	
	private Map<String, Object> metadata;
	
	private Object payload;
	
	
	public Message(boolean success, Object payload){
		this.success = success;
		this.payload = payload;
		this.metadata = new HashMap<String, Object>();
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public Map<String, Object> getMetadata()
	{
		return metadata;
	}

	public Object getMetadata(String key)
	{
		return metadata.get(key);
	}
	
	public void setMetadata(Map<String, Object> metadata)
	{
		this.metadata = metadata;
	}
	
	public void addMetadata(String key, Object value)
	{
		metadata.put(key, value);
	}
	
	@Override
	public String toString() {
		return "Message [success=" + success + ", metadata=" + metadata + ", payload=" + payload + "]";
	}
	
}
