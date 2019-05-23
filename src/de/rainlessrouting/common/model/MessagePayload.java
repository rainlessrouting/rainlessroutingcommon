package de.rainlessrouting.common.model;

public class MessagePayload 
{
	private String encoding;
	
	private Object data;
	
	public MessagePayload(Object data, String encoding)
	{
		setEncoding(encoding);
		setData(data);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "MessagePayload [encoding=" + encoding + ", data=" + data + "]";
	}
}
